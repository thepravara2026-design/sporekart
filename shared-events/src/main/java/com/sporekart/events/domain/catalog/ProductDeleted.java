package com.sporekart.events.domain.catalog;

import com.sporekart.events.model.DomainEvent;

public class ProductDeleted extends DomainEvent {
    private final String productId;

    private ProductDeleted(Builder builder) {
        super(builder);
        this.productId = builder.productId;
    }

    public String getProductId() { return productId; }

    public static Builder builder() { return new Builder(); }

    public static class Builder extends DomainEvent.Builder<Builder> {
        private String productId;

        public Builder productId(String productId) { this.productId = productId; return this; }

        public ProductDeleted build() {
            return new ProductDeleted(this);
        }
    }
}
