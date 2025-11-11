package com.hka.oms.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.hka.oms.domain.*;
import com.hka.oms.inventory.InventoryClient;
import com.hka.oms.payment.PaymentClient;
import com.hka.oms.payment.PaymentException;
import com.hka.oms.payment.dto.PaymentAuthorizeRequest;
import com.hka.oms.payment.dto.PaymentResponse;
import com.hka.oms.publisher.WmsPublisher;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

@Service
public class OrderService {

  private static final Logger log = LoggerFactory.getLogger(OrderService.class);
  private final Map<String, Order> store = new ConcurrentHashMap<>();
  private final InventoryClient inventoryClient;
  private final PaymentClient paymentClient;
  private final WmsPublisher wmsPublisher;

  public OrderService(InventoryClient inventoryClient, PaymentClient paymentClient, WmsPublisher wmsPublisher) {
    this.inventoryClient = inventoryClient;
    this.paymentClient = paymentClient;
    this.wmsPublisher = wmsPublisher;
  }

  public OrderCreationResult create(Order incoming) {
    Order normalized = normalize(Objects.requireNonNull(incoming, "order"));
    validate(normalized);
    ensureInventoryAvailability(normalized);

    Order withId = normalized.withOrderId(generateIdTs());
    inventory.Inventory.ReserveItemsResponse reservation = inventoryClient.reserveItems(withId);

    if (!reservation.getSuccess()) {
      String message = reservation.getMessage().isBlank()
          ? "inventory reservation failed"
          : reservation.getMessage();
      throw new InventoryUnavailableException(message);
    }

    log.info("Inventory reserved for {} -> {}", withId.getOrderId(), reservation.getMessage());

    OrderCreationResult result = withReservationGuard(withId, () -> {
      PaymentResponse payment = paymentClient.authorize(buildPaymentRequest(withId), withId.getOrderId());
      Order stored = persist(withId.withStatus(OrderStatus.PAID));
      return new OrderCreationResult(stored, reservation.getMessage(), payment);
    });

    publishToWms(result);
    return result;
  }

  public Order get(String orderId) {
    return Optional.ofNullable(store.get(orderId))
        .orElseThrow(() -> new NoSuchElementException("order not found: " + orderId));
  }

  public List<Order> list() {
    return List.copyOf(store.values());
  }

  public Order cancel(String orderId) {
    return mutate(orderId, order -> {
      if (order.getStatus() == OrderStatus.CANCELLED) {
        throw new IllegalStateException("order already cancelled");
      }
      if (order.getStatus() == OrderStatus.SHIPPED || order.getStatus() == OrderStatus.DELIVERED) {
        throw new IllegalStateException("order cannot be cancelled after shipment");
      }
      return order.withStatus(OrderStatus.CANCELLED);
    });
  }

  public Order updateStatus(String orderId, OrderStatus newStatus) {
    return mutate(orderId, o -> o.withStatus(newStatus));
  }

  private void validate(Order o) {
    Optional.ofNullable(o.getCustomer())
        .map(Customer::getCustomerId)
        .filter(id -> !id.isBlank())
        .orElseThrow(() -> new IllegalArgumentException("customerId must not be blank"));

    Optional.ofNullable(o.getItems())
        .filter(list -> !list.isEmpty())
        .orElseThrow(() -> new IllegalArgumentException("order needs at least one item"))
        .forEach(this::validateItem);

    BigDecimal provided = Optional.ofNullable(o.getTotalAmount())
        .map(amount -> amount.setScale(2, RoundingMode.HALF_UP))
        .orElseThrow(() -> new IllegalArgumentException("totalAmount must be provided and equal to the sum of items"));

    BigDecimal calculated = calculateTotal(o);
    if (provided.compareTo(calculated) != 0) {
      throw new IllegalArgumentException("totalAmount mismatch: provided=" + provided + ", calculated=" + calculated);
    }
  }

  private BigDecimal calculateTotal(Order o) {
    return o.getItems().stream()
        .map(i -> i.getPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
        .reduce(BigDecimal.ZERO, BigDecimal::add)
        .setScale(2, RoundingMode.HALF_UP);
  }

  private Order mutate(String orderId, UnaryOperator<Order> op) {
    Objects.requireNonNull(op, "mutation operator");
    return Optional.ofNullable(store.computeIfPresent(orderId, (id, current) -> op.apply(current)))
        .orElseThrow(() -> new NoSuchElementException("order not found: " + orderId));
  }

  private Order normalize(Order in) {
    return Order.builderFrom(in).build();
  }

  private void ensureInventoryAvailability(Order order) {
    boolean available = inventoryClient.checkAvailability(order.getItems());
    if (!available) {
      throw new InventoryUnavailableException("inventory not available for requested items");
    }
  }

  private static String generateIdTs() {
    java.time.LocalDateTime now = java.time.LocalDateTime.now();
    java.time.format.DateTimeFormatter fmt = java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss");
    String timestamp = now.format(fmt);
    String random = UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
    return "ORD-" + timestamp + "-" + random;
  }

  private PaymentAuthorizeRequest buildPaymentRequest(Order order) {
    return new PaymentAuthorizeRequest(
        order.getOrderId(),
        order.getTotalAmount(),
        paymentClient.properties().currency(),
        paymentClient.properties().method()
    );
  }

  private Order persist(Order order) {
    Optional.ofNullable(store.putIfAbsent(order.getOrderId(), order))
        .ifPresent(existing -> {
          throw new IllegalStateException("order already exists: " + order.getOrderId());
        });
    return order;
  }

  private <T> T withReservationGuard(Order order, Supplier<T> action) {
    try {
      return action.get();
    } catch (PaymentException ex) {
      log.warn("Downstream failure for order {}, releasing inventory", order.getOrderId());
      inventoryClient.releaseReservation(order.getOrderId());
      throw ex;
    } catch (RuntimeException ex) {
      inventoryClient.releaseReservation(order.getOrderId());
      throw ex;
    }
  }

  private void validateItem(OrderItem item) {
    Optional.ofNullable(item.getProductId())
        .filter(id -> !id.isBlank())
        .orElseThrow(() -> new IllegalArgumentException("productId must not be blank"));
    if (item.getQuantity() <= 0) {
      throw new IllegalArgumentException("quantity must be > 0");
    }
    Optional.ofNullable(item.getPrice())
        .orElseThrow(() -> new IllegalArgumentException("price required"));
  }
  private void publishToWms(OrderCreationResult result) {
    try {
      wmsPublisher.publishOrderCreated(result);
    } catch (RuntimeException ex) {
      log.warn("Order {} created but failed to publish to WMS", result.order().getOrderId(), ex);
    }
  }
}
