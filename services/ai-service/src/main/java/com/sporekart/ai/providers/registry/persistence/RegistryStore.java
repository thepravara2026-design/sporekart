package com.sporekart.ai.providers.registry.persistence;

import com.sporekart.ai.providers.registry.catalog.ProviderCatalogEntry;

import java.util.List;
import java.util.Optional;

public interface RegistryStore {
    void save(ProviderCatalogEntry entry);
    void update(ProviderCatalogEntry entry);
    void delete(String providerId);
    Optional<ProviderCatalogEntry> findById(String providerId);
    List<ProviderCatalogEntry> findAll();
    boolean exists(String providerId);
    long count();
}
