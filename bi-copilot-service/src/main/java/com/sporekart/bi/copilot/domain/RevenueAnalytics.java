package com.sporekart.bi.copilot.domain;

import java.util.Map;

public record RevenueAnalytics(
    String period,
    double grossRevenue,
    double netRevenue,
    double refundAmount,
    double averageOrderValue,
    double revenueGrowth,
    Map<String, Double> byCategory,
    Map<String, Double> byProduct,
    Map<String, Double> byRegion,
    Map<String, Double> byCustomerSegment,
    Map<String, Double> byChannel,
    Map<String, Double> byTraining,
    double previousPeriodRevenue,
    int orderCount,
    int refundCount
) {}
