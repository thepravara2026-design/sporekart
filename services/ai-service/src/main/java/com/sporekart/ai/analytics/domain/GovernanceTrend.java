package com.sporekart.ai.analytics.domain;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record GovernanceTrend(
    UUID id,
    String name,
    String module,
    List<Double> dataPoints,
    List<Instant> timestamps,
    TrendDirection direction,
    double changePercentage,
    Instant calculatedAt
) {}
