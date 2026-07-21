package com.sporekart.ai.provider.registry;

import com.sporekart.ai.provider.interfaces.AIProvider;
import com.sporekart.ai.provider.models.ProviderHealth;
import com.sporekart.ai.provider.models.ProviderInfo;

import java.util.List;
import java.util.Optional;

public interface ProviderRegistry {
    void register(AIProvider provider);
    void unregister(String providerId);
    Optional<AIProvider> lookup(String providerId);
    Optional<AIProvider> lookupByType(String providerType);
    List<AIProvider> getAllProviders();
    List<AIProvider> getAvailableProviders();
    List<AIProvider> getHealthyProviders();
    List<ProviderInfo> getProviderMetadata();
    Optional<ProviderInfo> getProviderMetadata(String providerId);
    void updateHealth(String providerId, ProviderHealth health);
    boolean isRegistered(String providerId);
    int providerCount();
    void clear();
}
