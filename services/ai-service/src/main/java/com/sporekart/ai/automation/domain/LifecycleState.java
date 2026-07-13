package com.sporekart.ai.automation.domain;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public record LifecycleState(
    UUID id,
    UUID entityId,
    String entityType,
    LifecycleStateType currentState,
    Map<String, Object> metadata,
    Instant enteredAt,
    Instant updatedAt
) {}
