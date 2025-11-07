package com.hka.oms.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.hka.oms.service.InventoryUnavailableException;

import java.time.Instant;
import java.util.Map;

@RestControllerAdvice
public class InventoryExceptionHandler {

  private static final Logger log = LoggerFactory.getLogger(InventoryExceptionHandler.class);

  @ExceptionHandler(InventoryUnavailableException.class)
  public ResponseEntity<Map<String, Object>> handleInventoryUnavailable(InventoryUnavailableException ex) {
    log.warn("Inventory unavailable: {}", ex.getMessage());
    Map<String, Object> body = Map.of(
        "timestamp", Instant.now().toString(),
        "status", 409,
        "error", "Conflict",
        "message", ex.getMessage()
    );
    return ResponseEntity.status(409).body(body);
  }
}
