package com.sporekart.ai.analytics.interfaces.rest.dto;

import java.util.Map;

public record WidgetDto(
    String id,
    String title,
    String type,
    String metricName,
    Map<String, Object> configuration,
    int position
) {}
