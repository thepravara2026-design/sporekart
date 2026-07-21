package com.sporekart.ai.providers.monitoring;

import java.util.Map;

public interface ProviderCostTracker {
    void recordCost(String providerId, String model, double cost);
    Map<String, Double> getAverageCost(String providerId);
    double getTotalCost(String providerId);
    double getEstimatedMonthlyCost(String providerId);
    Map<String, Object> getCostReport(String providerId);
}
