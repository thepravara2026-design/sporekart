package com.sporekart.customer.copilot.domain;

public record ProductRecommendation(
    ProductItem product,
    double score,
    String reason,
    String recommendationType
) {
    public ProductRecommendation {
        if (product == null) {
            throw new IllegalArgumentException("product must not be null");
        }
        if (reason == null || reason.isBlank()) {
            reason = "";
        }
        if (recommendationType == null || recommendationType.isBlank()) {
            recommendationType = "GENERAL";
        }
    }
}
