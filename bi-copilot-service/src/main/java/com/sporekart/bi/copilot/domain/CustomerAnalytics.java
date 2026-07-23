package com.sporekart.bi.copilot.domain;

import java.util.List;
import java.util.Map;

public record CustomerAnalytics(
    String period,
    int totalCustomers,
    int newCustomers,
    int returningCustomers,
    int churnedCustomers,
    double retentionRate,
    double churnRate,
    double customerLifetimeValue,
    double repeatPurchaseRate,
    int inactiveCustomers,
    List<TopCustomer> topCustomers,
    Map<String, Integer> bySegment,
    Map<String, Double> revenueBySegment
) {
    public record TopCustomer(String customerId, String name, double totalSpent, int orderCount) {}
}
