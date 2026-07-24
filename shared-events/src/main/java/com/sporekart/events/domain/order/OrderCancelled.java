package com.sporekart.events.domain.order;

import com.sporekart.events.model.DomainEvent;

public class OrderCancelled extends DomainEvent {
    private final String orderId;
    private final String reason;

    private OrderCancelled(Builder builder) {
        super(builder);
        this.orderId = builder.orderId;
        this.reason = builder.reason;
    }

    public String getOrderId() { return orderId; }
    public String getReason() { return reason; }

    public static Builder builder() { return new Builder(); }

    public static class Builder extends DomainEvent.Builder<Builder> {
        private String orderId;
        private String reason;

        public Builder orderId(String orderId) { this.orderId = orderId; return this; }
        public Builder reason(String reason) { this.reason = reason; return this; }

        public OrderCancelled build() {
            return new OrderCancelled(this);
        }
    }
}
