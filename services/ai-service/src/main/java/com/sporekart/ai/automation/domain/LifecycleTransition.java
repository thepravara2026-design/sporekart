package com.sporekart.ai.automation.domain;

import java.time.Instant;
import java.util.UUID;

public record LifecycleTransition(
    UUID id,
    UUID entityId,
    String entityType,
    LifecycleStateType fromState,
    LifecycleStateType toState,
    String triggeredBy,
    String reason,
    Instant transitionedAt
) {}
