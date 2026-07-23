package com.sporekart.bi.copilot.domain;

import java.util.Map;

public record CultivationAnalytics(
    String analyticsId,
    String period,
    double totalYieldKg,
    double averageYieldPerBatch,
    Map<String, Double> yieldBySpecies,
    Map<String, Double> yieldByRegion,
    double averageCycleTime,
    double contaminationRate,
    double averageDiseaseIncidence,
    int totalGrowers,
    Map<String, Double> revenueFromGrowers,
    double growerSatisfactionScore
) {}
