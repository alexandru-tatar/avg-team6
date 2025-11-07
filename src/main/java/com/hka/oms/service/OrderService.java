package com.hka.oms.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.hka.oms.domain.*;
import com.hka.oms.inventory.InventoryClient;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.UnaryOperator;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class OrderService {

  private static final Logger log = LoggerFactory.getLogger(OrderService.class);
  private final Map<String, Order> store = new ConcurrentHashMap<>();
  private final InventoryClient inventoryClient;

  public OrderService(InventoryClient inventoryClient) {
    this.inventoryClient = inventoryClient;
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

    Order existing = store.putIfAbsent(withId.getOrderId(), withId);
    if (existing != null) {
      throw new IllegalStateException("order already exists: " + withId.getOrderId());
    }
    return new OrderCreationResult(withId, reservation.getMessage());
  }

  public Order get(String orderId) {
    return Optional.ofNullable(store.get(orderId))
        .orElseThrow(() -> new NoSuchElementException("order not found: " + orderId));
  }

  public List<Order> list() {
    return List.copyOf(store.values());
  }

  public Order cancel(String orderId) {
    return mutate(orderId, o -> {
      if (o.getStatus() == OrderStatus.CANCELLED) throw new IllegalStateException("order already cancelled");
      if (o.getStatus() == OrderStatus.SHIPPED || o.getStatus() == OrderStatus.DELIVERED)
        throw new IllegalStateException("order cannot be cancelled after shipment");
      return o.withStatus(OrderStatus.CANCELLED);
    });
  }

  public Order updateStatus(String orderId, OrderStatus newStatus) {
    return mutate(orderId, o -> o.withStatus(newStatus));
  }

  private void validate(Order o) {
    if (o.getCustomer() == null || o.getCustomer().getCustomerId() == null || o.getCustomer().getCustomerId().isBlank()) {
      throw new IllegalArgumentException("customerId must not be blank");
    }
    if (o.getItems() == null || o.getItems().isEmpty()) {
      throw new IllegalArgumentException("order needs at least one item");
    }
    o.getItems().forEach(it -> {
      if (it.getProductId() == null || it.getProductId().isBlank()) {
        throw new IllegalArgumentException("productId must not be blank");
      }
      if (it.getQuantity() <= 0) {
        throw new IllegalArgumentException("quantity must be > 0");
      }
      if (it.getPrice() == null) {
        throw new IllegalArgumentException("price required");
      }
    });

    BigDecimal calculated = calculateTotal(o);
    if (o.getTotalAmount() == null) {
      throw new IllegalArgumentException("totalAmount must be provided and equal to the sum of items");
    }
    BigDecimal provided = o.getTotalAmount().setScale(2, RoundingMode.HALF_UP);
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
    return store.compute(orderId, (id, current) -> {
      if (current == null) throw new NoSuchElementException("order not found: " + id);
      return op.apply(current);
    });
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
    String random = java.util.UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
    String id = "ORD-" + timestamp + "-" + random;
    return id;
  }
}
