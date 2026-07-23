package com.sporekart.grower.copilot.dto;

public record YieldResponse(
    String predictionId,
    double expectedYieldKg,
    double yieldEfficiency,
    double estimatedRevenue,
    double productionCost,
    double profitMargin,
    double riskScore,
    String harvestWindow,
    String recommendations,
    int confidenceLevel
) {}
