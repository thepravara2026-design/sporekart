package com.sporekart.ai.admin.domain;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public record ConfigurationSnapshot(
    UUID id,
    String name,
    Map<String, Object> configuration,
    String environment,
    String description,
    Instant capturedAt,
    UUID capturedBy
) {}
