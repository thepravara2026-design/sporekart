package com.sporekart.ai.monitoring.api;

import java.time.OffsetDateTime;
import java.util.Map;

public interface MonitoringService {
    void recordRequest(String module, String provider, long durationMs, boolean success);
    void recordTokenUsage(String module, String provider, int promptTokens, int completionTokens);
    void recordError(String module, String provider, String errorType);
    Map<String, Object> getModuleMetrics(String module);
    Map<String, Object> getProviderMetrics(String provider);
    Map<String, Object> getPlatformSummary(OffsetDateTime from, OffsetDateTime to);
}
