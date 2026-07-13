package com.sporekart.ai.decision.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DecisionRegistryRepository extends JpaRepository<DecisionRegistryEntity, UUID> {

    Optional<DecisionRegistryEntity> findByIdAndIsDeletedFalse(UUID id);

    List<DecisionRegistryEntity> findByIsDeletedFalse();

    List<DecisionRegistryEntity> findByModuleAndIsDeletedFalse(String module);
}
