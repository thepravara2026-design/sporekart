package com.sporekart.ai.events;

import java.time.Instant;
import java.util.UUID;

public record OutboxEvent(
    UUID id,
    String aggregateId,
    String aggregateType,
    String eventType,
    String payload,
    Instant occurredAt,
    boolean processed,
    Instant processedAt,
    int retryCount
) {
    public OutboxEvent {
        if (id == null) id = UUID.randomUUID();
        if (occurredAt == null) occurredAt = Instant.now();
    }
}
