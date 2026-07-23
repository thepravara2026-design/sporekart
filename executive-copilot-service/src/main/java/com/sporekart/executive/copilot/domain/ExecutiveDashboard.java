package com.sporekart.executive.copilot.domain;

import java.util.List;
import java.util.Map;

public record ExecutiveDashboard(
    String summary,
    CompanyHealth health,
    Map<String, Object> financialHighlights,
    List<StrategicRecommendation> topRecommendations,
    RiskMatrix risks,
    List<PerformanceMetric> keyMetrics,
    List<String> alerts
) {}
