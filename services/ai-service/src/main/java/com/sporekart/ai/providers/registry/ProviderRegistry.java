package com.sporekart.ai.providers.registry;

import com.sporekart.ai.providers.AIProvider;
import com.sporekart.ai.providers.ProviderStatus;
import com.sporekart.ai.providers.ProviderType;

import java.util.List;
import java.util.Optional;

public interface ProviderRegistry {
    void register(AIProvider provider);
    void unregister(String providerId);
    Optional<AIProvider> getProvider(String providerId);
    List<AIProvider> getAllProviders();
    List<AIProvider> getProvidersByType(ProviderType type);
    List<AIProvider> getActiveProviders();
    List<AIProvider> getProvidersByStatus(ProviderStatus status);
    boolean isRegistered(String providerId);
    int providerCount();
    void updateStatus(String providerId, ProviderStatus status);
}
