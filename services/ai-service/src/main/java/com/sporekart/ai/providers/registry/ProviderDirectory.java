package com.sporekart.ai.providers.registry;

import com.sporekart.ai.providers.ProviderStatus;
import com.sporekart.ai.providers.ProviderType;
import com.sporekart.ai.providers.capability.ProviderCapability;
import com.sporekart.ai.providers.registry.catalog.ProviderCatalogEntry;

import java.util.List;
import java.util.Optional;

public interface ProviderDirectory {
    void register(String providerId, ProviderCatalogEntry entry);
    void unregister(String providerId);
    Optional<ProviderCatalogEntry> find(String providerId);
    List<ProviderCatalogEntry> findAll();
    List<ProviderCatalogEntry> findByType(ProviderType type);
    List<ProviderCatalogEntry> findByStatus(ProviderStatus status);
    List<ProviderCatalogEntry> findByCapability(ProviderCapability capability);
    boolean contains(String providerId);
}
