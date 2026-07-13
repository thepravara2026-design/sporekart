package com.sporekart.ai.decision.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DecisionRepository extends JpaRepository<DecisionEntity, UUID> {

    Optional<DecisionEntity> findByIdAndIsDeletedFalse(UUID id);

    List<DecisionEntity> findByRequestIdAndIsDeletedFalse(UUID requestId);

    List<DecisionEntity> findByActionAndIsDeletedFalse(String action);

    List<DecisionEntity> findByStatusAndIsDeletedFalse(String status);
}
