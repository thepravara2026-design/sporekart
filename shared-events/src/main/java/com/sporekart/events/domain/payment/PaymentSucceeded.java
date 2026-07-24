package com.sporekart.events.domain.payment;

import com.sporekart.events.model.DomainEvent;
import java.math.BigDecimal;

public class PaymentSucceeded extends DomainEvent {
    private final String paymentId;
    private final String orderId;
    private final BigDecimal amount;

    private PaymentSucceeded(Builder builder) {
        super(builder);
        this.paymentId = builder.paymentId;
        this.orderId = builder.orderId;
        this.amount = builder.amount;
    }

    public String getPaymentId() { return paymentId; }
    public String getOrderId() { return orderId; }
    public BigDecimal getAmount() { return amount; }

    public static Builder builder() { return new Builder(); }

    public static class Builder extends DomainEvent.Builder<Builder> {
        private String paymentId;
        private String orderId;
        private BigDecimal amount;

        public Builder paymentId(String paymentId) { this.paymentId = paymentId; return this; }
        public Builder orderId(String orderId) { this.orderId = orderId; return this; }
        public Builder amount(BigDecimal amount) { this.amount = amount; return this; }

        public PaymentSucceeded build() {
            return new PaymentSucceeded(this);
        }
    }
}
