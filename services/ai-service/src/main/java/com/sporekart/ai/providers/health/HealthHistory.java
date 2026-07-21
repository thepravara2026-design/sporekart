package com.sporekart.ai.providers.health;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface HealthHistory {
    void record(HealthResponse response);
    List<HealthResponse> getHistory(String providerId);
    List<HealthResponse> getHistoryByTimeRange(String providerId, Instant from, Instant to);
    Optional<HealthResponse> getLastHealthy(String providerId);
    Optional<HealthResponse> getLastUnhealthy(String providerId);
}
