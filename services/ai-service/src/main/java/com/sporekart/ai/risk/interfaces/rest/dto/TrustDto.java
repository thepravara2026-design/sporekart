package com.sporekart.ai.risk.interfaces.rest.dto;

import java.util.Map;

public record TrustDto(
    String assessmentId,
    double overallTrustScore,
    Map<String, Double> factorScores,
    Map<String, String> factorReasons,
    String calculatedAt
) {}
