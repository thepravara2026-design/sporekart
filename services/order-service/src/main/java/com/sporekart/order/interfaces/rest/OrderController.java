package com.sporekart.order.interfaces.rest;

import com.sporekart.order.application.service.OrderService;
import com.sporekart.order.domain.model.Order;
import com.sporekart.order.domain.model.OrderItem;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    private String requirePrincipalName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()
                || authentication.getName() == null || "anonymousUser".equals(authentication.getName())) {
            throw new AccessDeniedException("Authenticated customer principal is required.");
        }
        return authentication.getName();
    }

    private void assertOwnsOrder(Order order) {
        if (order == null || !requirePrincipalName().equals(order.getCustomerId())) {
            throw new AccessDeniedException("You are not authorized to access this order.");
        }
    }

    @PostMapping
    public ResponseEntity<Order> create(@RequestBody CreateOrderRequest request) {
        String principal = requirePrincipalName();
        if (!principal.equals(request.customerId())) {
            throw new AccessDeniedException("Orders can only be created for the authenticated customer.");
        }
        Order order = orderService.create(request.customerId(), request.amount(), request.items());
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

    @GetMapping
    public ResponseEntity<List<Order>> history(@RequestParam String customerId) {
        if (!requirePrincipalName().equals(customerId)) {
            throw new AccessDeniedException("You can only view your own order history.");
        }
        return ResponseEntity.ok(orderService.getHistory(customerId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getById(@PathVariable String id) {
        Optional<Order> order = orderService.getById(id);
        order.ifPresent(this::assertOwnsOrder);
        return order.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<Order> cancel(@PathVariable String id) {
        return ResponseEntity.ok(orderService.cancel(id));
    }

    public record CreateOrderRequest(String customerId, BigDecimal amount, List<OrderItem> items) {
    }
}
