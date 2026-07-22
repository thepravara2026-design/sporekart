package com.sporekart.ai.conversation.domain;

import java.time.Instant;
import java.util.Map;

public record MemoryEntry(
        String id,
        MemoryLayer layer,
        String key,
        String value,
        Map<String, String> metadata,
        Instant createdAt,
        Instant expiresAt) {
}
