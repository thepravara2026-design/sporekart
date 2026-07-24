package com.sporekart.events.domain.marketing;

import com.sporekart.events.model.DomainEvent;
import java.math.BigDecimal;

public class CouponApplied extends DomainEvent {
    private final String couponId;
    private final String orderId;
    private final BigDecimal discount;

    private CouponApplied(Builder builder) {
        super(builder);
        this.couponId = builder.couponId;
        this.orderId = builder.orderId;
        this.discount = builder.discount;
    }

    public String getCouponId() { return couponId; }
    public String getOrderId() { return orderId; }
    public BigDecimal getDiscount() { return discount; }

    public static Builder builder() { return new Builder(); }

    public static class Builder extends DomainEvent.Builder<Builder> {
        private String couponId;
        private String orderId;
        private BigDecimal discount;

        public Builder couponId(String couponId) { this.couponId = couponId; return this; }
        public Builder orderId(String orderId) { this.orderId = orderId; return this; }
        public Builder discount(BigDecimal discount) { this.discount = discount; return this; }

        public CouponApplied build() {
            return new CouponApplied(this);
        }
    }
}
