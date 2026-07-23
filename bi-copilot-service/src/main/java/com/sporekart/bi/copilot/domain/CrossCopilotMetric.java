package com.sporekart.bi.copilot.domain;

import java.time.OffsetDateTime;

public record CrossCopilotMetric(
    String metricId,
    String copilotType,
    String metricName,
    double currentValue,
    double previousValue,
    double changePercent,
    String trend,
    String source,
    OffsetDateTime measuredAt
) {}
