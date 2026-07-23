package com.sporekart.admin.domain;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public record SalesSummary(
    String period,
    BigDecimal totalRevenue,
    int totalOrders,
    BigDecimal averageOrderValue,
    BigDecimal growthRate,
    List<String> topProducts,
    Map<String, BigDecimal> revenueByCategory,
    int orderCount
) {}
