package com.sporekart.ai.providers.monitoring;

import java.util.Map;

public interface ProviderPerformanceTracker {
    void recordLatency(String providerId, String operation, long durationMs);
    void recordThroughput(String providerId, long requestCount);
    Map<String, Double> getAverageLatency(String providerId);
    double getP95Latency(String providerId);
    double getP99Latency(String providerId);
    long getThroughput(String providerId);
    Map<String, Object> getPerformanceReport(String providerId);
}
