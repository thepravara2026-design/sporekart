package com.sporekart.ai.analytics.interfaces.rest.dto;

import java.util.Map;

public record StatsDto(
    long totalMetrics,
    long totalReports,
    long totalKPIs,
    long totalExports,
    Map<String, Object> detailed
) {}
