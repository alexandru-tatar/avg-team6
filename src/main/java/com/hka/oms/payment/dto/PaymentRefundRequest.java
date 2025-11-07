package com.hka.oms.payment.dto;

import java.math.BigDecimal;

public record PaymentRefundRequest(
    String orderId,
    BigDecimal amount,
    String reason
) {}
