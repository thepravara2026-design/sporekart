package com.sporekart.events.domain.order;

import com.sporekart.events.model.DomainEvent;
import java.math.BigDecimal;

public class OrderPaid extends DomainEvent {
    private final String orderId;
    private final BigDecimal amountPaid;

    private OrderPaid(Builder builder) {
        super(builder);
        this.orderId = builder.orderId;
        this.amountPaid = builder.amountPaid;
    }

    public String getOrderId() { return orderId; }
    public BigDecimal getAmountPaid() { return amountPaid; }

    public static Builder builder() { return new Builder(); }

    public static class Builder extends DomainEvent.Builder<Builder> {
        private String orderId;
        private BigDecimal amountPaid;

        public Builder orderId(String orderId) { this.orderId = orderId; return this; }
        public Builder amountPaid(BigDecimal amountPaid) { this.amountPaid = amountPaid; return this; }

        public OrderPaid build() {
            return new OrderPaid(this);
        }
    }
}
