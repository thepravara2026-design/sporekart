package com.sporekart.ai.decision.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DecisionExplanationRepository extends JpaRepository<DecisionExplanationEntity, UUID> {

    List<DecisionExplanationEntity> findByDecisionIdAndIsDeletedFalse(UUID decisionId);
}
