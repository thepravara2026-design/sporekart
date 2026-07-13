package com.sporekart.ai.risk.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RiskAssessmentRepository extends JpaRepository<RiskAssessmentEntity, UUID> {
    List<RiskAssessmentEntity> findByModule(String module);
    List<RiskAssessmentEntity> findByStatus(String status);
    List<RiskAssessmentEntity> findByModuleAndStatus(String module, String status);
}
