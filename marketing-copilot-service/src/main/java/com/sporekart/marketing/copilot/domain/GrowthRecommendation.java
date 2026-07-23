package com.sporekart.marketing.copilot.domain;

import java.util.List;

public record GrowthRecommendation(
    String id,
    String title,
    String description,
    GrowthArea area,
    double projectedImpact,
    double confidence,
    String timeframe,
    List<String> actionItems,
    String requiredResources
) {
    public enum GrowthArea {
        CUSTOMER_ACQUISITION, CUSTOMER_RETENTION, MARKET_EXPANSION,
        PRODUCT_LAUNCH, PRICING_OPTIMIZATION, CHANNEL_EXPANSION,
        CONTENT_STRATEGY, BRAND_POSITIONING
    }
}
