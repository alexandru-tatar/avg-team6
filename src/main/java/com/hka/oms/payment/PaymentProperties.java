package com.hka.oms.payment;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "payment")
public record PaymentProperties(String baseUrl, String currency, String method) {
  public PaymentProperties {
    if (baseUrl == null || baseUrl.isBlank()) {
      throw new IllegalArgumentException("payment.base-url is required");
    }
  }
}
