package com.sporekart.bi.copilot.dto;

import java.util.List;

public record ForecastResponse(
    String forecastId,
    String metric,
    String method,
    List<ForecastPoint> points,
    double confidenceInterval,
    String recommendations
) {

    public record ForecastPoint(
        String period,
        double predictedValue,
        double lowerBound,
        double upperBound
    ) {}
}
