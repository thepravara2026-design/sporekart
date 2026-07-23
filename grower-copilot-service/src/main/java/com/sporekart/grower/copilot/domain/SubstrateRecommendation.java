package com.sporekart.grower.copilot.domain;

import java.util.List;

public record SubstrateRecommendation(
    String substrateId,
    String name,
    String type,
    String description,
    double moisturePercent,
    String sterilizationMethod,
    int preparationDays,
    double costPerKg,
    double expectedYieldMultiplier,
    List<String> suitableSpecies,
    List<String> preparationSteps,
    List<String> advantages,
    List<String> disadvantages,
    List<String> tips
) {}
