package com.sporekart.executive.copilot.dto;

import java.util.List;
import java.util.Map;

public record DashboardResponse(
    String summary,
    CompanyHealth health,
    Map<String, Object> financialHighlights,
    List<RecommendationItem> recommendations,
    RiskSummary risks,
    List<MetricItem> keyMetrics,
    List<String> alerts
) {
    public record CompanyHealth(double overallScore, double trend, String riskLevel, Map<String, Double> dimensions) {}
    public record RecommendationItem(String title, String category, double roi, double impact, double confidence, String timeframe) {}
    public record RiskSummary(double overallScore, String level, int criticalCount, int highCount) {}
    public record MetricItem(String name, String category, double current, double previous, double target, String unit, String trend) {}
}
