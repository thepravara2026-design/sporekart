package com.sporekart.ai.policy.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PolicyRegistryRepository extends JpaRepository<PolicyRegistryEntity, UUID> {

    Optional<PolicyRegistryEntity> findByIdAndIsDeletedFalse(UUID id);

    List<PolicyRegistryEntity> findByIsDeletedFalse();

    List<PolicyRegistryEntity> findByModuleAndIsDeletedFalse(String module);

    List<PolicyRegistryEntity> findByTypeAndIsDeletedFalse(String type);

    List<PolicyRegistryEntity> findByScopeAndIsDeletedFalse(String scope);

    Optional<PolicyRegistryEntity> findByModuleAndTypeAndIsDeletedFalse(String module, String type);
}
