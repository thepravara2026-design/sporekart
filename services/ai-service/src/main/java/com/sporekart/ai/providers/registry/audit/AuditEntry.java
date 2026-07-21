package com.sporekart.ai.providers.registry.audit;

import java.time.Instant;

public record AuditEntry(
    String eventId,
    String providerId,
    String eventType,
    String description,
    String source,
    Instant timestamp,
    String details
) {}
