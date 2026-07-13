package com.sporekart.ai.compliance.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface ComplianceExceptionRepository extends JpaRepository<ComplianceExceptionEntity, UUID> {

    List<ComplianceExceptionEntity> findByRuleId(UUID ruleId);

    List<ComplianceExceptionEntity> findByStatus(String status);

    List<ComplianceExceptionEntity> findByAssessmentId(UUID assessmentId);
}
