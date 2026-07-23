package com.sporekart.marketing.copilot.dto;

import java.util.List;

public record GrowthRecommendationResponse(
    List<GrowthItem> recommendations,
    String summary
) {
    public record GrowthItem(String title, String description, String area, double projectedImpact, double confidence, String timeframe) {}
}
