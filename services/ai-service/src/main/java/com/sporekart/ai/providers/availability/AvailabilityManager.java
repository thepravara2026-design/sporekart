package com.sporekart.ai.providers.availability;

import java.util.List;
import java.util.Optional;

public interface AvailabilityManager {
    boolean isAvailable(String providerId);
    void markAvailable(String providerId);
    void markUnavailable(String providerId, String reason);
    double getAvailabilityPercentage(String providerId);
    Optional<AvailabilityRecord> getCurrentStatus(String providerId);
    List<String> getAvailableProviders();
    List<String> getUnavailableProviders();
}

record AvailabilityRecord(String providerId, boolean available, String reason, double percentage, long timestamp) {}
