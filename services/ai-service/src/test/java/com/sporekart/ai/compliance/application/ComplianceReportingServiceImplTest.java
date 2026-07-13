package com.sporekart.ai.compliance.application;

import com.sporekart.ai.compliance.domain.*;
import com.sporekart.ai.compliance.infrastructure.persistence.ComplianceAssessmentRepository;
import com.sporekart.ai.compliance.infrastructure.persistence.ComplianceReportRepository;
import com.sporekart.ai.compliance.infrastructure.persistence.ComplianceViolationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ComplianceReportingServiceImplTest {

    @Mock private ComplianceReportRepository reportRepository;
    @Mock private ComplianceViolationRepository violationRepository;
    @Mock private ComplianceAssessmentRepository assessmentRepository;
    @Mock private ComplianceAuditService complianceAuditService;

    private ComplianceReportingServiceImpl reportingService;

    @BeforeEach
    void setUp() {
        reportingService = new ComplianceReportingServiceImpl(
            reportRepository, violationRepository, assessmentRepository, complianceAuditService);
    }

    @Test
    void testGenerateReport() {
        UUID assessmentId = UUID.randomUUID();
        UUID generatedBy = UUID.randomUUID();
        UUID frameworkId = UUID.randomUUID();

        ComplianceAssessment assessment = new ComplianceAssessment(
            assessmentId, frameworkId, "module", "action",
            AssessmentStatus.COMPLETED, ComplianceStatus.PASSED, Map.of(),
            null, Instant.now(), Instant.now()
        );
        when(assessmentRepository.findById(assessmentId)).thenReturn(Optional.of(assessment));
        when(violationRepository.findByAssessmentId(assessmentId)).thenReturn(List.of());
        when(reportRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        ComplianceReport report = reportingService.generateReport(assessmentId, generatedBy);

        assertNotNull(report);
        assertEquals(frameworkId, report.frameworkId());
        assertEquals(ComplianceStatus.PASSED, report.overallStatus());
        assertTrue(report.findings().isEmpty());
        assertTrue(report.violations().isEmpty());
        assertEquals(generatedBy, report.generatedBy());
        verify(complianceAuditService).recordAudit(
            eq("REPORT_GENERATE"), eq("REPORT"), any(), eq(generatedBy), anyMap(), eq("reporting")
        );
    }

    @Test
    void testGenerateReportWithViolations() {
        UUID assessmentId = UUID.randomUUID();
        UUID generatedBy = UUID.randomUUID();
        UUID frameworkId = UUID.randomUUID();

        ComplianceAssessment assessment = new ComplianceAssessment(
            assessmentId, frameworkId, "module", "action",
            AssessmentStatus.COMPLETED, ComplianceStatus.FAILED, Map.of(),
            null, Instant.now(), Instant.now()
        );
        when(assessmentRepository.findById(assessmentId)).thenReturn(Optional.of(assessment));

        List<ComplianceViolation> violations = List.of(new ComplianceViolation(
            UUID.randomUUID(), UUID.randomUUID(), assessmentId, "module",
            ViolationSeverity.MAJOR, "Test violation", Map.of(), false, Instant.now(), null
        ));
        when(violationRepository.findByAssessmentId(assessmentId)).thenReturn(violations);
        when(reportRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        ComplianceReport report = reportingService.generateReport(assessmentId, generatedBy);

        assertNotNull(report);
        assertEquals(1, report.violations().size());
        assertFalse(report.findings().isEmpty());
    }

    @Test
    void testGenerateReportThrowsWhenAssessmentNotFound() {
        UUID assessmentId = UUID.randomUUID();
        when(assessmentRepository.findById(assessmentId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
            reportingService.generateReport(assessmentId, UUID.randomUUID()));
    }

    @Test
    void testGetReportsByFramework() {
        UUID frameworkId = UUID.randomUUID();
        List<ComplianceReport> reports = List.of(
            new ComplianceReport(UUID.randomUUID(), frameworkId, "Report1", ComplianceStatus.PASSED,
                List.of(), List.of(), Map.of(), Instant.now(), UUID.randomUUID())
        );
        when(reportRepository.findByFrameworkId(frameworkId)).thenReturn(reports);

        List<ComplianceReport> result = reportingService.getReportsByFramework(frameworkId);

        assertEquals(1, result.size());
    }

    @Test
    void testGetReport() {
        UUID reportId = UUID.randomUUID();
        ComplianceReport report = new ComplianceReport(
            reportId, UUID.randomUUID(), "Report", ComplianceStatus.PASSED,
            List.of(), List.of(), Map.of(), Instant.now(), UUID.randomUUID()
        );
        when(reportRepository.findById(reportId)).thenReturn(Optional.of(report));

        Optional<ComplianceReport> result = reportingService.getReport(reportId);

        assertTrue(result.isPresent());
        assertEquals(reportId, result.get().id());
    }
}
