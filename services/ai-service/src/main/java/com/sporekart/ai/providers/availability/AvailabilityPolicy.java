package com.sporekart.ai.providers.availability;

public interface AvailabilityPolicy {
    double getRequiredAvailability(String providerId);
    int getMaxDowntimeEvents(String providerId);
    boolean isSlaCompliant(String providerId);
    double getSlaTarget();
}
