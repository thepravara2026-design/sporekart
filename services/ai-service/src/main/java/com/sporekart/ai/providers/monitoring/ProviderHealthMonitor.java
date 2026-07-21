package com.sporekart.ai.providers.monitoring;

import com.sporekart.ai.providers.ProviderStatus;

public interface ProviderHealthMonitor {
    ProviderStatus checkHealth(String providerId);
    boolean isAvailable(String providerId);
    long getLastHeartbeat(String providerId);
    int getFailureCount(String providerId);
    double getAvailabilityPercentage(String providerId);
    boolean isInMaintenance(String providerId);
    boolean isCircuitBreakerOpen(String providerId);
}
