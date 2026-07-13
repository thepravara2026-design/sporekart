package com.sporekart.ai.risk.interfaces.rest.dto;

import java.util.Map;

public record RiskStatsDto(
    long totalAssessments,
    double averageRiskScore,
    Map<String, Long> riskDistribution,
    Map<String, Double> trustTrends,
    Map<String, Double> confidenceTrends,
    Map<String, Long> recommendationCounts
) {}
