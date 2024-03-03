package com.ecommerce.shopapp.controllers;


import com.ecommerce.shopapp.dtos.OrderWithDetailsDTO;
import com.ecommerce.shopapp.models.Order;
import com.ecommerce.shopapp.responses.order.OrderResponse;
import com.ecommerce.shopapp.services.order.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("${api.prefix}/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<?> createOrder(
            @Valid @RequestBody OrderWithDetailsDTO orderWithDetailsDTO
    ) {
        try {
            Order orderResponse = orderService.createOrder(orderWithDetailsDTO);
            return ResponseEntity.ok(orderResponse);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("users/{user_id}")
    public ResponseEntity<?> getOrders(@PathVariable("user_id") Long userId) {
        try {
            List<OrderResponse> orders = orderService.findByUserId(userId);
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
