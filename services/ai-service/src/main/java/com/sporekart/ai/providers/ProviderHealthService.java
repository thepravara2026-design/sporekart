package com.sporekart.ai.providers;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ProviderHealthService {
    ProviderHealth check(String providerId);
    Map<String, ProviderHealth> checkAll();
    List<String> getHealthyProviders();
    List<String> getUnhealthyProviders();
    boolean isHealthy(String providerId);
    void recordHeartbeat(String providerId);
    void recordFailure(String providerId, String error);
    void recordRecovery(String providerId);
    Optional<ProviderHealth> getLastHealth(String providerId);
}
