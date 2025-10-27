package com.hka.shop.domain;

import java.math.BigDecimal;
import java.util.Objects;

public final class OrderItem {
  private final String productId;
  private final int quantity;
  private final BigDecimal price;

  private OrderItem(Builder b) {
    this.productId = trim(Objects.requireNonNull(b.productId, "productId"));
    if (b.quantity <= 0) throw new IllegalArgumentException("quantity must be > 0");
    this.quantity = b.quantity;
    this.price    = Objects.requireNonNull(b.price, "price");
  }

  public String getProductId() { return productId; }
  public int getQuantity() { return quantity; }
  public BigDecimal getPrice() { return price; }

  public static Builder builder(){ return new Builder(); }
  public static final class Builder {
    private String productId; private int quantity; private BigDecimal price;
    public Builder productId(String v){ this.productId = v; return this; }
    public Builder quantity(int v){ this.quantity = v; return this; }
    public Builder price(BigDecimal v){ this.price = v; return this; }
    public OrderItem build(){ return new OrderItem(this); }
  }

  private static String trim(String s){ return s == null ? null : s.trim(); }
} 

