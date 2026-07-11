package com.sporekart.ai.provider.application;

import com.sporekart.ai.core.api.AIProvider;
import com.sporekart.ai.provider.api.ProviderPort;
import com.sporekart.ai.provider.api.ProviderRegistry;
import com.sporekart.ai.provider.domain.Provider;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class ProviderRegistryImpl implements ProviderRegistry {
    private final Map<String, ProviderPort> providers = new ConcurrentHashMap<>();
    private final Map<String, Provider> providerMetadata = new ConcurrentHashMap<>();

    @Override
    public void register(ProviderPort provider) {
        providers.put(provider.getClass().getSimpleName(), provider);
    }

    public void register(String name, ProviderPort provider) {
        providers.put(name, provider);
    }

    @Override
    public void unregister(String providerType) {
        providers.remove(providerType);
        providerMetadata.remove(providerType);
    }

    @Override
    public Optional<ProviderPort> findByType(String providerType) {
        return providers.values().stream()
                .filter(p -> p.supports(providerType))
                .findFirst();
    }

    @Override
    public List<ProviderPort> all() {
        return List.copyOf(providers.values());
    }

    @Override
    public List<Provider> getRegisteredProviders() {
        return List.copyOf(providerMetadata.values());
    }

    public void registerProviderMetadata(Provider provider) {
        providerMetadata.put(provider.providerType(), provider);
    }

    public Optional<AIProvider> findAIProvider(String providerType) {
        return providers.values().stream()
                .filter(p -> p.supports(providerType))
                .filter(p -> p instanceof AIProvider)
                .map(p -> (AIProvider) p)
                .findFirst();
    }

    public List<AIProvider> allAIProviders() {
        return providers.values().stream()
                .filter(p -> p instanceof AIProvider)
                .map(p -> (AIProvider) p)
                .collect(Collectors.toList());
    }

    public boolean isRegistered(String providerType) {
        return providers.values().stream().anyMatch(p -> p.supports(providerType));
    }
}
