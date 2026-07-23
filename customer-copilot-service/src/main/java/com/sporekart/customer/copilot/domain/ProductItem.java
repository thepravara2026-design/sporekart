package com.sporekart.customer.copilot.domain;

import java.util.List;

public record ProductItem(
    String id,
    String name,
    String description,
    String category,
    double price,
    String currency,
    String imageUrl,
    int stockLevel,
    boolean isAvailable,
    double rating,
    List<String> tags
) {
    public ProductItem {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("id must not be blank");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name must not be blank");
        }
        if (description == null) {
            description = "";
        }
        if (category == null || category.isBlank()) {
            category = "General";
        }
        if (currency == null || currency.isBlank()) {
            currency = "USD";
        }
        if (imageUrl == null) {
            imageUrl = "";
        }
        if (tags == null) {
            tags = List.of();
        }
    }
}
