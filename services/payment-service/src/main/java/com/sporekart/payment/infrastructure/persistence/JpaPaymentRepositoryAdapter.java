package com.sporekart.payment.infrastructure.persistence;

import com.sporekart.payment.domain.model.Payment;
import com.sporekart.payment.domain.repository.PaymentRepositoryPort;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Primary
@Repository
@Transactional
public class JpaPaymentRepositoryAdapter implements PaymentRepositoryPort {

    private final PaymentJpaRepository jpaRepository;

    public JpaPaymentRepositoryAdapter(PaymentJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Payment save(Payment payment) {
        return jpaRepository.save(PaymentEntity.fromDomain(payment)).toDomain();
    }

    @Override
    public Optional<Payment> findById(String id) {
        return jpaRepository.findById(id).map(PaymentEntity::toDomain);
    }

    @Override
    public Optional<Payment> findByOrderId(String orderId) {
        return jpaRepository.findByOrderId(orderId).map(PaymentEntity::toDomain);
    }

    @Override
    public List<Payment> findAll() {
        return jpaRepository.findAll().stream().map(PaymentEntity::toDomain).toList();
    }
}
