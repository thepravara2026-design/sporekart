package com.sporekart.operations.copilot.dto;

import java.util.List;
import java.util.Map;

public record DashboardResponse(
    Map<String, Object> inventorySummary,
    Map<String, Object> orderSummary,
    Map<String, Object> warehouseSummary,
    Map<String, Object> logisticsSummary,
    Map<String, Object> kpiSummary,
    List<AlertItem> activeAlerts,
    List<RecommendationItem> recommendations
) {
    public record AlertItem(String alertId, String title, String category, String priority, String message) {}
    public record RecommendationItem(String recId, String title, String category, double impact, double confidence) {}
}
