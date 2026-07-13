package com.sporekart.ai.risk.domain;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public record RiskAssessment(
    UUID id,
    String module,
    String action,
    RiskAssessmentStatus status,
    Map<String, Object> context,
    UUID reviewerId,
    Instant assessedAt,
    Instant completedAt
) {}
