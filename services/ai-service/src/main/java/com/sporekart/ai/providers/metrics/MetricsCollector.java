package com.sporekart.ai.providers.metrics;

import java.util.Map;

public interface MetricsCollector {
    void recordMetric(String providerId, String name, double value);
    void recordLatency(String providerId, long latency);
    void recordError(String providerId, String errorType);
    void recordSuccess(String providerId);
    Map<String, Double> getMetrics(String providerId);
    Map<String, Map<String, Double>> getAllMetrics();
    void reset(String providerId);
}
