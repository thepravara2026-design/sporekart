package com.sporekart.executive.copilot.domain;

import java.util.List;
import java.util.Map;

public record BusinessForecast(
    String forecastId,
    String forecastType,
    int horizonMonths,
    List<Double> projectedValues,
    List<Double> lowerBound,
    List<Double> upperBound,
    double confidenceScore,
    Map<String, Object> assumptions,
    List<String> keyRisks
) {}
