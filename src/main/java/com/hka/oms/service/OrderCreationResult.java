package com.hka.oms.service;

import com.hka.oms.domain.Order;

public record OrderCreationResult(Order order, String reservationMessage) {}
