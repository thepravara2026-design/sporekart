package com.sporekart.ai.compliance.infrastructure.persistence;

import com.sporekart.ai.compliance.domain.ComplianceAssessment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ComplianceAssessmentRepository extends JpaRepository<ComplianceAssessment, UUID> {
}
