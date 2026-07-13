package com.sporekart.ai.analytics.interfaces.rest.dto;

import java.util.List;

public record TrendDto(
    String id,
    String name,
    String module,
    List<Double> dataPoints,
    List<String> timestamps,
    String direction,
    double changePercentage
) {}
