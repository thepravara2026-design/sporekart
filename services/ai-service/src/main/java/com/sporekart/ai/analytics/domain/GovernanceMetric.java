package com.sporekart.ai.analytics.domain;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public record GovernanceMetric(
    UUID id,
    String name,
    String module,
    MetricType type,
    double value,
    Map<String, Object> labels,
    Instant recordedAt
) {}
