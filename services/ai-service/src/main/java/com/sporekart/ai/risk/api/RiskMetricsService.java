package com.sporekart.ai.risk.api;

import java.util.Map;

public interface RiskMetricsService {
    long getTotalAssessments();
    double getAverageRiskScore();
    Map<String, Long> getRiskDistribution();
    Map<String, Double> getTrustTrends();
    Map<String, Double> getConfidenceTrends();
    Map<String, Long> getRecommendationCounts();
    Map<String, Object> getStatistics();
}
