package com.sporekart.ai.automation.domain;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public record AutomationRule(
    UUID id,
    String name,
    String description,
    String condition,
    String action,
    Map<String, Object> params,
    boolean enabled,
    Instant createdAt,
    Instant updatedAt
) {}
