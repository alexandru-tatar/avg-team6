package com.hka.oms.payment.dto;

import java.math.BigDecimal;

public record PaymentCaptureRequest(
    String orderId,
    BigDecimal amount
) {}
