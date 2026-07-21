package com.sporekart.ai.providers.health;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ProviderHealthManager {
    HealthResponse check(String providerId);
    Map<String, HealthResponse> checkAll();
    List<String> getHealthy();
    List<String> getUnhealthy();
    List<String> getDegraded();
    Optional<HealthResponse> getLastHealth(String providerId);
}
