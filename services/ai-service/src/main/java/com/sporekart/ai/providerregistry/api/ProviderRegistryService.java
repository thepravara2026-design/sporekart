package com.sporekart.ai.providerregistry.api;

import com.sporekart.ai.providerregistry.domain.ProviderCapability;
import com.sporekart.ai.providerregistry.domain.ProviderRegistryEntry;
import com.sporekart.ai.providerregistry.domain.ProviderStatus;

import java.util.List;
import java.util.Optional;

public interface ProviderRegistryService {

    ProviderRegistryEntry registerProvider(ProviderRegistryEntry entry);

    ProviderRegistryEntry updateProvider(ProviderRegistryEntry entry);

    Optional<ProviderRegistryEntry> getProvider(String providerId);

    List<ProviderRegistryEntry> listProviders();

    List<ProviderRegistryEntry> searchProviders(String name);

    List<ProviderRegistryEntry> getProvidersByStatus(ProviderStatus status);

    List<ProviderRegistryEntry> getProvidersByCapability(ProviderCapability capability);
}
