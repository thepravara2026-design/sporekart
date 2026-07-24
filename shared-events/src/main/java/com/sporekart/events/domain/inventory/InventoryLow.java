package com.sporekart.events.domain.inventory;

import com.sporekart.events.model.DomainEvent;

public class InventoryLow extends DomainEvent {
    private final String productId;
    private final int currentStock;
    private final int reorderPoint;

    private InventoryLow(Builder builder) {
        super(builder);
        this.productId = builder.productId;
        this.currentStock = builder.currentStock;
        this.reorderPoint = builder.reorderPoint;
    }

    public String getProductId() { return productId; }
    public int getCurrentStock() { return currentStock; }
    public int getReorderPoint() { return reorderPoint; }

    public static Builder builder() { return new Builder(); }

    public static class Builder extends DomainEvent.Builder<Builder> {
        private String productId;
        private int currentStock;
        private int reorderPoint;

        public Builder productId(String productId) { this.productId = productId; return this; }
        public Builder currentStock(int currentStock) { this.currentStock = currentStock; return this; }
        public Builder reorderPoint(int reorderPoint) { this.reorderPoint = reorderPoint; return this; }

        public InventoryLow build() {
            return new InventoryLow(this);
        }
    }
}
