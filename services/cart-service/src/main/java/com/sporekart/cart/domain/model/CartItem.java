package com.sporekart.cart.domain.model;

import java.math.BigDecimal;
import java.util.UUID;

public class CartItem {
    private final String id;
    private final String productId;
    private final String sku;
    private final String name;
    private final BigDecimal unitPrice;
    private int quantity;
    private BigDecimal subtotal;

    public CartItem(String id, String productId, String sku, String name, BigDecimal unitPrice, int quantity,
            BigDecimal subtotal) {
        this.id = id;
        this.productId = productId;
        this.sku = sku;
        this.name = name;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.subtotal = subtotal;
    }

    public static CartItem create(String productId, String sku, String name, BigDecimal unitPrice, int quantity) {
        String id = UUID.randomUUID().toString();
        BigDecimal subtotal = unitPrice.multiply(BigDecimal.valueOf(quantity));
        return new CartItem(id, productId, sku, name, unitPrice, quantity, subtotal);
    }

    public void incrementQuantity(int additionalQuantity) {
        this.quantity += additionalQuantity;
        this.subtotal = this.unitPrice.multiply(BigDecimal.valueOf(this.quantity));
    }

    public void updateQuantity(int newQuantity) {
        this.quantity = newQuantity;
        this.subtotal = this.unitPrice.multiply(BigDecimal.valueOf(this.quantity));
    }

    public String getId() {
        return id;
    }

    public String getProductId() {
        return productId;
    }

    public String getSku() {
        return sku;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }
}