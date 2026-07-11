package com.sporekart.ai.provider.application;

import com.sporekart.ai.core.api.AIProvider;
import com.sporekart.ai.core.domain.AiProviderType;
import com.sporekart.ai.provider.api.ProviderPort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProviderFactoryImpl {

    private final ProviderRegistryImpl registry;

    public ProviderFactoryImpl(ProviderRegistryImpl registry) {
        this.registry = registry;
    }

    public Optional<AIProvider> createProvider(AiProviderType providerType) {
        Optional<ProviderPort> port = registry.findByType(providerType.name());
        if (port.isPresent() && port.get() instanceof AIProvider aiProvider) {
            return Optional.of(aiProvider);
        }
        return Optional.empty();
    }

    public Optional<AIProvider> createProvider(String providerType) {
        try {
            AiProviderType type = AiProviderType.valueOf(providerType.toUpperCase());
            return createProvider(type);
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    public AIProvider getOrCreateProvider(AiProviderType providerType) {
        return createProvider(providerType)
                .orElseThrow(() -> new IllegalStateException("Provider not available: " + providerType));
    }
}
