package com.sporekart.marketing.copilot.domain;

import java.util.Map;

public record MarketingAnalytics(
    double totalSpend,
    double totalRevenue,
    double overallROAS,
    double averageCTR,
    double averageConversionRate,
    double customerAcquisitionCost,
    double customerLTV,
    Map<String, Double> channelPerformance,
    Map<String, Double> campaignPerformance,
    Map<String, Double> campaignROAS,
    Map<String, Integer> topPerformingContent
) {}
