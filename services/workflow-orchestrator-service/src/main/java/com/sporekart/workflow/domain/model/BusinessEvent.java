package com.sporekart.workflow.domain.model;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public record BusinessEvent(
    String id,
    String name,
    String source,
    String type,
    Map<String, Object> payload,
    Instant occurredAt
) {
    public static BusinessEvent create(String name, String source, String type, Map<String, Object> payload) {
        return new BusinessEvent(
            UUID.randomUUID().toString(),
            name, source, type,
            payload == null ? Map.of() : Map.copyOf(payload),
            Instant.now()
        );
    }
}
