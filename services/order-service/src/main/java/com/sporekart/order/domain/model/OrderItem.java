package com.sporekart.order.domain.model;

public class OrderItem {
    private final String productId;
    private final int quantity;
    private final java.math.BigDecimal unitPrice;

    public OrderItem(String productId, int quantity, java.math.BigDecimal unitPrice) {
        this.productId = productId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public String getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public java.math.BigDecimal getUnitPrice() {
        return unitPrice;
    }
}
