package com.sporekart.ai.providers.availability;

import java.time.Instant;
import java.util.List;

public interface AvailabilityHistory {
    void record(String providerId, boolean available, String reason);
    List<AvailabilityRecord> getHistory(String providerId);
    List<AvailabilityRecord> getHistoryByTimeRange(String providerId, Instant from, Instant to);
    double calculateUptime(String providerId, Instant from, Instant to);
}
