package com.hka.shop.service;

import com.hka.shop.domain.*;

import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.UnaryOperator;

@Service
public class OrderService {

  private final Map<String, Order> store = new ConcurrentHashMap<>();

  public Order create(Order incoming) {
    Order normalized = normalize(incoming);
    Order withId = (normalized.getOrderId() == null || normalized.getOrderId().isBlank())
        ? normalized.withOrderId(generateId())
        : normalized;

    Order existing = store.putIfAbsent(withId.getOrderId(), withId);
    if (existing != null) {
      throw new IllegalStateException("order already exists: " + withId.getOrderId());
    }
    return withId;
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

  private static String generateId() {
    var date = java.time.LocalDate.now();
    var random = java.util.UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    return "ORD-%d-%02d-%02d-%s".formatted(date.getYear(), date.getMonthValue(), date.getDayOfMonth(), random);
  }
}
