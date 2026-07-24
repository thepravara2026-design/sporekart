package com.sporekart.risk.infrastructure.persistence;

import com.sporekart.risk.domain.model.RiskLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RiskJpaRepository extends JpaRepository<RiskAssessmentEntity, String> {
    List<RiskAssessmentEntity> findByEntityTypeAndEntityId(String entityType, String entityId);
    List<RiskAssessmentEntity> findByRiskLevel(RiskLevel riskLevel);
}
