package com.sporekart.ai.providers.registry.discovery;

import com.sporekart.ai.providers.registry.catalog.ProviderCatalogEntry;

import java.util.List;

public interface DiscoverySource {
    String sourceName();
    List<ProviderCatalogEntry> discover();
    boolean isAvailable();
}
