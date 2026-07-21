package com.sporekart.ai.providers.registry.discovery;

import com.sporekart.ai.providers.capability.ProviderCapability;
import com.sporekart.ai.providers.registry.catalog.ProviderCatalogEntry;

import java.util.List;
import java.util.Map;

public interface CapabilityDiscovery extends DiscoverySource {
    List<ProviderCatalogEntry> discoverByCapability(ProviderCapability capability);
    Map<ProviderCapability, List<ProviderCatalogEntry>> discoverAllByCapability();
    List<ProviderCapability> discoverCapabilities(String providerId);
}
