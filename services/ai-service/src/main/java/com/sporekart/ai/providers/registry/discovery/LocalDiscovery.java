package com.sporekart.ai.providers.registry.discovery;

import com.sporekart.ai.providers.registry.catalog.ProviderCatalogEntry;

import java.util.List;

public interface LocalDiscovery extends DiscoverySource {
    List<ProviderCatalogEntry> discoverLocal();
    List<ProviderCatalogEntry> discoverFromClasspath();
    List<ProviderCatalogEntry> discoverFromFileSystem(String path);
}
