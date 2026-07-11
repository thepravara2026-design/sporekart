package com.sporekart.ai.chat.domain;

import java.time.OffsetDateTime;
import java.util.Map;

public record ChatSession(
        String id,
        String owner,
        String context,
        OffsetDateTime startedAt,
        OffsetDateTime lastActivityAt,
        Map<String, Object> metadata) {
}
