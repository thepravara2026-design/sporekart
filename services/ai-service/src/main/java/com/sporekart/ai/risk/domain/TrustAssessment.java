package com.sporekart.ai.risk.domain;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public record TrustAssessment(
    UUID id,
    UUID assessmentId,
    double overallTrustScore,
    Map<TrustFactor, Double> factorScores,
    Map<TrustFactor, String> factorReasons,
    Instant calculatedAt
) {}
