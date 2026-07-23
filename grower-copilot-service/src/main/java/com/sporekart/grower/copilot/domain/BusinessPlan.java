package com.sporekart.grower.copilot.domain;

import java.util.List;

public record BusinessPlan(
    String planId,
    String speciesName,
    double investmentAmount,
    double expectedRevenue,
    double expectedRoi,
    int cycleDurationDays,
    double breakEvenPoint,
    List<ProductionPlanner> productionPlans,
    List<String> marketInsights,
    List<String> seasonalDemand,
    List<String> pricingSuggestions,
    String packagingAdvice,
    String storageAdvice,
    String transportationAdvice
) {

    public record ProductionPlanner(
        int weekNumber,
        String activity,
        List<String> tasks,
        List<String> requiredMaterials,
        String expectedOutcome
    ) {}
}
