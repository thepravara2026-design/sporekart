package com.sporekart.bi.copilot.domain;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

public record BusinessInsight(
    String insightId,
    String title,
    String description,
    String category,
    String severity,
    String recommendation,
    double confidenceScore,
    Map<String, Object> supportingData,
    List<String> relatedMetrics,
    boolean actionable,
    OffsetDateTime generatedAt,
    OffsetDateTime expiresAt
) {}
