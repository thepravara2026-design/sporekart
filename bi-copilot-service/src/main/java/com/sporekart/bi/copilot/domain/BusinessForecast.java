package com.sporekart.bi.copilot.domain;

import java.util.List;

public record BusinessForecast(
    String forecastId,
    String metric,
    String period,
    String method,
    List<ForecastPoint> points,
    double confidenceInterval,
    double accuracy,
    String seasonality,
    String trend,
    String recommendations
) {
    public record ForecastPoint(String period, double value, double lowerBound, double upperBound) {}
}
