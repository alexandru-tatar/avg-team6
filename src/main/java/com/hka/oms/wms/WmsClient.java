package com.hka.oms.wms;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hka.oms.domain.Order;
import com.hka.oms.domain.OrderItem;
import com.hka.oms.domain.ShippingAddress;
import com.hka.oms.wms.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;

@Component
public class WmsClient {

  private static final Logger log = LoggerFactory.getLogger(WmsClient.class);
  private static final String IDEMPOTENCY_HEADER = "Idempotency-Key";

  private final RestClient restClient;
  private final WmsProperties properties;
  private final ObjectMapper objectMapper;

  public WmsClient(RestClient.Builder builder, WmsProperties properties, ObjectMapper objectMapper) {
    this.restClient = builder
        .baseUrl(properties.baseUrl())
        .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
        .build();
    this.properties = properties;
    this.objectMapper = objectMapper;
  }

  public WmsFulfillmentResponse orchestrateFulfillment(Order order, String idempotencyKey) {
    log.info("Triggering WMS fulfillment workflow for {}", order.getOrderId());

    WmsFulfillmentResponse response = createFulfillment(order, idempotencyKey);
    response = post("/wms/fulfillments/start-picking",
        new WmsStartPickingRequest(order.getOrderId(), pickAssignee(order)), null);
    response = post("/wms/fulfillments/complete-picking",
        new WmsCompletePickingRequest(order.getOrderId()), null);
    response = post("/wms/fulfillments/pack",
        new WmsPackRequest(order.getOrderId(), estimateWeight(order), estimateDimensions(order)), null);
    response = post("/wms/fulfillments/ship",
        new WmsShipRequest(order.getOrderId(), defaultCarrier()), null);

    log.info("WMS workflow finished for {} -> status {}", order.getOrderId(), response.status());
    return response;
  }

  private WmsFulfillmentResponse createFulfillment(Order order, String idempotencyKey) {
    ShippingAddress address = order.getShippingAddress();
    if (address == null) {
      throw new IllegalArgumentException("shipping address required for fulfillment");
    }

    List<WmsCreateFulfillmentRequest.Item> items = order.getItems().stream()
        .map(this::toItem)
        .toList();

    WmsCreateFulfillmentRequest request = new WmsCreateFulfillmentRequest(
        order.getOrderId(),
        items,
        new WmsCreateFulfillmentRequest.Address(
            recipientName(order),
            address.getStreet(),
            address.getZipCode(),
            address.getCity(),
            address.getCountry()
        )
    );

    return post("/wms/fulfillments", request, idempotencyKey);
  }

  private WmsFulfillmentResponse post(String path, Object body, String idempotencyKey) {
    try {
      RestClient.RequestBodySpec spec = restClient.post().uri(path).body(body);
      if (StringUtils.hasText(idempotencyKey)) {
        spec = spec.header(IDEMPOTENCY_HEADER, idempotencyKey);
      }
      return spec.retrieve().body(WmsFulfillmentResponse.class);
    } catch (RestClientResponseException ex) {
      HttpStatus status = HttpStatus.resolve(ex.getStatusCode().value());
      String message = extractMessage(ex.getResponseBodyAsString());
      throw new WmsException(status == null ? HttpStatus.BAD_GATEWAY : status,
          message == null || message.isBlank() ? ex.getStatusText() : message);
    }
  }

  private String recipientName(Order order) {
    var customer = order.getCustomer();
    String prename = customer != null ? customer.getPrename() : "";
    String name = customer != null ? customer.getName() : "";
    return Stream.of(prename, name)
        .filter(StringUtils::hasText)
        .reduce((a, b) -> a + " " + b)
        .orElseGet(() -> customer != null ? customer.getCustomerId() : "unknown");
  }

  private WmsCreateFulfillmentRequest.Item toItem(OrderItem item) {
    return new WmsCreateFulfillmentRequest.Item(
        item.getProductId(),
        item.getProductId(),
        item.getQuantity()
    );
  }

  private BigDecimal estimateWeight(Order order) {
    return order.getItems().stream()
        .map(i -> BigDecimal.valueOf(Math.max(1, i.getQuantity())))
        .reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  private String estimateDimensions(Order order) {
    return order.getItems().size() + " items";
  }

  private String pickAssignee(Order order) {
    return "robot-" + Math.abs(order.getOrderId().hashCode() % 100);
  }

  private String defaultCarrier() {
    return "DHL";
  }

  private String extractMessage(String body) {
    if (!StringUtils.hasText(body)) {
      return null;
    }
    try {
      JsonNode node = objectMapper.readTree(body);
      if (node.hasNonNull("message")) return node.get("message").asText();
      if (node.hasNonNull("error")) return node.get("error").asText();
    } catch (Exception ignored) {
    }
    return body;
  }
}
