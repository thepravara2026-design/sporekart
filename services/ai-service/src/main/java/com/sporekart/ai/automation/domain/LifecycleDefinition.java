package com.sporekart.ai.automation.domain;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public record LifecycleDefinition(
    UUID id,
    String name,
    String entityType,
    LifecycleStateType initialState,
    Map<LifecycleStateType, Map<String, LifecycleStateType>> transitions,
    Map<String, Object> config,
    Instant createdAt,
    Instant updatedAt
) {}
