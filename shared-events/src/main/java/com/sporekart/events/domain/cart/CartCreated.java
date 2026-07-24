package com.sporekart.events.domain.cart;

import com.sporekart.events.model.DomainEvent;

public class CartCreated extends DomainEvent {
    private final String cartId;
    private final String customerId;

    private CartCreated(Builder builder) {
        super(builder);
        this.cartId = builder.cartId;
        this.customerId = builder.customerId;
    }

    public String getCartId() { return cartId; }
    public String getCustomerId() { return customerId; }

    public static Builder builder() { return new Builder(); }

    public static class Builder extends DomainEvent.Builder<Builder> {
        private String cartId;
        private String customerId;

        public Builder cartId(String cartId) { this.cartId = cartId; return this; }
        public Builder customerId(String customerId) { this.customerId = customerId; return this; }

        public CartCreated build() {
            return new CartCreated(this);
        }
    }
}
