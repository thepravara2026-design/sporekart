package com.sporekart.ai.compliance.infrastructure.persistence;

import com.sporekart.ai.compliance.domain.ComplianceViolation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ComplianceViolationRepository extends JpaRepository<ComplianceViolation, UUID> {
    List<ComplianceViolation> findByAssessmentId(UUID assessmentId);
}
