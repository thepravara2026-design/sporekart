package com.sporekart.ai.risk.domain;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public record ConfidenceScore(
    UUID id,
    UUID assessmentId,
    double overallConfidence,
    Map<ConfidenceFactor, Double> factorScores,
    String explanation,
    Instant calculatedAt
) {}
