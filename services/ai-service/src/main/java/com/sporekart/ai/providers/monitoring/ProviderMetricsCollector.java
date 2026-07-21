package com.sporekart.ai.providers.monitoring;

import com.sporekart.ai.providers.model.ProviderRequest;
import com.sporekart.ai.providers.model.ProviderResponse;

public interface ProviderMetricsCollector {
    void recordRequest(ProviderRequest request);
    void recordResponse(ProviderResponse response);
    void recordLatency(String providerId, long durationMs);
    void recordError(String providerId, String errorCode);
    void recordTokenUsage(String providerId, int promptTokens, int completionTokens);
    void recordCost(String providerId, double cost);
    void incrementCounter(String providerId, String metric);
}
