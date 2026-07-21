package com.sporekart.ai.providers.capability;

import com.sporekart.ai.providers.AIProvider;

import java.util.List;

public interface CapabilityRegistry {
    void registerCapability(String providerId, ProviderCapability capability);
    void unregisterCapability(String providerId, ProviderCapability capability);
    List<ProviderCapability> getCapabilities(String providerId);
    boolean hasCapability(String providerId, ProviderCapability capability);
    List<AIProvider> findProvidersByCapability(ProviderCapability capability);
    List<AIProvider> findProvidersByAllCapabilities(List<ProviderCapability> capabilities);
    CapabilityProfile getProfile(String providerId);
}
