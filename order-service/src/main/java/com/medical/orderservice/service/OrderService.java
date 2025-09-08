package com.medical.orderservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.medical.orderservice.api.dto.CreateOrderRequest;
import com.medical.orderservice.events.OrderCreated;
import com.medical.orderservice.modrl.Order;
import com.medical.orderservice.modrl.OutboxEvent;
import com.medical.orderservice.repo.OrderRepo;
import com.medical.orderservice.repo.OutboxRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepo orders;
    private final OutboxRepo outbox;
    private final ObjectMapper om;


    @Transactional
    public Order create(CreateOrderRequest r) {
        var o = Order.builder()
                .customerId(r.customerId())
                .amount(r.amount())
                .status(Order.Status.CREATED)
                .createdAt(OffsetDateTime.now())
                .build();
        orders.save(o);


        var evt = new OrderCreated(o.getId(), o.getCustomerId(), o.getAmount(), OffsetDateTime.now());
        try {
            var payload = om.writeValueAsString(evt);
            var ob = OutboxEvent.builder()
                    .aggregateType("ORDER")
                    .aggregateId(o.getId().toString())
                    .eventType("OrderCreated")
                    .payloadJson(payload)
                    .createdAt(OffsetDateTime.now())
                    .status(OutboxEvent.Status.PENDING)
                    .build();
            outbox.save(ob);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return o;
    }
}
