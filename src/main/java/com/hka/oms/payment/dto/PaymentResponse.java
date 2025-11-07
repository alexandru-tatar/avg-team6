package com.hka.oms.payment.dto;

import java.math.BigDecimal;
import java.time.Instant;

public record PaymentResponse(
    String orderId,
    BigDecimal amount,
    String currency,
    String method,
    PaymentStatus status,
    Instant createdAt,
    Instant updatedAt
) {}
