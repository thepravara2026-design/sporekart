package com.sporekart.executive.copilot.dto;

import java.util.List;
import java.util.Map;

public record FinancialResponse(
    Map<String, Object> summary,
    List<FinancialMetricItem> metrics,
    RevenueDetail revenue
) {
    public record FinancialMetricItem(String name, double current, double previous, double target, String unit, String trend) {}
    public record RevenueDetail(double total, double growth, Map<String, Double> byCategory, double aov, double ltv) {}
}
