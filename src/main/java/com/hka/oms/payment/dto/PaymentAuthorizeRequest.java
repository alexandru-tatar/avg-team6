package com.hka.oms.payment.dto;

import java.math.BigDecimal;

public record PaymentAuthorizeRequest(
    String orderId,
    BigDecimal amount,
    String currency,
    String method
) {}
