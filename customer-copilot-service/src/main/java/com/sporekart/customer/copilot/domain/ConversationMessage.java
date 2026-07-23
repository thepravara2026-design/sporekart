package com.sporekart.customer.copilot.domain;

import java.util.Map;

public record ConversationMessage(
    String id,
    String role,
    String content,
    Map<String, Object> metadata,
    String timestamp
) {
    public ConversationMessage {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("id must not be blank");
        }
        if (role == null || role.isBlank()) {
            throw new IllegalArgumentException("role must not be blank");
        }
        if (content == null) {
            throw new IllegalArgumentException("content must not be null");
        }
        if (metadata == null) {
            metadata = Map.of();
        }
        if (timestamp == null || timestamp.isBlank()) {
            timestamp = java.time.OffsetDateTime.now().toString();
        }
    }
}
