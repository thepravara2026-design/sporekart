package com.sporekart.ai.providers.monitoring;

import com.sporekart.ai.providers.AIProvider;

import java.util.Map;

public interface AvailabilityTracker {
    void recordSuccess(String providerId);
    void recordFailure(String providerId, String reason);
    double getAvailability(String providerId);
    long getUptime(String providerId);
    long getDowntime(String providerId);
    Map<String, Object> getAvailabilityReport(String providerId);
}
