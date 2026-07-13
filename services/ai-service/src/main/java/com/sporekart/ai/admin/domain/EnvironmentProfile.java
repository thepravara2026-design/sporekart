package com.sporekart.ai.admin.domain;

import java.time.Instant;
import java.util.UUID;

public record EnvironmentProfile(
    UUID id,
    String name,
    EnvironmentType type,
    String description,
    boolean active,
    String configSource,
    Instant createdAt,
    Instant updatedAt
) {}
