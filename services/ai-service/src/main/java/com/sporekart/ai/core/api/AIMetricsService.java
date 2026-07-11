package com.sporekart.ai.core.api;

import java.util.Map;

public interface AIMetricsService {
    void recordExecution(String module, long durationMs, boolean success);
    void recordLatency(String module, String provider, long durationMs);
    void recordError(String module, String provider, String errorType);
    Map<String, Object> getGatewayMetrics();
    Map<String, Object> getModuleMetrics(String module);
}
