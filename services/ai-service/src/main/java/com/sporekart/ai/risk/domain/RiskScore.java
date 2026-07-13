package com.sporekart.ai.risk.domain;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public record RiskScore(
    UUID id,
    UUID assessmentId,
    double overallScore,
    RiskLevel riskLevel,
    Map<RiskCategory, Double> categoryScores,
    int factorCount,
    Instant calculatedAt
) {}
