package com.sporekart.copilot.memory;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

public record MemoryEntry(
    String id,
    MemoryType type,
    String key,
    Object value,
    float relevance,
    OffsetDateTime createdAt,
    OffsetDateTime expiresAt
) {
    public MemoryEntry {
        Objects.requireNonNull(type, "type must not be null");
        Objects.requireNonNull(key, "key must not be null");
        Objects.requireNonNull(value, "value must not be null");
        Objects.requireNonNull(createdAt, "createdAt must not be null");
        if (key.isBlank()) throw new IllegalArgumentException("key must not be blank");
    }

    public static MemoryEntry of(MemoryType type, String key, Object value) {
        return new MemoryEntry(
            UUID.randomUUID().toString(),
            type,
            key,
            value,
            1.0f,
            OffsetDateTime.now(),
            null
        );
    }

    public static MemoryEntry of(MemoryType type, String key, Object value, float relevance) {
        return new MemoryEntry(
            UUID.randomUUID().toString(),
            type,
            key,
            value,
            relevance,
            OffsetDateTime.now(),
            null
        );
    }

    public static MemoryEntry of(MemoryType type, String key, Object value, OffsetDateTime expiresAt) {
        return new MemoryEntry(
            UUID.randomUUID().toString(),
            type,
            key,
            value,
            1.0f,
            OffsetDateTime.now(),
            expiresAt
        );
    }

    public boolean isExpired() {
        return expiresAt != null && OffsetDateTime.now().isAfter(expiresAt);
    }
}
