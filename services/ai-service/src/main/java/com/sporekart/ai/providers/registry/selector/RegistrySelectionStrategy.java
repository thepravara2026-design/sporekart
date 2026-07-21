package com.sporekart.ai.providers.registry.selector;

import com.sporekart.ai.providers.registry.catalog.ProviderCatalogEntry;

import java.util.List;
import java.util.Optional;

public interface RegistrySelectionStrategy {
    Optional<ProviderCatalogEntry> select(List<ProviderCatalogEntry> candidates);
    String strategyName();
    int priority();
}
