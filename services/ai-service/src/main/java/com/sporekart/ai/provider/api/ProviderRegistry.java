package com.sporekart.ai.provider.api;

import com.sporekart.ai.provider.domain.Provider;
import java.util.List;
import java.util.Optional;

public interface ProviderRegistry {
    void register(ProviderPort provider);
    void unregister(String providerType);
    Optional<ProviderPort> findByType(String providerType);
    List<ProviderPort> all();
    List<Provider> getRegisteredProviders();
}
