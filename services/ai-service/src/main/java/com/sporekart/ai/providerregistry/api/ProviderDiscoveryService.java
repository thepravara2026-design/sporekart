package com.sporekart.ai.providerregistry.api;

import com.sporekart.ai.providerregistry.domain.FallbackChain;
import com.sporekart.ai.providerregistry.domain.ProviderCapability;
import com.sporekart.ai.providerregistry.domain.ProviderRegistryEntry;

import java.util.List;
import java.util.Optional;

public interface ProviderDiscoveryService {

    List<ProviderRegistryEntry> discoverByCapability(ProviderCapability capability);

    FallbackChain getFallbackChain(ProviderCapability capability);

    Optional<ProviderRegistryEntry> recommendProvider(ProviderCapability capability);
}
