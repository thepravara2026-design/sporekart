package com.sporekart.ai.decision.domain;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

public record DecisionRule(
    UUID id, String name, String description, DecisionAction action,
    int priority, int weight, Map<String, Object> conditions,
    Map<String, Object> overrides, boolean isActive,
    OffsetDateTime createdAt, OffsetDateTime updatedAt) {}
