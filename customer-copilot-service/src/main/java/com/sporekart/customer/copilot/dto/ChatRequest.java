package com.sporekart.customer.copilot.dto;

import jakarta.validation.constraints.NotBlank;

public record ChatRequest(
    @NotBlank String message,
    String sessionId,
    String pageUrl,
    String pageTitle,
    String section,
    String entityType,
    String entityId
) {
    public ChatRequest {
        if (message == null || message.isBlank()) {
            throw new IllegalArgumentException("message must not be blank");
        }
    }
}
