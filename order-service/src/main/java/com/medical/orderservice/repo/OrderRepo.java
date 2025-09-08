package com.medical.orderservice.repo;

import com.medical.orderservice.modrl.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderRepo extends JpaRepository<Order, UUID> {}
