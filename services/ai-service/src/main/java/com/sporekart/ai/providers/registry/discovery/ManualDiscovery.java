package com.sporekart.ai.providers.registry.discovery;

import com.sporekart.ai.providers.registry.catalog.ProviderCatalogEntry;

public interface ManualDiscovery extends DiscoverySource {
    ProviderCatalogEntry registerManually(ProviderCatalogEntry entry);
    void unregisterManually(String providerId);
}
