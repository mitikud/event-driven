package com.medical.orderservice.service;

import com.medical.orderservice.modrl.OutboxEvent;
import com.medical.orderservice.repo.OutboxRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;

@Slf4j
@Component
@RequiredArgsConstructor
public class OutboxPublisher {
    private final OutboxRepo outbox;
    private final KafkaTemplate<String, String> kafka;


    @Value("${app.topics.orders}")
    private String ordersTopic;


    @Scheduled(fixedDelayString = "${app.outbox.poll-ms:1000}")
    @Transactional
    public void publishBatch() {
        var batch = outbox.lockBatch().stream().limit(200).toList();
        for (OutboxEvent e : batch) {
            try {
                var rec = new ProducerRecord<String, String>(ordersTopic, e.getAggregateId(), e.getPayloadJson());
                rec.headers().add("event-id", e.getId().toString().getBytes(StandardCharsets.UTF_8));
                rec.headers().add("event-type", e.getEventType().getBytes(StandardCharsets.UTF_8));
                kafka.send(rec).get(); // sync for simplicity; tune to async + callbacks
                e.setStatus(OutboxEvent.Status.PUBLISHED);
                log.info("Published {} id={} key={}", e.getEventType(), e.getId(), e.getAggregateId());
            } catch (Exception ex) {
                log.error("Failed publishing outbox event {}: {}", e.getId(), ex.toString());
                e.setStatus(OutboxEvent.Status.FAILED);
            }
        }
    }
}
