package com.sporekart.cart.domain.model;

import com.sporekart.cart.common.exception.InvalidCartOperationException;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Cart {
    private final String id;
    private final String customerId;
    private final List<CartItem> items;
    private CartStatus status;
    private final Instant createdAt;
    private Instant updatedAt;

    public Cart(String id, String customerId, List<CartItem> items, CartStatus status, Instant createdAt,
            Instant updatedAt) {
        this.id = id;
        this.customerId = customerId;
        this.items = new ArrayList<>(items);
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Cart create(String customerId) {
        return new Cart(UUID.randomUUID().toString(), customerId, new ArrayList<>(), CartStatus.ACTIVE,
                Instant.now(), Instant.now());
    }

    public void addItem(String productId, String sku, String name, BigDecimal unitPrice, int quantity) {
        if (quantity <= 0) {
            throw new InvalidCartOperationException("Item quantity must be positive");
        }
        for (CartItem item : items) {
            if (item.getProductId().equals(productId)) {
                item.incrementQuantity(quantity);
                updatedAt = Instant.now();
                return;
            }
        }
        items.add(CartItem.create(productId, sku, name, unitPrice, quantity));
        updatedAt = Instant.now();
    }

    public void updateItemQuantity(String itemId, int newQuantity) {
        if (newQuantity <= 0) {
            throw new InvalidCartOperationException("Item quantity must be positive");
        }
        CartItem item = items.stream()
                .filter(i -> i.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new InvalidCartOperationException("Item not found in cart"));
        item.updateQuantity(newQuantity);
        updatedAt = Instant.now();
    }

    public void removeItem(String itemId) {
        boolean removed = items.removeIf(i -> i.getId().equals(itemId));
        if (!removed) {
            throw new InvalidCartOperationException("Item not found in cart");
        }
        updatedAt = Instant.now();
    }

    public void checkout() {
        if (items.isEmpty()) {
            throw new InvalidCartOperationException("Cannot checkout an empty cart");
        }
        this.status = CartStatus.CHECKED_OUT;
        updatedAt = Instant.now();
    }

    public String getId() {
        return id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public List<CartItem> getItems() {
        return new ArrayList<>(items);
    }

    public CartStatus getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}