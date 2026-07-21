package com.sporekart.ai.providers.capability;

import java.util.List;
import java.util.Map;

public record CapabilityProfile(
    String providerId,
    List<ProviderCapability> capabilities,
    Map<ProviderCapability, Integer> capabilityVersion,
    Map<ProviderCapability, String> capabilityNotes
) {
    public boolean hasCapability(ProviderCapability capability) {
        return capabilities.contains(capability);
    }

    public boolean hasAllCapabilities(List<ProviderCapability> required) {
        return capabilities.containsAll(required);
    }

    public int capabilityVersion(ProviderCapability capability) {
        return capabilityVersion != null ? capabilityVersion.getOrDefault(capability, 1) : 1;
    }
}
