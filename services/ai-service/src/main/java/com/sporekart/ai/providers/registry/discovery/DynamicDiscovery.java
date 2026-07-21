package com.sporekart.ai.providers.registry.discovery;

import com.sporekart.ai.providers.registry.catalog.ProviderCatalogEntry;

import java.util.List;

public interface DynamicDiscovery extends DiscoverySource {
    List<ProviderCatalogEntry> discoverDynamic();
    void watch(String providerId);
    void unwatch(String providerId);
    boolean isWatching(String providerId);
}
