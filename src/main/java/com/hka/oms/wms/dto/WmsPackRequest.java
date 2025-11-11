package com.hka.oms.wms.dto;

import java.math.BigDecimal;

public record WmsPackRequest(String orderId, BigDecimal weightKg, String dimensions) {}
