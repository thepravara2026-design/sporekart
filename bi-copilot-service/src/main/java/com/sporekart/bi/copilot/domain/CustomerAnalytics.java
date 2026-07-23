package com.sporekart.bi.copilot.domain;

import java.util.Map;

public record CustomerAnalytics(
    String analyticsId,
    String period,
    int totalCustomers,
    int newCustomers,
    int churnedCustomers,
    double churnRate,
    double customerLifetimeValue,
    double acquisitionCost,
    double retentionRate,
    Map<String, Integer> customersBySegment,
    Map<String, Double> revenueBySegment,
    double averageSatisfactionScore,
    int activeCustomers,
    Map<String, Integer> customersByRegion
) {}
