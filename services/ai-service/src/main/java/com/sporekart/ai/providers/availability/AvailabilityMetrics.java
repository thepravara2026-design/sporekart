package com.sporekart.ai.providers.availability;

public interface AvailabilityMetrics {
    void recordAvailabilityChange(String providerId, boolean available);
    double getOverallAvailability();
    double getProviderAvailability(String providerId);
    int getTotalDowntimeEvents(String providerId);
    long getTotalDowntimeDuration(String providerId);
}
