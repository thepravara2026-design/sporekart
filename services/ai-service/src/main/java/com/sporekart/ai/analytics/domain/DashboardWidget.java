package com.sporekart.ai.analytics.domain;

import java.util.Map;
import java.util.UUID;

public record DashboardWidget(
    UUID id,
    UUID dashboardId,
    String title,
    String type,
    String metricName,
    Map<String, Object> configuration,
    int position,
    int width,
    int height
) {}
