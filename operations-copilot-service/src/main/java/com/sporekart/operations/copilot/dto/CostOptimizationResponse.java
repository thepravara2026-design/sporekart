package com.sporekart.operations.copilot.dto;

import java.util.List;
import java.util.Map;

public record CostOptimizationResponse(
    double totalOperationalCost,
    Map<String, Double> costBreakdown,
    List<SavingsOpportunity> opportunities,
    double totalPotentialSavings
) {
    public record SavingsOpportunity(String area, String suggestion, double estimatedSavings, double implementationEffort, String riskLevel) {}
}
