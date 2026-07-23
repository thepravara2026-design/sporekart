package com.sporekart.executive.copilot.dto;

import java.util.List;
import java.util.Map;

public record ForecastResponse(
    String forecastType,
    int horizonMonths,
    List<Double> projectedValues,
    List<Double> lowerBound,
    List<Double> upperBound,
    double confidenceScore,
    Map<String, Object> assumptions,
    List<String> keyRisks
) {}
