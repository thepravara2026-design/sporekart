package com.sporekart.customer.copilot.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.Map;

public record FeedbackRequest(
    @NotBlank String sessionId,
    String messageId,
    @NotBlank String rating,
    String feedback,
    Map<String, Object> metadata
) {
    public FeedbackRequest {
        if (sessionId == null || sessionId.isBlank()) {
            throw new IllegalArgumentException("sessionId must not be blank");
        }
        if (messageId == null) {
            messageId = "";
        }
        if (rating == null || rating.isBlank()) {
            throw new IllegalArgumentException("rating must not be blank");
        }
        if (feedback == null) {
            feedback = "";
        }
        if (metadata == null) {
            metadata = Map.of();
        }
    }
}
