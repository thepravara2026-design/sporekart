package com.sporekart.ai.monitoring.api;

import java.util.Map;

public interface MetricsCollector {
    void increment(String metricName, Map<String, String> tags);
    void record(String metricName, double value, Map<String, String> tags);
    void gauge(String metricName, double value, Map<String, String> tags);
}
