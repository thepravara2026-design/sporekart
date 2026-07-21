package com.sporekart.ai.providers.registry.capability;

import com.sporekart.ai.providers.capability.ProviderCapability;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface RegistryCapabilityIndex {
    void index(String providerId, List<ProviderCapability> capabilities);
    Set<ProviderCapability> getCapabilities(String providerId);
    Map<ProviderCapability, List<String>> getProvidersByCapability();
    List<String> findProvidersWithAll(ProviderCapability... capabilities);
    List<String> findProvidersWithAny(ProviderCapability... capabilities);
    boolean hasCapability(String providerId, ProviderCapability capability);
    void removeIndex(String providerId);
}
