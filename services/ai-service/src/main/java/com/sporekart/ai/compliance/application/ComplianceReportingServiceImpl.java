package com.sporekart.ai.compliance.application;

import com.sporekart.ai.compliance.domain.ComplianceAssessment;
import com.sporekart.ai.compliance.domain.ComplianceFinding;
import com.sporekart.ai.compliance.domain.ComplianceReport;
import com.sporekart.ai.compliance.domain.ComplianceViolation;
import com.sporekart.ai.compliance.infrastructure.persistence.ComplianceAssessmentRepository;
import com.sporekart.ai.compliance.infrastructure.persistence.ComplianceReportRepository;
import com.sporekart.ai.compliance.infrastructure.persistence.ComplianceViolationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ComplianceReportingServiceImpl implements ComplianceReportingService {

    private final ComplianceReportRepository reportRepository;
    private final ComplianceViolationRepository violationRepository;
    private final ComplianceAssessmentRepository assessmentRepository;
    private final ComplianceAuditService complianceAuditService;

    @Override
    public ComplianceReport generateReport(UUID assessmentId, UUID generatedBy) {
        ComplianceAssessment assessment = assessmentRepository.findById(assessmentId)
            .orElseThrow(() -> new RuntimeException("Assessment not found: " + assessmentId));

        List<ComplianceViolation> violations = violationRepository.findByAssessmentId(assessmentId);

        List<ComplianceFinding> findings = violations.stream()
            .map(v -> new ComplianceFinding(
                UUID.randomUUID(),
                assessmentId,
                "FIND-" + v.id().toString().substring(0, 8),
                "Violation: " + v.description(),
                v.description(),
                v.severity(),
                assessment.result() != null ? assessment.result() : com.sporekart.ai.compliance.domain.ComplianceStatus.PENDING,
                List.of(v),
                "Review violation details",
                v.detectedAt(),
                null
            ))
            .toList();

        ComplianceReport report = new ComplianceReport(
            UUID.randomUUID(),
            assessment.frameworkId(),
            "Compliance Report for " + assessment.module() + "/" + assessment.action(),
            assessment.result() != null ? assessment.result() : com.sporekart.ai.compliance.domain.ComplianceStatus.PENDING,
            findings,
            violations,
            Map.of(
                "assessmentId", assessmentId.toString(),
                "module", assessment.module(),
                "action", assessment.action(),
                "totalViolations", violations.size(),
                "totalFindings", findings.size()
            ),
            Instant.now(),
            generatedBy
        );

        ComplianceReport saved = reportRepository.save(report);
        log.info("Generated report {} for assessment {}", saved.id(), assessmentId);

        complianceAuditService.recordAudit(
            "REPORT_GENERATE", "REPORT", saved.id(), generatedBy,
            Map.of("assessmentId", assessmentId.toString(), "violationsCount", violations.size()),
            "reporting"
        );

        return saved;
    }

    @Override
    public List<ComplianceReport> getReportsByFramework(UUID frameworkId) {
        return reportRepository.findByFrameworkId(frameworkId);
    }

    @Override
    public Optional<ComplianceReport> getReport(UUID reportId) {
        return reportRepository.findById(reportId);
    }
}
