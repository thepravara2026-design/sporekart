package com.sporekart.payment.domain.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public class Payment {
    private final String id;
    private final String orderId;
    private final BigDecimal amount;
    private final PaymentStatus status;
    private final String paymentMethod;
    private final Instant createdAt;
    private final Instant updatedAt;

    public Payment(String id, String orderId, BigDecimal amount, PaymentStatus status, String paymentMethod,
            Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.orderId = orderId;
        this.amount = amount;
        this.status = status;
        this.paymentMethod = paymentMethod;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Payment initiate(String orderId, BigDecimal amount, String paymentMethod) {
        return new Payment(UUID.randomUUID().toString(), orderId, amount, PaymentStatus.INITIATED, paymentMethod,
                Instant.now(), Instant.now());
    }

    public Payment capture() {
        return new Payment(id, orderId, amount, PaymentStatus.CAPTURED, paymentMethod, createdAt, Instant.now());
    }

    public Payment fail() {
        return new Payment(id, orderId, amount, PaymentStatus.FAILED, paymentMethod, createdAt, Instant.now());
    }

    public String getId() {
        return id;
    }

    public String getOrderId() {
        return orderId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
