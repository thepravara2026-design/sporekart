package com.sporekart.ai.analytics.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sporekart.ai.analytics.domain.GovernanceExport;
import com.sporekart.ai.analytics.domain.ReportFormat;
import com.sporekart.ai.analytics.infrastructure.persistence.GovernanceExportEntity;
import com.sporekart.ai.analytics.infrastructure.persistence.GovernanceExportRepository;
import com.sporekart.ai.analytics.infrastructure.persistence.GovernanceReportEntity;
import com.sporekart.ai.analytics.infrastructure.persistence.GovernanceReportRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExportServiceImplTest {

    @Mock private GovernanceExportRepository exportRepository;
    @Mock private GovernanceReportRepository reportRepository;
    private ObjectMapper objectMapper;
    private ExportServiceImpl service;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        service = new ExportServiceImpl(exportRepository, reportRepository, objectMapper);
    }

    @Test
    void testExportReport() {
        var report = new GovernanceReportEntity();
        report.setId(UUID.randomUUID());
        report.setTitle("Test Report");
        report.setType("COMPLIANCE_REPORT");
        report.setData("{}");
        report.setSummary("{}");
        report.setGeneratedAt(OffsetDateTime.now());
        report.setCreatedAt(OffsetDateTime.now());

        var exportEntity = new GovernanceExportEntity();
        exportEntity.setId(UUID.randomUUID());
        exportEntity.setReportId(report.getId());
        exportEntity.setFormat("JSON");
        exportEntity.setFileName("Test_Report.json");
        exportEntity.setFileSize(0L);
        exportEntity.setMetadata("{}");
        exportEntity.setExportedAt(OffsetDateTime.now());
        exportEntity.setExportedBy(UUID.randomUUID());
        exportEntity.setCreatedAt(OffsetDateTime.now());

        when(reportRepository.findById(report.getId())).thenReturn(Optional.of(report));
        when(exportRepository.save(any(GovernanceExportEntity.class))).thenReturn(exportEntity);

        GovernanceExport export = service.exportReport(report.getId(), ReportFormat.JSON);

        assertNotNull(export);
        assertEquals(report.getId(), export.reportId());
        assertEquals(ReportFormat.JSON, export.format());
    }

    @Test
    void testGetExport() {
        var entity = new GovernanceExportEntity();
        entity.setId(UUID.randomUUID());
        entity.setReportId(UUID.randomUUID());
        entity.setFormat("CSV");
        entity.setFileName("report.csv");
        entity.setFileSize(1024L);
        entity.setMetadata("{}");
        entity.setExportedAt(OffsetDateTime.now());
        entity.setExportedBy(UUID.randomUUID());
        entity.setCreatedAt(OffsetDateTime.now());

        when(exportRepository.findById(entity.getId())).thenReturn(Optional.of(entity));

        GovernanceExport export = service.getExport(entity.getId());

        assertNotNull(export);
        assertEquals(entity.getId(), export.id());
    }
}
