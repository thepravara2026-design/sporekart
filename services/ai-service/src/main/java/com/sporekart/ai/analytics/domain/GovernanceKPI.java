package com.sporekart.ai.analytics.domain;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public record GovernanceKPI(
    UUID id,
    String name,
    String description,
    String module,
    double currentValue,
    double targetValue,
    double threshold,
    KpiStatus status,
    Map<String, Object> dimensions,
    Instant calculatedAt
) {}
