package com.sporekart.inventory.domain.model;

public class InventoryItem {
    private final String productId;
    private final int stockQuantity;
    private final int reservedQuantity;
    private final int availableQuantity;

    public InventoryItem(String productId, int stockQuantity, int reservedQuantity, int availableQuantity) {
        this.productId = productId;
        this.stockQuantity = stockQuantity;
        this.reservedQuantity = reservedQuantity;
        this.availableQuantity = availableQuantity;
    }

    public String getId() {
        return productId;
    }

    public String getProductId() {
        return productId;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public int getReservedQuantity() {
        return reservedQuantity;
    }

    public int getAvailableQuantity() {
        return availableQuantity;
    }
}
