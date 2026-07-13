package com.sporekart.ai.governance.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface GovernanceRegistryRepository extends JpaRepository<GovernanceRegistryEntity, UUID> {

    Optional<GovernanceRegistryEntity> findByIdAndIsDeletedFalse(UUID id);

    List<GovernanceRegistryEntity> findByIsDeletedFalse();

    List<GovernanceRegistryEntity> findByScopeAndIsDeletedFalse(String scope);

    List<GovernanceRegistryEntity> findByModuleAndIsDeletedFalse(String module);

    Optional<GovernanceRegistryEntity> findByModuleAndEndpointAndIsDeletedFalse(String module, String endpoint);
}
