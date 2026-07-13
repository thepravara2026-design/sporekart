package com.sporekart.ai.automation.domain;

import java.time.Instant;
import java.util.UUID;

public record EscalationPolicy(
    UUID id,
    String name,
    int maxLevels,
    long escalationDelayMs,
    String escalationTarget,
    String notificationChannel,
    Instant createdAt,
    Instant updatedAt
) {}
