package com.sporekart.bi.copilot.domain;

import java.time.OffsetDateTime;

public record TrendDataPoint(
    String trendId,
    String metric,
    String period,
    double value,
    double movingAverage,
    double seasonalFactor,
    double trendLine,
    double deviation,
    String direction,
    double changePercent,
    OffsetDateTime timestamp
) {}
