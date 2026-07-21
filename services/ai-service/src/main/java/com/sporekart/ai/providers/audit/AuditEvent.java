package com.sporekart.ai.providers.audit;

import java.time.Instant;

public record AuditEvent(
    String eventId,
    String providerId,
    AuditEventType eventType,
    String source,
    String description,
    String details,
    Instant timestamp
) {}
