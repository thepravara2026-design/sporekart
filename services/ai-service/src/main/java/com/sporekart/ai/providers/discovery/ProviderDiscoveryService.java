package com.sporekart.ai.providers.discovery;

import com.sporekart.ai.providers.ProviderConfiguration;
import com.sporekart.ai.providers.ProviderType;

import java.util.List;
import java.util.Optional;

public interface ProviderDiscoveryService {
    List<ProviderConfiguration> discoverProviders();
    List<ProviderConfiguration> discoverByType(ProviderType type);
    Optional<ProviderConfiguration> discoverById(String providerId);
    List<ProviderType> getAvailableTypes();
    boolean isAvailable(ProviderType type);
    void refresh();
}
