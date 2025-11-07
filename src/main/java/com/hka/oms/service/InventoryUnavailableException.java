package com.hka.oms.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class InventoryUnavailableException extends RuntimeException {
  public InventoryUnavailableException(String message) {
    super(message);
  }
}
