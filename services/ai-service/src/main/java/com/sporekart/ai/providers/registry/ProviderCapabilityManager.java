package com.sporekart.ai.providers.registry;

import com.sporekart.ai.providers.capability.ProviderCapability;

import java.util.List;
import java.util.Map;

public interface ProviderCapabilityManager {
    void registerCapabilities(String providerId, List<ProviderCapability> capabilities);
    List<ProviderCapability> getCapabilities(String providerId);
    boolean hasCapability(String providerId, ProviderCapability capability);
    Map<String, List<ProviderCapability>> getProvidersByCapability(ProviderCapability capability);
    void removeCapabilities(String providerId);
    int capabilityCount(String providerId);
}
