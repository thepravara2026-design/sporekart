package com.sporekart.cart.application.dto;

import com.sporekart.cart.domain.model.Cart;
import com.sporekart.cart.domain.model.CartItem;
import com.sporekart.cart.domain.model.CartStatus;

import java.time.Instant;
import java.util.List;

public record CartResponse(
        String id,
        String customerId,
        List<CartItem> items,
        CartStatus status,
        Instant createdAt,
        Instant updatedAt) {

    public static CartResponse from(Cart cart) {
        return new CartResponse(
                cart.getId(),
                cart.getCustomerId(),
                cart.getItems(),
                cart.getStatus(),
                cart.getCreatedAt(),
                cart.getUpdatedAt());
    }
}