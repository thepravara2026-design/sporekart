package com.sporekart.ai.events;

import java.time.Instant;
import java.util.UUID;

public record AiDomainEvent(
    UUID eventId,
    String eventType,
    String source,
    String payload,
    Instant occurredAt,
    String correlationId,
    String causationId
) {
    public AiDomainEvent {
        if (eventId == null) eventId = UUID.randomUUID();
        if (occurredAt == null) occurredAt = Instant.now();
    }
}
