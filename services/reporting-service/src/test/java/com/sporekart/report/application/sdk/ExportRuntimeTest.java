package com.sporekart.report.application.sdk;

import com.sporekart.report.domain.model.*;
import com.sporekart.report.domain.repository.ReportRepositoryPort;
import com.sporekart.report.infrastructure.export.MockExportService;
import com.sporekart.report.infrastructure.persistence.InMemoryReportRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class ExportRuntimeTest {
    private ExportRuntime runtime;
    private ReportRepositoryPort repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryReportRepository();
        MockExportService exportService = new MockExportService(repository);
        runtime = new ExportRuntime(exportService);
    }

    private String seedReport() {
        Report report = Report.create("Test Export", "Desc", ReportType.WEEKLY,
            ReportCategory.REVENUE, "Finance", "Summary", "HEALTHY",
            List.of(), List.of(), Map.of(), Map.of(), "t1");
        return repository.saveReport(report).id();
    }

    @Test
    void shouldExportReport() {
        String reportId = seedReport();
        ReportExport export = runtime.exportReport(reportId, ExportFormat.PDF);
        assertNotNull(export.id());
        assertEquals("COMPLETED", export.status());
    }

    @Test
    void shouldGetExportStatus() {
        String reportId = seedReport();
        ReportExport export = runtime.exportReport(reportId, ExportFormat.CSV);
        ReportExport status = runtime.getExportStatus(export.id());
        assertEquals(export.id(), status.id());
    }

    @Test
    void shouldGetExportHistory() {
        String reportId = seedReport();
        runtime.exportReport(reportId, ExportFormat.PDF);
        runtime.exportReport(reportId, ExportFormat.CSV);
        List<ReportExport> history = runtime.getExportHistory(reportId);
        assertEquals(2, history.size());
    }

    @Test
    void shouldGetAllExports() {
        String reportId = seedReport();
        runtime.exportReport(reportId, ExportFormat.PDF);
        runtime.exportReport(reportId, ExportFormat.JSON);
        assertTrue(runtime.getAllExports().size() >= 2);
    }
}
