package com.sporekart.payment.application.service;

import com.sporekart.payment.domain.model.Payment;
import com.sporekart.payment.domain.repository.PaymentRepositoryPort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class PaymentService {
    private final PaymentRepositoryPort repositoryPort;

    public PaymentService(PaymentRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    public Payment initiate(String orderId, BigDecimal amount, String paymentMethod) {
        Payment payment = Payment.initiate(orderId, amount, paymentMethod);
        return repositoryPort.save(payment);
    }

    public Optional<Payment> getByOrderId(String orderId) {
        return repositoryPort.findByOrderId(orderId);
    }

    public Payment capture(String paymentId) {
        Payment payment = repositoryPort.findById(paymentId)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found"));
        return repositoryPort.save(payment.capture());
    }

    public Payment fail(String paymentId) {
        Payment payment = repositoryPort.findById(paymentId)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found"));
        return repositoryPort.save(payment.fail());
    }
}
