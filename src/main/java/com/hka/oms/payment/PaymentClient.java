package com.hka.oms.payment;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hka.oms.payment.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import java.io.IOException;

@Component
public class PaymentClient {

  private static final Logger log = LoggerFactory.getLogger(PaymentClient.class);
  private static final String IDEMPOTENCY_HEADER = "Idempotency-Key";

  private final RestClient restClient;
  private final PaymentProperties properties;
  private final ObjectMapper objectMapper;

  public PaymentClient(RestClient.Builder builder, PaymentProperties properties, ObjectMapper objectMapper) {
    this.restClient = builder
        .baseUrl(properties.baseUrl())
        .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
        .build();
    this.properties = properties;
    this.objectMapper = objectMapper;
  }

  public PaymentResponse authorize(PaymentAuthorizeRequest request, String idempotencyKey) {
    log.info("Authorizing payment for order {}", request.orderId());
    return post("/payments/authorize", request, idempotencyKey);
  }

  public PaymentResponse capture(PaymentCaptureRequest request) {
    log.info("Capturing payment for order {}", request.orderId());
    return post("/payments/capture", request, null);
  }

  public PaymentResponse refund(PaymentRefundRequest request) {
    log.info("Refunding payment for order {}", request.orderId());
    return post("/payments/refund", request, null);
  }

  private <T> PaymentResponse post(String path, T body, String idempotencyKey) {
    try {
      RestClient.RequestBodySpec requestSpec = restClient.post().uri(path).body(body);
      if (StringUtils.hasText(idempotencyKey)) {
        requestSpec = requestSpec.header(IDEMPOTENCY_HEADER, idempotencyKey);
      }
      return requestSpec.retrieve().body(PaymentResponse.class);
    } catch (RestClientResponseException ex) {
      String message = extractMessage(ex.getResponseBodyAsString());
      HttpStatus status = HttpStatus.resolve(ex.getStatusCode().value());
      throw new PaymentException(status == null ? HttpStatus.BAD_GATEWAY : status,
          message == null || message.isBlank() ? ex.getStatusText() : message);
    }
  }

  private String extractMessage(String body) {
    if (!StringUtils.hasText(body)) {
      return null;
    }
    try {
      JsonNode node = objectMapper.readTree(body);
      if (node.hasNonNull("message")) {
        return node.get("message").asText();
      }
      if (node.hasNonNull("error")) {
        return node.get("error").asText();
      }
    } catch (IOException ignored) {
    }
    return body;
  }

  public PaymentProperties properties() {
    return properties;
  }
}
