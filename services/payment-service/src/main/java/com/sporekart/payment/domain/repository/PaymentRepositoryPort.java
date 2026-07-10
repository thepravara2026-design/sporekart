package com.sporekart.payment.domain.repository;

import com.sporekart.payment.domain.model.Payment;

import java.util.List;
import java.util.Optional;

public interface PaymentRepositoryPort {
    Payment save(Payment payment);

    Optional<Payment> findById(String id);

    Optional<Payment> findByOrderId(String orderId);

    List<Payment> findAll();
}
