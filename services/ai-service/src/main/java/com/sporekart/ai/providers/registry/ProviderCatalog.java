package com.sporekart.ai.providers.registry;

import com.sporekart.ai.providers.ProviderStatus;
import com.sporekart.ai.providers.ProviderType;
import com.sporekart.ai.providers.registry.catalog.ProviderCatalogEntry;

import java.util.List;
import java.util.Optional;

public interface ProviderCatalog {
    void addEntry(ProviderCatalogEntry entry);
    void removeEntry(String providerId);
    Optional<ProviderCatalogEntry> getEntry(String providerId);
    List<ProviderCatalogEntry> getAllEntries();
    List<ProviderCatalogEntry> searchByName(String name);
    List<ProviderCatalogEntry> searchByCapability(String capability);
    List<ProviderCatalogEntry> searchByModel(String model);
    List<ProviderCatalogEntry> searchByRegion(String region);
    List<ProviderCatalogEntry> getEntriesByStatus(ProviderStatus status);
    List<ProviderCatalogEntry> getEntriesByType(ProviderType type);
    int entryCount();
}
