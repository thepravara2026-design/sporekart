package com.sporekart.ai.analytics.interfaces.rest.dto;

import java.util.List;
import java.util.Map;

public record DashboardDto(
    String id,
    String name,
    String description,
    List<WidgetDto> widgets,
    Map<String, Object> configuration,
    String createdAt
) {}
