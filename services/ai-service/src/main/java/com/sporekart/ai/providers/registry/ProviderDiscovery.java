package com.sporekart.ai.providers.registry;

import com.sporekart.ai.providers.AIProvider;
import com.sporekart.ai.providers.capability.ProviderCapability;
import com.sporekart.ai.providers.metadata.ProviderMetadata;

import java.util.List;
import java.util.Optional;

public interface ProviderDiscovery {
    List<AIProvider> discover();
    List<AIProvider> discoverByCapability(ProviderCapability capability);
    List<AIProvider> discoverByMetadata(ProviderMetadata metadata);
    Optional<AIProvider> discoverById(String providerId);
    List<ProviderMetadata> discoverAvailableProviders();
    boolean isDiscoverable(String providerId);
}
