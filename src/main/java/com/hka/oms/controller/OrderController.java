package com.hka.oms.controller;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.hka.oms.domain.Order;
import com.hka.oms.service.OrderCreationResult;
import com.hka.oms.service.OrderService;

import java.net.URI;

@RestController
@RequestMapping("/orders")
public class OrderController {

  private final OrderService service;

  public OrderController(OrderService service) {
    this.service = service;
  }

  @PostMapping
  public ResponseEntity<OrderCreationResult> create(@RequestBody Order order) {
    OrderCreationResult result = service.create(order);

    URI location = URI.create("/orders/" + result.order().getOrderId());

    return ResponseEntity
        .created(location)
        .body(result);
  }

  @GetMapping("/{orderId}")
  public ResponseEntity<Order> get(@PathVariable String orderId) {
    Order order = service.get(orderId);
    return ResponseEntity.ok(order);
  }

  @GetMapping
  public ResponseEntity<java.util.List<Order>> list() {
    return ResponseEntity.ok(service.list());
  }
}
