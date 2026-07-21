package com.sporekart.ai.providers.factory;

import com.sporekart.ai.providers.AIProvider;
import com.sporekart.ai.providers.ProviderConfiguration;
import com.sporekart.ai.providers.ProviderType;

import java.util.Optional;

public interface ProviderFactory {
    String factoryId();
    ProviderType providerType();
    AIProvider create(ProviderConfiguration configuration);
    AIProvider createWithDefaults(String providerId);
    boolean supports(ProviderType type);
    boolean supportsConfiguration(ProviderConfiguration configuration);
    Optional<String> validate(ProviderConfiguration configuration);
}
