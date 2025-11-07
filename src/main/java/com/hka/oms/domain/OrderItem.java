package com.hka.oms.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.math.BigDecimal;
import java.util.Objects;

@JsonDeserialize(builder = OrderItem.Builder.class)
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
  @JsonPOJOBuilder(withPrefix = "")
  public static final class Builder {
    private String productId; private int quantity; private BigDecimal price;
    @JsonProperty("productId")
    public Builder productId(String v){ this.productId = v; return this; }
    @JsonProperty("quantity")
    public Builder quantity(int v){ this.quantity = v; return this; }
    @JsonProperty("price")
    public Builder price(BigDecimal v){ this.price = v; return this; }
    public OrderItem build(){ return new OrderItem(this); }
  }

  private static String trim(String s){ return s == null ? null : s.trim(); }
} 
