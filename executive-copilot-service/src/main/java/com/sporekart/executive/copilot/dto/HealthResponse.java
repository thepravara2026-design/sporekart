package com.sporekart.executive.copilot.dto;

import java.util.List;
import java.util.Map;

public record HealthResponse(
    double overallScore,
    double previousScore,
    double trend,
    String riskLevel,
    List<HealthDimensionItem> dimensions,
    String summary
) {
    public record HealthDimensionItem(String name, double score, double previous, String status, String insight) {}
}
