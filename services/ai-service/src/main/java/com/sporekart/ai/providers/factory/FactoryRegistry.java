package com.sporekart.ai.providers.factory;

import com.sporekart.ai.providers.ProviderConfiguration;
import com.sporekart.ai.providers.ProviderType;

import java.util.List;

public interface FactoryRegistry {
    void register(ProviderFactory factory);
    void unregister(String factoryId);
    ProviderFactory getFactory(ProviderType type);
    List<ProviderFactory> getAllFactories();
    List<ProviderType> getSupportedTypes();
    boolean hasFactory(ProviderType type);
    ProviderConfiguration resolveConfiguration(ProviderType type, java.util.Map<String, Object> params);
}
