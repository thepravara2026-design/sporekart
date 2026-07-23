package com.sporekart.operations.copilot.domain;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public record DemandForecast(
    String forecastId,
    String sku,
    String productName,
    LocalDate forecastDate,
    int predictedDemand,
    int lowerBound,
    int upperBound,
    double confidenceScore,
    String seasonality,
    List<LocalDate> demandCurve,
    List<Integer> demandValues,
    Map<String, Integer> regionalDemand,
    Map<String, Integer> festivalDemand
) {}
