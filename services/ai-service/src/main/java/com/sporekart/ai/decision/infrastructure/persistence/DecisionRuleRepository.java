package com.sporekart.ai.decision.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DecisionRuleRepository extends JpaRepository<DecisionRuleEntity, UUID> {

    Optional<DecisionRuleEntity> findByIdAndIsDeletedFalse(UUID id);

    List<DecisionRuleEntity> findByIsDeletedFalse();

    List<DecisionRuleEntity> findByActionAndIsDeletedFalse(String action);
}
