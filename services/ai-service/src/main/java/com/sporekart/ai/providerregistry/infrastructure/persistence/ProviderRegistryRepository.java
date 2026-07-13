package com.sporekart.ai.providerregistry.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sporekart.ai.providerregistry.domain.ProviderCapability;
import com.sporekart.ai.providerregistry.domain.ProviderStatus;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProviderRegistryRepository extends JpaRepository<ProviderRegistryEntity, String> {

    List<ProviderRegistryEntity> findByProviderNameContainingIgnoreCase(String name);

    List<ProviderRegistryEntity> findByStatus(ProviderStatus status);

    List<ProviderRegistryEntity> findByCapabilitiesContaining(ProviderCapability capability);
}
