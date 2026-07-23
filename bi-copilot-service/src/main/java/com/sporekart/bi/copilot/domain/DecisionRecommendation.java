package com.sporekart.bi.copilot.domain;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

public record DecisionRecommendation(
    String recommendationId,
    String title,
    String description,
    String category,
    String priority,
    String expectedImpact,
    double confidenceScore,
    String rationale,
    Map<String, Object> supportingData,
    List<String> actionItems,
    boolean implemented,
    OffsetDateTime createdAt
) {}
