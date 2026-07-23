package com.sporekart.grower.copilot.domain;

public record YieldPrediction(
    String predictionId,
    String speciesName,
    String substrateType,
    double areaSquareMeters,
    int numberOfBags,
    double expectedYieldKg,
    double yieldEfficiencyPercent,
    double estimatedRevenue,
    double productionCost,
    double profitMargin,
    double riskScore,
    String harvestWindowStart,
    String harvestWindowEnd,
    int confidenceLevel,
    String recommendations
) {}
