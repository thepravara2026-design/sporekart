package com.sporekart.ai.providerregistry.application;

import com.sporekart.ai.providerregistry.api.ProviderDiscoveryService;
import com.sporekart.ai.providerregistry.domain.FallbackChain;
import com.sporekart.ai.providerregistry.domain.ProviderCapability;
import com.sporekart.ai.providerregistry.domain.ProviderHealthStatus;
import com.sporekart.ai.providerregistry.domain.ProviderRegistryEntry;
import com.sporekart.ai.providerregistry.domain.ProviderStatus;
import com.sporekart.ai.providerregistry.domain.ProviderType;
import com.sporekart.ai.providerregistry.infrastructure.persistence.ProviderRegistryEntity;
import com.sporekart.ai.providerregistry.infrastructure.persistence.ProviderRegistryRepository;

import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class ProviderDiscoveryServiceImpl implements ProviderDiscoveryService {

    private final ProviderRegistryRepository repository;

    public ProviderDiscoveryServiceImpl(ProviderRegistryRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<ProviderRegistryEntry> discoverByCapability(ProviderCapability capability) {
        return buildCandidateChain(capability, null);
    }

    @Override
    public FallbackChain getFallbackChain(ProviderCapability capability) {
        return new FallbackChain(buildCandidateChain(capability, null));
    }

    public FallbackChain getFallbackChain(ProviderCapability capability, ProviderType preferredType) {
        return new FallbackChain(buildCandidateChain(capability, preferredType));
    }

    @Override
    public Optional<ProviderRegistryEntry> recommendProvider(ProviderCapability capability) {
        return buildCandidateChain(capability, null).stream().findFirst();
    }

    private List<ProviderRegistryEntry> buildCandidateChain(ProviderCapability capability,
                                                            ProviderType preferredType) {
        return repository.findByCapabilitiesContaining(capability).stream()
                .map(ProviderRegistryEntity::toDomain)
                .filter(e -> e.getStatus() == ProviderStatus.ACTIVE
                        || e.getStatus() == ProviderStatus.DEGRADED)
                .filter(e -> preferredType == null || e.getProviderType() == preferredType)
                .filter(e -> e.getHealthStatus() == null
                        || e.getHealthStatus() != ProviderHealthStatus.UNHEALTHY)
                .sorted(Comparator.comparingInt(ProviderRegistryEntry::getPriority))
                .toList();
    }
}
