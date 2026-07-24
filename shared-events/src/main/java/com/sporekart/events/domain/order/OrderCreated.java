package com.sporekart.events.domain.order;

import com.sporekart.events.model.DomainEvent;
import java.math.BigDecimal;

public class OrderCreated extends DomainEvent {
    private final String orderId;
    private final String customerId;
    private final BigDecimal totalAmount;

    private OrderCreated(Builder builder) {
        super(builder);
        this.orderId = builder.orderId;
        this.customerId = builder.customerId;
        this.totalAmount = builder.totalAmount;
    }

    public String getOrderId() { return orderId; }
    public String getCustomerId() { return customerId; }
    public BigDecimal getTotalAmount() { return totalAmount; }

    public static Builder builder() { return new Builder(); }

    public static class Builder extends DomainEvent.Builder<Builder> {
        private String orderId;
        private String customerId;
        private BigDecimal totalAmount;

        public Builder orderId(String orderId) { this.orderId = orderId; return this; }
        public Builder customerId(String customerId) { this.customerId = customerId; return this; }
        public Builder totalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; return this; }

        public OrderCreated build() {
            return new OrderCreated(this);
        }
    }
}
