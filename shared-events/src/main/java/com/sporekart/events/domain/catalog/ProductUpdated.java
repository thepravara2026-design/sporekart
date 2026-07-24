package com.sporekart.events.domain.catalog;

import com.sporekart.events.model.DomainEvent;

public class ProductUpdated extends DomainEvent {
    private final String productId;
    private final String updatedFields;

    private ProductUpdated(Builder builder) {
        super(builder);
        this.productId = builder.productId;
        this.updatedFields = builder.updatedFields;
    }

    public String getProductId() { return productId; }
    public String getUpdatedFields() { return updatedFields; }

    public static Builder builder() { return new Builder(); }

    public static class Builder extends DomainEvent.Builder<Builder> {
        private String productId;
        private String updatedFields;

        public Builder productId(String productId) { this.productId = productId; return this; }
        public Builder updatedFields(String updatedFields) { this.updatedFields = updatedFields; return this; }

        public ProductUpdated build() {
            return new ProductUpdated(this);
        }
    }
}
