package com.sporekart.ai.providers.registry.discovery;

import com.sporekart.ai.providers.registry.catalog.ProviderCatalogEntry;

import java.util.List;

public interface EnvironmentDiscovery extends DiscoverySource {
    List<ProviderCatalogEntry> discoverFromEnvironment();
    List<ProviderCatalogEntry> discoverFromEnvironmentVariables();
    List<ProviderCatalogEntry> discoverFromSystemProperties();
}
