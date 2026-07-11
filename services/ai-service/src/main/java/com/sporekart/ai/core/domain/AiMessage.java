package com.sporekart.ai.core.domain;

import java.time.OffsetDateTime;
import java.util.Map;

public record AiMessage(
        String id,
        AiRole role,
        String content,
        OffsetDateTime timestamp,
        Map<String, Object> metadata) {
    public AiMessage(AiRole role, String content) {
        this(null, role, content, OffsetDateTime.now(), Map.of());
    }
}
