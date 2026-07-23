package com.sporekart.admin.domain;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public record ForecastResult(
    String metric,
    String period,
    Map<LocalDate, Double> forecastValues,
    String confidenceInterval,
    String trend,
    List<Double> seasonalityFactors
) {}
