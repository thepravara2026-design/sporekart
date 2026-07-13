package com.sporekart.ai.providerregistry.application;

import com.sporekart.ai.providerregistry.api.ProviderHealthService;
import com.sporekart.ai.providerregistry.domain.ProviderHealthStatus;
import com.sporekart.ai.providerregistry.infrastructure.persistence.ProviderHealthEntity;
import com.sporekart.ai.providerregistry.infrastructure.persistence.ProviderHealthRepository;
import com.sporekart.ai.providerregistry.infrastructure.persistence.ProviderRegistryEntity;
import com.sporekart.ai.providerregistry.infrastructure.persistence.ProviderRegistryRepository;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class ProviderHealthServiceImpl implements ProviderHealthService {

    private final ProviderHealthRepository healthRepository;
    private final ProviderRegistryRepository registryRepository;

    public ProviderHealthServiceImpl(ProviderHealthRepository healthRepository,
                                     ProviderRegistryRepository registryRepository) {
        this.healthRepository = healthRepository;
        this.registryRepository = registryRepository;
    }

    @Override
    public void recordHealth(String providerId, ProviderHealthStatus status) {
        ProviderHealthEntity record = new ProviderHealthEntity(providerId, status, Instant.now());
        healthRepository.save(record);

        registryRepository.findById(providerId).ifPresent(entity -> {
            entity.setHealthStatus(status);
            registryRepository.save(entity);
        });
    }

    @Override
    public Optional<ProviderHealthStatus> getHealth(String providerId) {
        return healthRepository.findTop1ByProviderIdOrderByCheckedAtDesc(providerId)
                .stream().findFirst()
                .map(ProviderHealthEntity::getStatus);
    }

    @Override
    public List<ProviderHealthStatus> getHealthHistory(String providerId) {
        return healthRepository.findByProviderIdOrderByCheckedAtDesc(providerId).stream()
                .map(ProviderHealthEntity::getStatus).toList();
    }
}
