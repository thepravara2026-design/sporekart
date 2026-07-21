package com.sporekart.ai.providers.registry;

import com.sporekart.ai.providers.ProviderType;
import com.sporekart.ai.providers.registry.catalog.ProviderCatalogEntry;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ProviderDiscoveryEngine {
    List<ProviderCatalogEntry> discoverAll();
    List<ProviderCatalogEntry> discoverByType(ProviderType type);
    List<ProviderCatalogEntry> discoverByCapability(String capability);
    List<ProviderCatalogEntry> discoverByRegion(String region);
    Optional<ProviderCatalogEntry> discoverById(String providerId);
    List<ProviderCatalogEntry> search(String query);
    List<ProviderCatalogEntry> filter(Map<String, String> filters);
    List<ProviderCatalogEntry> rank(List<ProviderCatalogEntry> entries);
    void refresh();
}
