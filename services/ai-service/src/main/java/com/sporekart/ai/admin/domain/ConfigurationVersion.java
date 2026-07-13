package com.sporekart.ai.admin.domain;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public record ConfigurationVersion(
    UUID id,
    UUID configId,
    int version,
    String value,
    String changeReason,
    UUID changedBy,
    Instant changedAt
) {}
