package com.sporekart.ai.risk.domain;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public record RiskRecommendation(
    UUID id,
    UUID assessmentId,
    RecommendationType type,
    String title,
    String description,
    Map<String, Object> details,
    int priority,
    Instant generatedAt
) {}
