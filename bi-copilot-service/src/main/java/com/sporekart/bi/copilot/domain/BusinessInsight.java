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
    double confidenceScore,
    String businessImpact,
    List<String> actionItems,
    Map<String, Object> supportingData,
    OffsetDateTime generatedAt
) {}
