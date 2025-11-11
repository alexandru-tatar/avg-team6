package com.hka.oms.wms;

import org.springframework.http.HttpStatus;

public class WmsException extends RuntimeException {
  private final HttpStatus status;

  public WmsException(HttpStatus status, String message) {
    super(message);
    this.status = status;
  }

  public HttpStatus getStatus() {
    return status;
  }
}
