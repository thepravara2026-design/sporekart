package com.sporekart.ai.compliance.application;

import com.sporekart.ai.compliance.domain.ComplianceReport;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ComplianceReportingService {
    ComplianceReport generateReport(UUID assessmentId, UUID generatedBy);
    List<ComplianceReport> getReportsByFramework(UUID frameworkId);
    Optional<ComplianceReport> getReport(UUID reportId);
}
