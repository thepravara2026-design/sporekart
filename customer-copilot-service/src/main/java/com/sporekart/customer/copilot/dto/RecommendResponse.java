package com.sporekart.customer.copilot.dto;

import com.sporekart.customer.copilot.domain.ProductRecommendation;

import java.util.List;

public record RecommendResponse(
    List<ProductRecommendation> recommendations,
    String recommendationType,
    String explanation
) {
    public RecommendResponse {
        if (recommendations == null) {
            recommendations = List.of();
        }
        if (recommendationType == null || recommendationType.isBlank()) {
            recommendationType = "GENERAL";
        }
        if (explanation == null) {
            explanation = "";
        }
    }
}
