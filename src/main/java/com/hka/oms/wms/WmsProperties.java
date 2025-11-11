package com.hka.oms.wms;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "wms")
public record WmsProperties(String baseUrl) {
  public WmsProperties {
    if (baseUrl == null || baseUrl.isBlank()) {
      throw new IllegalArgumentException("wms.base-url is required");
    }
  }
}
