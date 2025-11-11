package com.hka.oms.wms.dto;

import java.time.Instant;
import java.util.List;

public record WmsFulfillmentResponse(
    String orderId,
    String status,
    List<Item> items,
    String trackingNumber,
    String carrier,
    Address address,
    Instant createdAt,
    Instant updatedAt
) {
  public record Item(String productId, String productName, int quantity) {}
  public record Address(String recipientName, String street, String postalCode, String city, String country) {}
}
