package com.sporekart.ai.risk.interfaces.rest.dto;

import java.util.Map;

public record ConfidenceDto(
    String assessmentId,
    double overallConfidence,
    Map<String, Double> factorScores,
    String explanation,
    String calculatedAt
) {}
