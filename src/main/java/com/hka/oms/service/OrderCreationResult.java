package com.hka.oms.service;

import com.hka.oms.domain.Order;
import com.hka.oms.payment.dto.PaymentResponse;

public record OrderCreationResult(Order order, String reservationMessage, PaymentResponse payment) {}
