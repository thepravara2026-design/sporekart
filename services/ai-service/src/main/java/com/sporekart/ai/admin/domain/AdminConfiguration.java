package com.sporekart.ai.admin.domain;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public record AdminConfiguration(
    UUID id,
    String key,
    String value,
    String module,
    String environment,
    String description,
    ConfigurationStatus status,
    int version,
    UUID updatedBy,
    Instant createdAt,
    Instant updatedAt
) {}
