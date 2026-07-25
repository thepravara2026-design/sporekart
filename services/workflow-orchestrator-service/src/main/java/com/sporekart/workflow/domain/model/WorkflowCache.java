package com.sporekart.workflow.domain.model;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public record WorkflowCache(
    String id,
    String key,
    Object value,
    Map<String, Object> metadata,
    Instant createdAt,
    Instant expiresAt
) {
    public static WorkflowCache create(String key, Object value, int ttlSeconds) {
        return new WorkflowCache(
            UUID.randomUUID().toString(),
            key, value, Map.of(),
            Instant.now(),
            Instant.now().plusSeconds(ttlSeconds)
        );
    }
}
