package com.sporekart.ai.providers.metadata;

import java.util.List;
import java.util.Optional;

public interface MetadataRegistry {
    void registerMetadata(String providerId, ProviderMetadata metadata);
    void updateMetadata(String providerId, ProviderMetadata metadata);
    Optional<ProviderMetadata> getMetadata(String providerId);
    List<ProviderMetadata> getAllMetadata();
    List<ProviderMetadata> searchByName(String name);
    List<ProviderMetadata> searchByCapability(String capability);
    ProviderMetadata getDefaultMetadata();
}
