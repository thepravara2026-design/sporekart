package com.sporekart.ai.governance.domain;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

public record GovernanceConfiguration(
    UUID id,
    String key,
    String value,
    String description,
    GovernanceScope scope,
    GovernanceMode mode,
    Map<String, Object> metadata,
    boolean isActive,
    int version,
    OffsetDateTime createdAt,
    OffsetDateTime updatedAt) {
}
