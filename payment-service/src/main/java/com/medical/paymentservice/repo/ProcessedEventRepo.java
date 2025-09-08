package com.medical.paymentservice.repo;

import com.medical.paymentservice.model.ProcessedEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ProcessedEventRepo extends JpaRepository<ProcessedEvent, UUID> {
    Optional<ProcessedEvent> findByEventId(String eventId);
}
