package com.medical.orderservice.events;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record OrderCreated(UUID orderId, String customerId, BigDecimal amount, OffsetDateTime occurredAt) {}
