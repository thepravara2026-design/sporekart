package com.sporekart.order.interfaces.rest;

import com.sporekart.order.application.service.OrderService;
import com.sporekart.order.domain.model.Order;
import com.sporekart.order.domain.model.OrderItem;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<Order> create(@RequestBody CreateOrderRequest request) {
        Order order = orderService.create(request.customerId(), request.amount(), request.items());
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

    @GetMapping
    public ResponseEntity<List<Order>> history(@RequestParam String customerId) {
        return ResponseEntity.ok(orderService.getHistory(customerId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getById(@PathVariable String id) {
        return orderService.getById(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<Order> cancel(@PathVariable String id) {
        return ResponseEntity.ok(orderService.cancel(id));
    }

    public record CreateOrderRequest(String customerId, BigDecimal amount, List<OrderItem> items) {
    }
}
