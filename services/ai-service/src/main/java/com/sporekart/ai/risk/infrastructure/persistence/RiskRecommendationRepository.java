package com.sporekart.ai.risk.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RiskRecommendationRepository extends JpaRepository<RiskRecommendationEntity, UUID> {
    List<RiskRecommendationEntity> findByAssessmentId(UUID assessmentId);
    List<RiskRecommendationEntity> findByType(String type);
}
