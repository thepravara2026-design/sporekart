package com.sporekart.order.application.service;

import com.sporekart.order.domain.model.Order;
import com.sporekart.order.domain.model.OrderItem;
import com.sporekart.order.domain.model.OrderStatus;
import com.sporekart.order.domain.repository.OrderRepositoryPort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    private final OrderRepositoryPort repositoryPort;

    public OrderService(OrderRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    public Order create(String customerId, BigDecimal amount, List<OrderItem> items) {
        Order order = Order.create(customerId, amount, items);
        return repositoryPort.save(order);
    }

    public Optional<Order> getById(String id) {
        return repositoryPort.findById(id);
    }

    public List<Order> getHistory(String customerId) {
        return repositoryPort.findByCustomerId(customerId);
    }

    public Order cancel(String id) {
        Order order = repositoryPort.findById(id).orElseThrow(() -> new IllegalArgumentException("Order not found"));
        return repositoryPort.save(order.cancel());
    }

    public Order confirm(String id) {
        Order order = repositoryPort.findById(id).orElseThrow(() -> new IllegalArgumentException("Order not found"));
        return repositoryPort.save(order.confirm());
    }
}
