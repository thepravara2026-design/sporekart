package com.sporekart.bi.copilot.domain;

import java.time.OffsetDateTime;

public record AnomalyAlert(
    String anomalyId,
    String metric,
    String dimension,
    double observedValue,
    double expectedValue,
    double deviationScore,
    String severity,
    String description,
    String impact,
    String recommendedAction,
    boolean autoResolved,
    OffsetDateTime detectedAt,
    OffsetDateTime resolvedAt
) {}
