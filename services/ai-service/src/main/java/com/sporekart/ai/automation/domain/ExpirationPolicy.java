package com.sporekart.ai.automation.domain;

import java.time.Instant;
import java.util.UUID;

public record ExpirationPolicy(
    UUID id,
    String name,
    String entityType,
    long ttlMs,
    String actionOnExpiry,
    boolean enabled,
    Instant createdAt,
    Instant updatedAt
) {}
