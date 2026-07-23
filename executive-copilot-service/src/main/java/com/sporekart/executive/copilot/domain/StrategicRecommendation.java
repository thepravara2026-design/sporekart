package com.sporekart.executive.copilot.domain;

import java.util.List;

public record StrategicRecommendation(
    String recId,
    String title,
    String description,
    RecommendationCategory category,
    double expectedROI,
    double expectedImpact,
    double confidenceScore,
    String timeframe,
    List<String> supportingAnalytics,
    String riskLevel,
    List<String> actionItems
) {
    public enum RecommendationCategory {
        EXPANSION, MARKET_ENTRY, PRICING, HIRING, INVESTMENT,
        INVENTORY, TRAINING, MARKETING, TECHNOLOGY, COST_OPTIMIZATION
    }
}
