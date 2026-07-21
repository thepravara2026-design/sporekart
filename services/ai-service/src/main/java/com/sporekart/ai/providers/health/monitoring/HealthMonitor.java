package com.sporekart.ai.providers.health.monitoring;

import com.sporekart.ai.providers.health.HealthStatus;

import java.util.List;

public interface HealthMonitor {
    void startMonitoring(String providerId);
    void stopMonitoring(String providerId);
    boolean isMonitoring(String providerId);
    List<String> getMonitoredProviders();
    HealthStatus getCurrentStatus(String providerId);
}
