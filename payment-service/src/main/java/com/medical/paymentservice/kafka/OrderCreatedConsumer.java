package com.medical.paymentservice.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.medical.paymentservice.model.Payment;
import com.medical.paymentservice.model.ProcessedEvent;
import com.medical.paymentservice.repo.PaymentRepo;
import com.medical.paymentservice.repo.ProcessedEventRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderCreatedConsumer {
    private final ProcessedEventRepo processed;
    private final PaymentRepo payments;
    private final ObjectMapper om;


    record OrderCreated(UUID orderId, String customerId, BigDecimal amount, OffsetDateTime occurredAt) {}


    @KafkaListener(topics = "orders.v1", groupId = "payment-svc")
    @Transactional
    public void onMessage(ConsumerRecord<String, String> rec,
                          @Header(name = "event-id", required = false) byte[] eventIdBytes,
                          @Header(name = "event-type", required = false) byte[] eventTypeBytes) throws Exception {
        String eventId = eventIdBytes == null ? null : new String(eventIdBytes);
        String eventType = eventTypeBytes == null ? null : new String(eventTypeBytes);


        if (eventId == null) {
            log.warn("Missing event-id header; processing with best-effort idempotency using key={}", rec.key());
        } else if (processed.findByEventId(eventId).isPresent()) {
            log.info("Skip duplicate event {}", eventId);
            return; // idempotent
        }


        var payload = om.readValue(rec.value(), OrderCreated.class);


// Simulate payment authorization
        var p = Payment.builder()
                .orderId(payload.orderId())
                .customerId(payload.customerId())
                .amount(payload.amount())
                .authorizedAt(OffsetDateTime.now())
                .build();
        payments.save(p);


        if (eventId != null) {
            processed.save(ProcessedEvent.builder().eventId(eventId).build());
        }


        log.info("Authorized payment for order {} amount {}", payload.orderId(), payload.amount());
    }
}
