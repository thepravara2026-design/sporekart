package com.sporekart.events.domain.cart;

import com.sporekart.events.model.DomainEvent;
import java.math.BigDecimal;

public class CartCheckedOut extends DomainEvent {
    private final String cartId;
    private final String customerId;
    private final BigDecimal total;

    private CartCheckedOut(Builder builder) {
        super(builder);
        this.cartId = builder.cartId;
        this.customerId = builder.customerId;
        this.total = builder.total;
    }

    public String getCartId() { return cartId; }
    public String getCustomerId() { return customerId; }
    public BigDecimal getTotal() { return total; }

    public static Builder builder() { return new Builder(); }

    public static class Builder extends DomainEvent.Builder<Builder> {
        private String cartId;
        private String customerId;
        private BigDecimal total;

        public Builder cartId(String cartId) { this.cartId = cartId; return this; }
        public Builder customerId(String customerId) { this.customerId = customerId; return this; }
        public Builder total(BigDecimal total) { this.total = total; return this; }

        public CartCheckedOut build() {
            return new CartCheckedOut(this);
        }
    }
}
