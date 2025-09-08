package com.medical.orderservice.api.dto;

import java.math.BigDecimal;

public record CreateOrderRequest(String customerId, BigDecimal amount) {}
