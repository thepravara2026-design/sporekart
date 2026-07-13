package com.sporekart.ai.compliance.infrastructure.persistence;

import com.sporekart.ai.compliance.domain.ComplianceReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ComplianceReportRepository extends JpaRepository<ComplianceReport, UUID> {
    List<ComplianceReport> findByFrameworkId(UUID frameworkId);
}
