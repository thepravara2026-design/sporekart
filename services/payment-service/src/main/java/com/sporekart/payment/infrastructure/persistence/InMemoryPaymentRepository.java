package com.sporekart.payment.infrastructure.persistence;

import com.sporekart.payment.domain.model.Payment;
import com.sporekart.payment.domain.repository.PaymentRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryPaymentRepository implements PaymentRepositoryPort {
    private final Map<String, Payment> paymentsById = new ConcurrentHashMap<>();
    private final Map<String, String> orderIndex = new ConcurrentHashMap<>();

    @Override
    public Payment save(Payment payment) {
        paymentsById.put(payment.getId(), payment);
        orderIndex.put(payment.getOrderId(), payment.getId());
        return payment;
    }

    @Override
    public Optional<Payment> findById(String id) {
        return Optional.ofNullable(paymentsById.get(id));
    }

    @Override
    public Optional<Payment> findByOrderId(String orderId) {
        String paymentId = orderIndex.get(orderId);
        if (paymentId == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(paymentsById.get(paymentId));
    }

    @Override
    public List<Payment> findAll() {
        return new ArrayList<>(paymentsById.values());
    }
}
