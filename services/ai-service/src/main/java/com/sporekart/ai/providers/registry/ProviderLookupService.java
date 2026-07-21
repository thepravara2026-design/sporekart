package com.sporekart.ai.providers.registry;

import com.sporekart.ai.providers.ProviderType;
import com.sporekart.ai.providers.capability.ProviderCapability;
import com.sporekart.ai.providers.registry.catalog.ProviderCatalogEntry;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ProviderLookupService {
    Optional<ProviderCatalogEntry> lookup(String providerId);
    List<ProviderCatalogEntry> lookupByType(ProviderType type);
    List<ProviderCatalogEntry> lookupByCapability(ProviderCapability capability);
    List<ProviderCatalogEntry> lookupByModel(String model);
    List<ProviderCatalogEntry> lookupByRegion(String region);
    Optional<ProviderCatalogEntry> lookupBestMatch(Map<String, String> criteria);
    List<ProviderCatalogEntry> lookupAll();
}
