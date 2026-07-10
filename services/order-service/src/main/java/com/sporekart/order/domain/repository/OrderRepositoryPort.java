package com.sporekart.order.domain.repository;

import com.sporekart.order.domain.model.Order;

import java.util.List;
import java.util.Optional;

public interface OrderRepositoryPort {
    Order save(Order order);

    Optional<Order> findById(String id);

    List<Order> findByCustomerId(String customerId);

    List<Order> findAll();
}
