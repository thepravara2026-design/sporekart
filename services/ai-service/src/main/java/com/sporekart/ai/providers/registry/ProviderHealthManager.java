package com.sporekart.ai.providers.registry;

import com.sporekart.ai.providers.ProviderHealth;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ProviderHealthManager {
    void reportHealth(String providerId, ProviderHealth health);
    Optional<ProviderHealth> getHealth(String providerId);
    Map<String, ProviderHealth> getAllHealth();
    List<String> getHealthyProviders();
    List<String> getUnhealthyProviders();
    List<String> getDegradedProviders();
    ProviderHealth getAggregateHealth();
}
