package com.sporekart.ai.analytics.interfaces.rest.dto;

import java.util.Map;

public record MetricDto(
    String id,
    String name,
    String module,
    String type,
    double value,
    Map<String, Object> labels,
    String recordedAt
) {}
