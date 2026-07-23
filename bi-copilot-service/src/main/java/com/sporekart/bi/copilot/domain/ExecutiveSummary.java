package com.sporekart.bi.copilot.domain;

import java.time.OffsetDateTime;
import java.util.List;

public record ExecutiveSummary(
    String summaryId,
    String period,
    String type,
    CompanyHealthScore healthScore,
    RevenueAnalytics revenue,
    CustomerAnalytics customers,
    ProductAnalytics products,
    InventoryAnalytics inventory,
    TrainingAnalytics training,
    List<BusinessInsight> insights,
    List<DecisionRecommendation> recommendations,
    List<RiskAlert> risks,
    OffsetDateTime generatedAt
) {}
