package com.sporekart.ai.providers.registry.discovery;

import com.sporekart.ai.providers.registry.catalog.ProviderCatalogEntry;

import java.util.List;

public interface VersionDiscovery extends DiscoverySource {
    List<ProviderCatalogEntry> discoverByVersion(String version);
    List<ProviderCatalogEntry> discoverLatestVersions();
    List<ProviderCatalogEntry> discoverStableVersions();
    List<ProviderCatalogEntry> discoverExperimentalVersions();
}
