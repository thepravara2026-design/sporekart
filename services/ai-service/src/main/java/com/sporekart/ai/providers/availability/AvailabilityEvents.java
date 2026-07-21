package com.sporekart.ai.providers.availability;

public interface AvailabilityEvents {
    void onProviderAvailable(String providerId);
    void onProviderUnavailable(String providerId, String reason);
    void onAvailabilityRestored(String providerId);
    void onSlaViolation(String providerId, double current, double required);
}
