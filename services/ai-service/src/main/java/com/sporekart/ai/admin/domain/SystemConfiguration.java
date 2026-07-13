package com.sporekart.ai.admin.domain;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public record SystemConfiguration(
    UUID id,
    String name,
    String category,
    Map<String, Object> values,
    boolean systemManaged,
    Instant createdAt,
    Instant updatedAt
) {}
