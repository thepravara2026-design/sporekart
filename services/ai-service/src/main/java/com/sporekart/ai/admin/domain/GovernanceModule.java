package com.sporekart.ai.admin.domain;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public record GovernanceModule(
    UUID id,
    GovernanceModuleType type,
    String name,
    String description,
    boolean enabled,
    String version,
    Map<String, Object> metadata,
    Instant createdAt,
    Instant updatedAt
) {}
