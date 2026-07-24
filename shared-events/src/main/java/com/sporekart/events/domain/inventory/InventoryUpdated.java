package com.sporekart.events.domain.inventory;

import com.sporekart.events.model.DomainEvent;

public class InventoryUpdated extends DomainEvent {
    private final String productId;
    private final int newStock;
    private final int delta;

    private InventoryUpdated(Builder builder) {
        super(builder);
        this.productId = builder.productId;
        this.newStock = builder.newStock;
        this.delta = builder.delta;
    }

    public String getProductId() { return productId; }
    public int getNewStock() { return newStock; }
    public int getDelta() { return delta; }

    public static Builder builder() { return new Builder(); }

    public static class Builder extends DomainEvent.Builder<Builder> {
        private String productId;
        private int newStock;
        private int delta;

        public Builder productId(String productId) { this.productId = productId; return this; }
        public Builder newStock(int newStock) { this.newStock = newStock; return this; }
        public Builder delta(int delta) { this.delta = delta; return this; }

        public InventoryUpdated build() {
            return new InventoryUpdated(this);
        }
    }
}
