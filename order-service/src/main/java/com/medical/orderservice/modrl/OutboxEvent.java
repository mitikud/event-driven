package com.medical.orderservice.modrl;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "outbox_events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OutboxEvent {
    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id; // eventId (dedupe key)


    private String aggregateType; // e.g., ORDER
    private String aggregateId; // e.g., order UUID
    private String eventType; // e.g., OrderCreated


    @Lob
    @Column(columnDefinition = "TEXT")
    private String payloadJson; // serialized event payload


    private OffsetDateTime createdAt;


    @Enumerated(EnumType.STRING)
    private Status status; // PENDING, PUBLISHED, FAILED


    public enum Status { PENDING, PUBLISHED, FAILED }
}
