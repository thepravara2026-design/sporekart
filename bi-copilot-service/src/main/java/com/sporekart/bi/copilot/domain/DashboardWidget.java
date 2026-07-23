package com.sporekart.bi.copilot.domain;

import java.util.Map;

public record DashboardWidget(
    String widgetId,
    String title,
    String widgetType,
    String dataSource,
    String metric,
    Map<String, Object> configuration,
    int width,
    int height,
    int positionX,
    int positionY,
    String refreshInterval,
    boolean enabled
) {}
