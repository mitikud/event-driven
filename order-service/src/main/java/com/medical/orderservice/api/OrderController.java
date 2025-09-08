package com.medical.orderservice.api;

import com.medical.orderservice.api.dto.CreateOrderRequest;
import com.medical.orderservice.modrl.Order;
import com.medical.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService service;


    @PostMapping
    public ResponseEntity<Order> create(@RequestBody CreateOrderRequest req) {
        var o = service.create(req);
        return ResponseEntity.ok(o);
    }
}