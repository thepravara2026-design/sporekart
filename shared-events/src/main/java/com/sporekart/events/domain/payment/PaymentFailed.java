package com.sporekart.events.domain.payment;

import com.sporekart.events.model.DomainEvent;
import java.math.BigDecimal;

public class PaymentFailed extends DomainEvent {
    private final String paymentId;
    private final String orderId;
    private final BigDecimal amount;
    private final String failureReason;

    private PaymentFailed(Builder builder) {
        super(builder);
        this.paymentId = builder.paymentId;
        this.orderId = builder.orderId;
        this.amount = builder.amount;
        this.failureReason = builder.failureReason;
    }

    public String getPaymentId() { return paymentId; }
    public String getOrderId() { return orderId; }
    public BigDecimal getAmount() { return amount; }
    public String getFailureReason() { return failureReason; }

    public static Builder builder() { return new Builder(); }

    public static class Builder extends DomainEvent.Builder<Builder> {
        private String paymentId;
        private String orderId;
        private BigDecimal amount;
        private String failureReason;

        public Builder paymentId(String paymentId) { this.paymentId = paymentId; return this; }
        public Builder orderId(String orderId) { this.orderId = orderId; return this; }
        public Builder amount(BigDecimal amount) { this.amount = amount; return this; }
        public Builder failureReason(String failureReason) { this.failureReason = failureReason; return this; }

        public PaymentFailed build() {
            return new PaymentFailed(this);
        }
    }
}
