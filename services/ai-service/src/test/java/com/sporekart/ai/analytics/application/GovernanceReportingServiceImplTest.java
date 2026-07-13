package com.sporekart.ai.analytics.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sporekart.ai.analytics.api.AnalyticsAuditService;
import com.sporekart.ai.analytics.domain.GovernanceReport;
import com.sporekart.ai.analytics.domain.ReportType;
import com.sporekart.ai.analytics.infrastructure.persistence.GovernanceReportEntity;
import com.sporekart.ai.analytics.infrastructure.persistence.GovernanceReportRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GovernanceReportingServiceImplTest {

    @Mock private GovernanceReportRepository repository;
    @Mock private AnalyticsAuditService auditService;
    private ObjectMapper objectMapper;

    private GovernanceReportingServiceImpl service;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        service = new GovernanceReportingServiceImpl(repository, auditService, objectMapper);
    }

    @Test
    void testGenerateReport() {
        var entity = createEntity();
        when(repository.save(any(GovernanceReportEntity.class))).thenReturn(entity);

        GovernanceReport report = service.generateReport(ReportType.COMPLIANCE_REPORT, Map.of("param1", "value1"));

        assertNotNull(report);
        assertEquals(ReportType.COMPLIANCE_REPORT, report.type());
        verify(auditService).recordAudit(eq("REPORT_GENERATED"), eq("GovernanceReport"), any(), isNull(), any(), eq("system"));
    }

    @Test
    void testGetReport() {
        var entity = createEntity();
        when(repository.findById(entity.getId())).thenReturn(Optional.of(entity));

        GovernanceReport report = service.getReport(entity.getId());

        assertNotNull(report);
        assertEquals(entity.getId(), report.id());
    }

    @Test
    void testGetReportsByType() {
        var entity = createEntity();
        when(repository.findByType("COMPLIANCE_REPORT")).thenReturn(List.of(entity));

        List<GovernanceReport> reports = service.getReportsByType(ReportType.COMPLIANCE_REPORT);

        assertEquals(1, reports.size());
        assertEquals(ReportType.COMPLIANCE_REPORT, reports.get(0).type());
    }

    private GovernanceReportEntity createEntity() {
        var entity = new GovernanceReportEntity();
        entity.setId(UUID.randomUUID());
        entity.setType("COMPLIANCE_REPORT");
        entity.setTitle("Test Report");
        entity.setDescription("Desc");
        entity.setData("{}");
        entity.setSummary("{}");
        entity.setGeneratedAt(OffsetDateTime.now());
        entity.setGeneratedBy(UUID.randomUUID());
        entity.setCreatedAt(OffsetDateTime.now());
        return entity;
    }
}
