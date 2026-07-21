package com.sporekart.ai.providers.registry.discovery;

import com.sporekart.ai.providers.registry.catalog.ProviderCatalogEntry;

import java.util.List;
import java.util.Map;

public interface AutomaticDiscovery extends DiscoverySource {
    List<ProviderCatalogEntry> discoverAutomatically();
    boolean isAutoDiscoverySupported();
    void configureAutoDiscovery(Map<String, String> config);
}
