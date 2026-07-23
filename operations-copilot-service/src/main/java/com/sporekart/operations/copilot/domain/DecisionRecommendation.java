package com.sporekart.operations.copilot.domain;

import java.util.List;

public record DecisionRecommendation(
    String recId,
    String title,
    String description,
    DecisionCategory category,
    double projectedImpact,
    double cost,
    double confidenceScore,
    List<String> supportingAnalytics,
    String riskLevel,
    String timeframe
) {
    public enum DecisionCategory {
        INVENTORY, PROCUREMENT, WAREHOUSE, LOGISTICS, DEMAND, PRICING, CAPACITY, PROCESS
    }
}
