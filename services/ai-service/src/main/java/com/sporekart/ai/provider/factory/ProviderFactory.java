package com.sporekart.ai.provider.factory;

import com.sporekart.ai.provider.configuration.EnvironmentConfigProvider;
import com.sporekart.ai.provider.configuration.ProviderConfig;
import com.sporekart.ai.provider.interfaces.AIProvider;
import com.sporekart.ai.provider.registry.ProviderRegistry;

import java.util.Optional;

public class ProviderFactory {
    private final ProviderRegistry registry;
    private final EnvironmentConfigProvider configProvider;

    public ProviderFactory(ProviderRegistry registry, EnvironmentConfigProvider configProvider) {
        this.registry = registry;
        this.configProvider = configProvider;
    }

    public Optional<AIProvider> createProvider(String providerType) {
        return registry.lookupByType(providerType);
    }

    public Optional<AIProvider> getOrCreateProvider(String providerType) {
        var existing = registry.lookupByType(providerType);
        if (existing.isPresent()) return existing;

        var config = configProvider.loadConfig(providerType);
        if (!config.enabled()) return Optional.empty();

        var provider = instantiateProvider(providerType, config);
        provider.ifPresent(registry::register);
        return provider;
    }

    public ProviderConfig getConfiguration(String providerType) {
        return configProvider.loadConfig(providerType);
    }

    public void refreshConfiguration(String providerType) {
        configProvider.refreshConfig(providerType);
    }

    public boolean isProviderEnabled(String providerType) {
        return configProvider.loadConfig(providerType).enabled();
    }

    private Optional<AIProvider> instantiateProvider(String providerType, ProviderConfig config) {
        return registry.lookupByType(providerType);
    }
}
