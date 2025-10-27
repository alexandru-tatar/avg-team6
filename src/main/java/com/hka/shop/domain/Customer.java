package com.hka.shop.domain;

public final class Customer {
  private final String customerId;
  private final String prename;
  private final String name;

  private Customer(Builder b) {
    this.customerId = trim(b.customerId);
    this.prename    = trim(b.prename);
    this.name       = trim(b.name);
    if (customerId == null || customerId.isBlank()) throw new IllegalArgumentException("customerId must not be blank");
  }

  public String getCustomerId() { return customerId; }
  public String getPrename()    { return prename; }
  public String getName()       { return name; }

  public static Builder builder() { return new Builder(); }
  public static final class Builder {
    private String customerId, prename, name;
    public Builder customerId(String v){ this.customerId = v; return this; }
    public Builder prename(String v){ this.prename = v; return this; }
    public Builder name(String v){ this.name = v; return this; }
    public Customer build(){ return new Customer(this); }
  }

  private static String trim(String s){ return s == null ? null : s.trim(); }
}