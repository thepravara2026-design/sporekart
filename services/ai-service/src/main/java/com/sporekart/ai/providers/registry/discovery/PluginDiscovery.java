package com.sporekart.ai.providers.registry.discovery;

import com.sporekart.ai.providers.registry.catalog.ProviderCatalogEntry;

import java.util.List;

public interface PluginDiscovery extends DiscoverySource {
    List<ProviderCatalogEntry> discoverPlugins();
    void registerPlugin(String pluginId, ProviderCatalogEntry entry);
    void unregisterPlugin(String pluginId);
}
