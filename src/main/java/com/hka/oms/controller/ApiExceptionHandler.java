package com.hka.oms.controller;

import com.hka.oms.payment.PaymentException;
import com.hka.oms.service.InventoryUnavailableException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler {

  private static final Logger log = LoggerFactory.getLogger(ApiExceptionHandler.class);

  @ExceptionHandler(InventoryUnavailableException.class)
  public ResponseEntity<Map<String, Object>> handleInventoryUnavailable(InventoryUnavailableException ex) {
    log.warn("Inventory unavailable: {}", ex.getMessage());
    return build(HttpStatus.CONFLICT, ex.getMessage());
  }

  @ExceptionHandler(PaymentException.class)
  public ResponseEntity<Map<String, Object>> handlePaymentException(PaymentException ex) {
    HttpStatus status = ex.getStatus() == null ? HttpStatus.BAD_GATEWAY : ex.getStatus();
    log.warn("Payment error ({}): {}", status.value(), ex.getMessage());
    return build(status, ex.getMessage());
  }

  private ResponseEntity<Map<String, Object>> build(HttpStatus status, String message) {
    Map<String, Object> body = Map.of(
        "timestamp", Instant.now().toString(),
        "status", status.value(),
        "error", status.getReasonPhrase(),
        "message", message
    );
    return ResponseEntity.status(status).body(body);
  }
}