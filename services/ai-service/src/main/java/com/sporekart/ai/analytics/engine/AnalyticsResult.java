package com.sporekart.ai.analytics.engine;

import java.util.List;
import java.util.Map;

public record AnalyticsResult(
    String dashboardId,
    Map<String, Object> dashboardData,
    List<Map<String, Object>> reports,
    Map<String, Object> kpis,
    Map<String, Object> trends,
    Map<String, Object> metrics,
    Map<String, Object> exports
) {}
