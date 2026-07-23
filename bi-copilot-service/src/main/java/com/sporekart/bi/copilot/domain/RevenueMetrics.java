package com.sporekart.bi.copilot.domain;

import java.time.OffsetDateTime;
import java.util.Map;

public record RevenueMetrics(
    String metricId,
    String period,
    String periodStart,
    String periodEnd,
    double totalRevenue,
    double totalOrders,
    double averageOrderValue,
    double revenuePerCustomer,
    Map<String, Double> revenueByProduct,
    Map<String, Double> revenueByCategory,
    Map<String, Double> revenueByRegion,
    Map<String, Double> revenueByChannel,
    double growthRate,
    double previousPeriodRevenue,
    OffsetDateTime calculatedAt
) {}
