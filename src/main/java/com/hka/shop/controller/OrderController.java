package com.hka.shop.controller;

import com.hka.shop.domain.Order;
import com.hka.shop.domain.OrderStatus;
import com.hka.shop.service.OrderService;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {
  private final OrderService service;
  public OrderController(OrderService service){ this.service = service; }

  @PostMapping
  public ResponseEntity<Order> create(@RequestBody Order order){
    return ResponseEntity.status(HttpStatus.CREATED).body(service.create(order));
  }

  @GetMapping("/{orderId}")
  public Order get(@PathVariable String orderId){ return service.get(orderId); }

  @GetMapping
  public java.util.List<Order> list(){ return service.list(); }

  @PostMapping("/{orderId}/cancel")
  public Order cancel(@PathVariable String orderId){ return service.cancel(orderId); }

  @PostMapping("/{orderId}/status/{status}")
  public Order update(@PathVariable String orderId, @PathVariable String status){
    return service.updateStatus(orderId, OrderStatus.valueOf(status.toUpperCase()));
  }
}
