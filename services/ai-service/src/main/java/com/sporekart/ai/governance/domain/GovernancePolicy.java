package com.sporekart.ai.governance.domain;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

public record GovernancePolicy(
    UUID id,
    String name,
    String description,
    GovernanceScope scope,
    GovernanceStatus status,
    int priority,
    Map<String, Object> rules,
    Map<String, Object> conditions,
    boolean isActive,
    UUID createdBy,
    OffsetDateTime createdAt,
    OffsetDateTime updatedAt) {
}
