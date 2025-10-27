package com.hka.shop.domain;

import java.math.BigDecimal;
import java.util.List;

public final class OrderMocks {
  private OrderMocks() {}

  public static List<Order> sampleOrders() {
    return List.of(electronicsOrder(), groceryOrder(), cancelledOrder());
  }

  public static Order electronicsOrder() {
    return Order.builder()
        .orderId("ORD-1001")
        .customer(customer("CUST-1001", "Anna", "Mueller"))
        .addItem(item("PRD-101", 1, "1299.00"))
        .addItem(item("PRD-205", 2, "199.50"))
        .shippingAddress(address("Hauptstrasse 12", "Stuttgart", "70173", "DE"))
        .status(OrderStatus.PAID)
        .build();
  }

  public static Order groceryOrder() {
    return Order.builder()
        .orderId("ORD-1002")
        .customer(customer("CUST-1002", "Bastian", "Schmidt"))
        .addItem(item("PRD-310", 6, "2.49"))
        .addItem(item("PRD-311", 2, "4.90"))
        .addItem(item("PRD-450", 1, "18.75"))
        .shippingAddress(address("Marktplatz 5", "Heidelberg", "69117", "DE"))
        .status(OrderStatus.SHIPPED)
        .build();
  }

  public static Order cancelledOrder() {
    return Order.builder()
        .orderId("ORD-1003")
        .customer(customer("CUST-1003", "Carla", "Neumann"))
        .addItem(item("PRD-808", 1, "89.95"))
        .shippingAddress(address("Bahnhofstrasse 20", "Karlsruhe", "76133", "DE"))
        .status(OrderStatus.CANCELLED)
        .build();
  }

  private static OrderItem item(String productId, int quantity, String price) {
    return OrderItem.builder()
        .productId(productId)
        .quantity(quantity)
        .price(new BigDecimal(price))
        .build();
  }

  private static Customer customer(String id, String prename, String name) {
    return Customer.builder()
        .customerId(id)
        .prename(prename)
        .name(name)
        .build();
  }

  private static ShippingAddress address(String street, String city, String zip, String country) {
    return ShippingAddress.builder()
        .street(street)
        .city(city)
        .zipCode(zip)
        .country(country)
        .build();
  }
}
