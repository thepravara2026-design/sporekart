package com.sporekart.order.infrastructure.persistence;

import com.sporekart.order.domain.model.Order;
import com.sporekart.order.domain.repository.OrderRepositoryPort;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Primary
@Repository
@Transactional
public class JpaOrderRepositoryAdapter implements OrderRepositoryPort {

    private final OrderJpaRepository jpaRepository;

    public JpaOrderRepositoryAdapter(OrderJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Order save(Order order) {
        return jpaRepository.save(OrderEntity.fromDomain(order)).toDomain();
    }

    @Override
    public Optional<Order> findById(String id) {
        return jpaRepository.findById(id).map(OrderEntity::toDomain);
    }

    @Override
    public List<Order> findByCustomerId(String customerId) {
        return jpaRepository.findByCustomerId(customerId).stream()
                .map(OrderEntity::toDomain).toList();
    }

    @Override
    public List<Order> findAll() {
        return jpaRepository.findAll().stream().map(OrderEntity::toDomain).toList();
    }
}
