package com.sporekart.ai.providers.capability;

import java.util.List;

public interface CapabilityValidator {
    boolean validate(String providerId, ProviderCapability capability);
    List<ProviderCapability> getSupportedCapabilities(String providerId);
    List<ProviderCapability> getMissingCapabilities(String providerId, List<ProviderCapability> required);
    boolean isFullySupported(String providerId, List<ProviderCapability> required);
}
