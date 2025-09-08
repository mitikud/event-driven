package com.medical.paymentservice.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Table(name = "processed_events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProcessedEvent {
    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;


    @Column(unique = true, nullable = false)
    private String eventId; // from Kafka header
}
