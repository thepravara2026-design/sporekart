package com.sporekart.bi.copilot.domain;

import java.util.List;

public record ForecastResult(
    String forecastId,
    String metric,
    String method,
    int horizon,
    List<ForecastPoint> points,
    double confidenceInterval,
    double accuracy,
    String recommendations
) {

    public record ForecastPoint(
        String period,
        double predictedValue,
        double lowerBound,
        double upperBound
    ) {}
}
