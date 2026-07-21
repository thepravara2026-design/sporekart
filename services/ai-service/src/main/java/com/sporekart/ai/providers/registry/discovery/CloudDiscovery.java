package com.sporekart.ai.providers.registry.discovery;

import com.sporekart.ai.providers.registry.catalog.ProviderCatalogEntry;

import java.util.List;

public interface CloudDiscovery extends DiscoverySource {
    List<ProviderCatalogEntry> discoverFromCloud();
    List<ProviderCatalogEntry> discoverFromAws();
    List<ProviderCatalogEntry> discoverFromAzure();
    List<ProviderCatalogEntry> discoverFromGcp();
}
