package com.sporekart.customer.copilot.domain;

public record ShoppingCartItem(
    String productId,
    String productName,
    int quantity,
    double unitPrice,
    double totalPrice
) {
    public ShoppingCartItem {
        if (productId == null || productId.isBlank()) {
            throw new IllegalArgumentException("productId must not be blank");
        }
        if (productName == null || productName.isBlank()) {
            productName = "Unknown Product";
        }
        if (quantity < 0) {
            quantity = 0;
        }
        if (unitPrice < 0) {
            unitPrice = 0.0;
        }
        if (totalPrice < 0) {
            totalPrice = 0.0;
        }
    }

    public static ShoppingCartItem from(String productId, String productName, int quantity, double unitPrice) {
        return new ShoppingCartItem(productId, productName, quantity, unitPrice, quantity * unitPrice);
    }
}
