package com.sporekart.ai.providers.registry.metadata;

import com.sporekart.ai.providers.metadata.ProviderMetadata;

import java.util.List;
import java.util.Optional;

public interface RegistryMetadataService {
    void indexMetadata(ProviderMetadata metadata);
    Optional<ProviderMetadata> findMetadata(String providerId);
    List<ProviderMetadata> searchMetadata(String query);
    void rebuildIndex();
    int metadataCount();
}
