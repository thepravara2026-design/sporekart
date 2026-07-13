package com.sporekart.ai.providerregistry.application;

import com.sporekart.ai.providerregistry.api.ProviderRegistryService;
import com.sporekart.ai.providerregistry.domain.ProviderCapability;
import com.sporekart.ai.providerregistry.domain.ProviderRegistryEntry;
import com.sporekart.ai.providerregistry.domain.ProviderStatus;
import com.sporekart.ai.providerregistry.infrastructure.persistence.ProviderModelInfoEmbeddable;
import com.sporekart.ai.providerregistry.infrastructure.persistence.ProviderRegistryEntity;
import com.sporekart.ai.providerregistry.infrastructure.persistence.ProviderRegistryRepository;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProviderRegistryServiceImpl implements ProviderRegistryService {

    private final ProviderRegistryRepository repository;

    public ProviderRegistryServiceImpl(ProviderRegistryRepository repository) {
        this.repository = repository;
    }

    @Override
    public ProviderRegistryEntry registerProvider(ProviderRegistryEntry entry) {
        String providerId = entry.getProviderId() != null && !entry.getProviderId().isBlank()
                ? entry.getProviderId()
                : UUID.randomUUID().toString();
        Instant now = Instant.now();
        ProviderRegistryEntry toSave = new ProviderRegistryEntry(
                providerId,
                entry.getProviderName(),
                entry.getProviderType(),
                entry.getVersion(),
                entry.getStatus() != null ? entry.getStatus() : ProviderStatus.ACTIVE,
                entry.getPriority(),
                entry.getSupportedModels(),
                entry.getCapabilities(),
                entry.getHealthStatus(),
                entry.getMetadata(),
                now,
                now,
                entry.getDeprecatedAt());
        return repository.save(ProviderRegistryEntity.fromDomain(toSave)).toDomain();
    }

    @Override
    public ProviderRegistryEntry updateProvider(ProviderRegistryEntry entry) {
        ProviderRegistryEntity existing = repository.findById(entry.getProviderId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Provider not found: " + entry.getProviderId()));

        existing.setProviderName(entry.getProviderName());
        existing.setProviderType(entry.getProviderType());
        existing.setVersion(entry.getVersion());
        existing.setPriority(entry.getPriority());
        existing.setCapabilities(entry.getCapabilities() != null
                ? new ArrayList<>(entry.getCapabilities()) : new ArrayList<>());
        existing.setMetadata(entry.getMetadata() != null
                ? new HashMap<>(entry.getMetadata()) : new HashMap<>());
        existing.setSupportedModels(entry.getSupportedModels() != null
                ? entry.getSupportedModels().stream()
                    .map(m -> new ProviderModelInfoEmbeddable(
                            m.modelId(), m.modelName(), m.contextWindow(), m.maxTokens(),
                            m.streamingSupported(), m.toolCallingSupported(),
                            m.embeddingsSupported(), m.imageSupported(), m.audioSupported()))
                    .collect(Collectors.toList())
                : new ArrayList<>());

        if (entry.getStatus() != null) {
            existing.setStatus(entry.getStatus());
            if (entry.getStatus() == ProviderStatus.DEPRECATED && existing.getDeprecatedAt() == null) {
                existing.setDeprecatedAt(Instant.now());
            }
        }
        existing.setUpdatedAt(Instant.now());
        return repository.save(existing).toDomain();
    }

    @Override
    public Optional<ProviderRegistryEntry> getProvider(String providerId) {
        return repository.findById(providerId).map(ProviderRegistryEntity::toDomain);
    }

    @Override
    public List<ProviderRegistryEntry> listProviders() {
        return repository.findAll().stream().map(ProviderRegistryEntity::toDomain).toList();
    }

    @Override
    public List<ProviderRegistryEntry> searchProviders(String name) {
        return repository.findByProviderNameContainingIgnoreCase(name).stream()
                .map(ProviderRegistryEntity::toDomain).toList();
    }

    @Override
    public List<ProviderRegistryEntry> getProvidersByStatus(ProviderStatus status) {
        return repository.findByStatus(status).stream()
                .map(ProviderRegistryEntity::toDomain).toList();
    }

    @Override
    public List<ProviderRegistryEntry> getProvidersByCapability(ProviderCapability capability) {
        return repository.findByCapabilitiesContaining(capability).stream()
                .map(ProviderRegistryEntity::toDomain).toList();
    }
}
