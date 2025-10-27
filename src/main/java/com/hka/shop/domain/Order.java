package com.hka.shop.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public final class Order {
  private final String orderId;
  private final Customer customer;
  private final List<OrderItem> items;
  private final BigDecimal totalAmount;
  private final ShippingAddress shippingAddress;
  private final OrderStatus status;

  private Order(Builder b) {
    this.orderId = b.orderId;
    this.customer = Objects.requireNonNull(b.customer, "customer");
    this.items = List.copyOf(Objects.requireNonNull(b.items, "items"));
    if (items.isEmpty()) throw new IllegalArgumentException("order needs at least one item");
    this.shippingAddress = b.shippingAddress;
    this.status = b.status == null ? OrderStatus.CREATED : b.status;

    BigDecimal calculated = items.stream()
        .map(i -> i.getPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
        .reduce(BigDecimal.ZERO, BigDecimal::add)
        .setScale(2, RoundingMode.HALF_UP);

    this.totalAmount = (b.totalAmount != null ? b.totalAmount.setScale(2, RoundingMode.HALF_UP) : calculated);

    if (b.totalAmount != null && this.totalAmount.compareTo(calculated) != 0) {
      throw new IllegalArgumentException("totalAmount mismatch: provided=" + this.totalAmount + ", calculated=" + calculated);
    }
  }

  public String getOrderId(){ return orderId; }
  public Customer getCustomer(){ return customer; }
  public List<OrderItem> getItems(){ return items; }
  public BigDecimal getTotalAmount(){ return totalAmount; }
  public ShippingAddress getShippingAddress(){ return shippingAddress; }
  public OrderStatus getStatus(){ return status; }

  public Order withOrderId(String id){ return builderFrom(this).orderId(id).build(); }
  public Order withStatus(OrderStatus s){ return builderFrom(this).status(s).build(); }

  public static Builder builder(){ return new Builder(); }
  public static Builder builderFrom(Order o){
    return new Builder()
        .orderId(o.orderId)
        .customer(o.customer)
        .items(o.items)
        .totalAmount(o.totalAmount)
        .shippingAddress(o.shippingAddress)
        .status(o.status);
  }

  public static final class Builder {
    private String orderId;
    private Customer customer;
    private List<OrderItem> items = new ArrayList<>();
    private BigDecimal totalAmount;
    private ShippingAddress shippingAddress;
    private OrderStatus status;

    public Builder orderId(String v){ this.orderId = v; return this; }
    public Builder customer(Customer v){ this.customer = v; return this; }
    public Builder addItem(OrderItem v){ this.items.add(v); return this; }
    public Builder items(Collection<OrderItem> v){ this.items = new ArrayList<>(v); return this; }
    public Builder totalAmount(BigDecimal v){ this.totalAmount = v; return this; }
    public Builder shippingAddress(ShippingAddress v){ this.shippingAddress = v; return this; }
    public Builder status(OrderStatus v){ this.status = v; return this; }
    public Order build() {
        if (orderId == null || orderId.isBlank()) {
            orderId = generateId();
        }
        return new Order(this);
    }

    private static String generateId() {
        return "ORD-" + java.util.UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
  }
}