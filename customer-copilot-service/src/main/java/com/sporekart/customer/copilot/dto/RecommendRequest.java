package com.sporekart.customer.copilot.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record RecommendRequest(
    @NotBlank String customerId,
    String category,
    String context,
    int limit,
    List<String> excludeProductIds
) {
    public RecommendRequest {
        if (customerId == null || customerId.isBlank()) {
            throw new IllegalArgumentException("customerId must not be blank");
        }
        if (category == null) {
            category = "";
        }
        if (context == null) {
            context = "";
        }
        if (limit <= 0) {
            limit = 10;
        }
        if (excludeProductIds == null) {
            excludeProductIds = List.of();
        }
    }
}
