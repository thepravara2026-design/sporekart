package com.sporekart.ai.providers.registry.discovery;

import com.sporekart.ai.providers.registry.catalog.ProviderCatalogEntry;

import java.util.List;

public interface RegionDiscovery extends DiscoverySource {
    List<ProviderCatalogEntry> discoverByRegion(String region);
    List<String> discoverRegions();
    List<ProviderCatalogEntry> discoverByLatency(String region);
    List<String> discoverOptimalRegion(String providerId);
}
