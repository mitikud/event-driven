package com.medical.paymentservice.repo;

import com.medical.paymentservice.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PaymentRepo extends JpaRepository<Payment, UUID> {}
