package com.hka.oms.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

@JsonDeserialize(builder = ShippingAddress.Builder.class)
public final class ShippingAddress {
  private final String street, city, zipCode, country;

  private ShippingAddress(Builder b) {
    this.street  = trim(b.street);
    this.city    = trim(b.city);
    this.zipCode = trim(b.zipCode);
    this.country = trim(b.country);
  }

  public String getStreet(){ return street; }
  public String getCity(){ return city; }
  public String getZipCode(){ return zipCode; }
  public String getCountry(){ return country; }

  public static Builder builder(){ return new Builder(); }
  @JsonPOJOBuilder(withPrefix = "")
  public static final class Builder {
    private String street, city, zipCode, country;
    @JsonProperty("street")
    public Builder street(String v){ this.street = v; return this; }
    @JsonProperty("city")
    public Builder city(String v){ this.city = v; return this; }
    @JsonProperty("zipCode")
    public Builder zipCode(String v){ this.zipCode = v; return this; }
    @JsonProperty("country")
    public Builder country(String v){ this.country = v; return this; }
    public ShippingAddress build(){ return new ShippingAddress(this); }
  }

  private static String trim(String s){ return s == null ? null : s.trim(); }
}
