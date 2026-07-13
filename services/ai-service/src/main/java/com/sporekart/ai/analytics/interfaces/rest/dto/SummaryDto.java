package com.sporekart.ai.analytics.interfaces.rest.dto;

import java.util.Map;

public record SummaryDto(
    Map<String, Object> metrics,
    Map<String, Object> kpis,
    Map<String, Object> trends,
    String generatedAt
) {}
