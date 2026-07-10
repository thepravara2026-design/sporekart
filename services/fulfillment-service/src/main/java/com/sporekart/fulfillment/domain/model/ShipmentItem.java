package com.sporekart.fulfillment.domain.model;

public class ShipmentItem {
    private final String sku;
    private final int quantity;

    public ShipmentItem(String sku, int quantity) {
        this.sku = sku;
        this.quantity = quantity;
    }

    public String getSku() {
        return sku;
    }

    public int getQuantity() {
        return quantity;
    }
}
