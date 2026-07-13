package com.sporekart.ai.admin.domain;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public record AdminOperation(
    UUID id,
    AdminOperationType type,
    String description,
    Map<String, Object> details,
    UUID performedBy,
    String ipAddress,
    boolean successful,
    Instant performedAt
) {}
