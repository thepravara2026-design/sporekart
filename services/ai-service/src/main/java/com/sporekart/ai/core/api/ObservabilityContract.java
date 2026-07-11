package com.sporekart.ai.core.api;

import java.util.Map;

public interface ObservabilityContract {
    void recordMetric(String name, double value, Map<String, String> tags);
    void incrementCounter(String name, Map<String, String> tags);
    void recordTiming(String name, long durationMs, Map<String, String> tags);
    void recordError(String name, String errorType, Map<String, String> tags);
}
