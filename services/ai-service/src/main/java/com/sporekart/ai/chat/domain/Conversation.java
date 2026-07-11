package com.sporekart.ai.chat.domain;

import java.time.OffsetDateTime;
import java.util.Map;

public record Conversation(
        String id,
        String owner,
        String context,
        String title,
        boolean active,
        OffsetDateTime startedAt,
        OffsetDateTime updatedAt,
        Map<String, Object> metadata) {
}
