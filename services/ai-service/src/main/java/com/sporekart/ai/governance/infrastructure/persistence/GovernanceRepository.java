package com.sporekart.ai.governance.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface GovernanceRepository extends JpaRepository<GovernanceEntity, UUID> {

    Optional<GovernanceEntity> findByIdAndIsDeletedFalse(UUID id);

    List<GovernanceEntity> findByIsDeletedFalse();

    List<GovernanceEntity> findByScopeAndIsDeletedFalse(String scope);

    List<GovernanceEntity> findByStatusAndIsDeletedFalse(String status);
}
