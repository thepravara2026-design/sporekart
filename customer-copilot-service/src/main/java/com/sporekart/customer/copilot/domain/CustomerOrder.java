package com.sporekart.customer.copilot.domain;

import java.time.OffsetDateTime;
import java.util.List;

public record CustomerOrder(
    String orderId,
    String customerId,
    List<ShoppingCartItem> items,
    String status,
    double totalAmount,
    String currency,
    OffsetDateTime placedAt,
    OffsetDateTime estimatedDelivery,
    String trackingUrl
) {
    public CustomerOrder {
        if (orderId == null || orderId.isBlank()) {
            throw new IllegalArgumentException("orderId must not be blank");
        }
        if (customerId == null || customerId.isBlank()) {
            throw new IllegalArgumentException("customerId must not be blank");
        }
        if (items == null) {
            items = List.of();
        }
        if (status == null || status.isBlank()) {
            status = "PENDING";
        }
        if (currency == null || currency.isBlank()) {
            currency = "USD";
        }
        if (placedAt == null) {
            placedAt = OffsetDateTime.now();
        }
        if (trackingUrl == null) {
            trackingUrl = "";
        }
    }
}
