package com.sporekart.ai.events;

import java.time.Instant;
import java.util.UUID;

public record IntegrationEvent(
    UUID eventId,
    String eventType,
    String source,
    String destination,
    String payload,
    String schemaVersion,
    Instant occurredAt,
    String correlationId
) {
    public IntegrationEvent {
        if (eventId == null) eventId = UUID.randomUUID();
        if (occurredAt == null) occurredAt = Instant.now();
    }
}
