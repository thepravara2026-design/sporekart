package com.sporekart.executive.copilot.domain;

import java.util.Map;

public record RevenueBreakdown(
    double totalRevenue,
    double previousRevenue,
    double growthRate,
    Map<String, Double> byProductCategory,
    Map<String, Double> byRegion,
    Map<String, Double> byChannel,
    double averageOrderValue,
    double customerLifetimeValue
) {}
