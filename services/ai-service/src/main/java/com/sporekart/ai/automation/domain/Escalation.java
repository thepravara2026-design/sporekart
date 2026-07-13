package com.sporekart.ai.automation.domain;

import java.time.Instant;
import java.util.UUID;

public record Escalation(
    UUID id,
    UUID entityId,
    String entityType,
    String reason,
    int level,
    boolean resolved,
    Instant createdAt,
    Instant resolvedAt
) {}
