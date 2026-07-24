package com.sporekart.events.domain.inventory;

import com.sporekart.events.model.DomainEvent;

public class InventoryReserved extends DomainEvent {
    private final String productId;
    private final int quantity;

    private InventoryReserved(Builder builder) {
        super(builder);
        this.productId = builder.productId;
        this.quantity = builder.quantity;
    }

    public String getProductId() { return productId; }
    public int getQuantity() { return quantity; }

    public static Builder builder() { return new Builder(); }

    public static class Builder extends DomainEvent.Builder<Builder> {
        private String productId;
        private int quantity;

        public Builder productId(String productId) { this.productId = productId; return this; }
        public Builder quantity(int quantity) { this.quantity = quantity; return this; }

        public InventoryReserved build() {
            return new InventoryReserved(this);
        }
    }
}
