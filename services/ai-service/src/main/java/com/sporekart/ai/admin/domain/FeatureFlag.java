package com.sporekart.ai.admin.domain;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public record FeatureFlag(
    UUID id,
    String key,
    String name,
    String description,
    boolean enabled,
    String environment,
    String module,
    Map<String, Object> metadata,
    Instant createdAt,
    Instant updatedAt
) {}
