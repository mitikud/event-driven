package com.medical.orderservice.modrl;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {
    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;


    private String customerId;


    @Column(precision = 12, scale = 2)
    private BigDecimal amount;


    @Enumerated(EnumType.STRING)
    private Status status;


    private OffsetDateTime createdAt;


    public enum Status { CREATED, FAILED }
}
