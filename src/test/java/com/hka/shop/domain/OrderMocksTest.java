package com.hka.shop.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OrderMocksTest {

  @Test
  void sampleOrdersReturnsThreeDistinctOrders() {
    List<Order> orders = OrderMocks.sampleOrders();
    assertEquals(3, orders.size());
    assertThrows(UnsupportedOperationException.class, () -> orders.add(OrderMocks.electronicsOrder()));
    assertEquals(List.of("ORD-1001", "ORD-1002", "ORD-1003"),
        orders.stream().map(Order::getOrderId).toList());
  }

  @Test
  void electronicsOrderHasExpectedDetails() {
    Order order = OrderMocks.electronicsOrder();
    assertEquals("ORD-1001", order.getOrderId());
    assertEquals(OrderStatus.PAID, order.getStatus());
    assertEquals(new BigDecimal("1698.00"), order.getTotalAmount());
    assertEquals(2, order.getItems().size());
    OrderItem first = order.getItems().get(0);
    assertEquals("PRD-101", first.getProductId());
    assertEquals(1, first.getQuantity());
    assertEquals(new BigDecimal("1299.00"), first.getPrice());
    OrderItem second = order.getItems().get(1);
    assertEquals("PRD-205", second.getProductId());
    assertEquals(2, second.getQuantity());
    assertEquals(new BigDecimal("199.50"), second.getPrice());
    ShippingAddress address = order.getShippingAddress();
    assertEquals("Hauptstrasse 12", address.getStreet());
    assertEquals("Stuttgart", address.getCity());
    assertEquals("70173", address.getZipCode());
    assertEquals("DE", address.getCountry());
    Customer customer = order.getCustomer();
    assertEquals("CUST-1001", customer.getCustomerId());
    assertEquals("Anna", customer.getPrename());
    assertEquals("Mueller", customer.getName());
  }

  @Test
  void groceryOrderComputesTotalAmountFromItems() {
    Order order = OrderMocks.groceryOrder();
    assertEquals("ORD-1002", order.getOrderId());
    assertEquals(OrderStatus.SHIPPED, order.getStatus());
    assertEquals(new BigDecimal("43.49"), order.getTotalAmount());
    assertEquals(List.of("PRD-310", "PRD-311", "PRD-450"),
        order.getItems().stream().map(OrderItem::getProductId).toList());
  }

  @Test
  void cancelledOrderKeepsGivenState() {
    Order order = OrderMocks.cancelledOrder();
    assertEquals("ORD-1003", order.getOrderId());
    assertEquals(OrderStatus.CANCELLED, order.getStatus());
    assertEquals(new BigDecimal("89.95"), order.getTotalAmount());
    assertEquals(1, order.getItems().size());
    assertEquals("PRD-808", order.getItems().get(0).getProductId());
  }
}
