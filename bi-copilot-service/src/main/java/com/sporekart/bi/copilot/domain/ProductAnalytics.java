package com.sporekart.bi.copilot.domain;

import java.util.List;
import java.util.Map;

public record ProductAnalytics(
    String period,
    List<ProductPerformance> topProducts,
    List<ProductPerformance> worstProducts,
    List<ProductPerformance> fastMovers,
    List<ProductPerformance> slowMovers,
    Map<String, Double> categoryPerformance,
    double conversionRate,
    List<String> searchTrends,
    List<String> wishlistTrends
) {
    public record ProductPerformance(String productId, String name, String category, double revenue, int unitsSold, double growth, double profitMargin) {}
}
