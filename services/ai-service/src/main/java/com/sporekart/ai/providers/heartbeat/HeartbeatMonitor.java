package com.sporekart.ai.providers.heartbeat;

import java.util.List;

public interface HeartbeatMonitor {
    void startMonitoring(String providerId);
    void stopMonitoring(String providerId);
    boolean isMonitoring(String providerId);
    List<String> getMonitoredProviders();
    HeartbeatStatus getStatus(String providerId);
}
