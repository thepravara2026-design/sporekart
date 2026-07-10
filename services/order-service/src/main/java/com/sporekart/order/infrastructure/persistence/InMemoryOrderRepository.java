package com.sporekart.order.infrastructure.persistence;

import com.sporekart.order.domain.model.Order;
import com.sporekart.order.domain.repository.OrderRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryOrderRepository implements OrderRepositoryPort {
    private final Map<String, Order> ordersById = new ConcurrentHashMap<>();

    @Override
    public Order save(Order order) {
        ordersById.put(order.getId(), order);
        return order;
    }

    @Override
    public Optional<Order> findById(String id) {
        return Optional.ofNullable(ordersById.get(id));
    }

    @Override
    public List<Order> findByCustomerId(String customerId) {
        return ordersById.values().stream().filter(order -> customerId.equals(order.getCustomerId())).toList();
    }

    @Override
    public List<Order> findAll() {
        return new ArrayList<>(ordersById.values());
    }
}
