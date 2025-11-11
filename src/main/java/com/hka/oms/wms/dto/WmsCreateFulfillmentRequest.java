package com.hka.oms.wms.dto;

import java.util.List;

public record WmsCreateFulfillmentRequest(
    String orderId,
    List<Item> items,
    Address address
) {
  public record Item(String productId, String productName, int quantity) {}
  public record Address(String recipientName, String street, String postalCode, String city, String country) {}
}
