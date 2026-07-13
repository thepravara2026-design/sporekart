package com.sporekart.ai.risk.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RiskScoreRepository extends JpaRepository<RiskScoreEntity, UUID> {
    List<RiskScoreEntity> findByAssessmentId(UUID assessmentId);
    List<RiskScoreEntity> findByRiskLevel(String riskLevel);
}
