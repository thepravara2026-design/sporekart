package com.sporekart.report.infrastructure.export;

import com.sporekart.report.domain.model.*;
import com.sporekart.report.domain.repository.ReportRepositoryPort;
import com.sporekart.report.infrastructure.persistence.InMemoryReportRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class MockExportServiceTest {
    private MockExportService exportService;
    private ReportRepositoryPort repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryReportRepository();
        exportService = new MockExportService(repository);
    }

    private String seedReport() {
        Report r = Report.create("Export Test", "Desc", ReportType.WEEKLY,
            ReportCategory.REVENUE, "Finance", "Summary", "HEALTHY",
            List.of(), List.of(), Map.of(), Map.of(), "t1");
        return repository.saveReport(r).id();
    }

    @Test
    void shouldExportReportAsPdf() {
        String reportId = seedReport();
        ReportExport export = exportService.exportReport(reportId, ExportFormat.PDF);
        assertNotNull(export.id());
        assertEquals(ExportFormat.PDF, export.format());
        assertEquals("COMPLETED", export.status());
        assertTrue(export.filename().endsWith(".pdf"));
    }

    @Test
    void shouldExportReportAsCsv() {
        String reportId = seedReport();
        ReportExport export = exportService.exportReport(reportId, ExportFormat.CSV);
        assertTrue(export.filename().endsWith(".csv"));
    }

    @Test
    void shouldExportReportAsJson() {
        String reportId = seedReport();
        ReportExport export = exportService.exportReport(reportId, ExportFormat.JSON);
        assertTrue(export.filename().endsWith(".json"));
    }

    @Test
    void shouldExportReportAsExcel() {
        String reportId = seedReport();
        ReportExport export = exportService.exportReport(reportId, ExportFormat.EXCEL);
        assertTrue(export.filename().endsWith(".xlsx"));
    }

    @Test
    void shouldGetExportStatus() {
        String reportId = seedReport();
        ReportExport export = exportService.exportReport(reportId, ExportFormat.PDF);
        ReportExport status = exportService.getExportStatus(export.id());
        assertEquals(export.id(), status.id());
    }

    @Test
    void shouldGetExportHistory() {
        String reportId = seedReport();
        exportService.exportReport(reportId, ExportFormat.PDF);
        exportService.exportReport(reportId, ExportFormat.CSV);
        List<ReportExport> history = exportService.getExportHistory(reportId);
        assertEquals(2, history.size());
    }

    @Test
    void shouldGetAllExports() {
        String r1 = seedReport();
        String r2 = seedReport();
        exportService.exportReport(r1, ExportFormat.PDF);
        exportService.exportReport(r2, ExportFormat.CSV);
        assertEquals(2, exportService.getAllExports().size());
    }

    @Test
    void shouldThrowForMissingReport() {
        assertThrows(IllegalArgumentException.class, () ->
            exportService.exportReport("missing", ExportFormat.PDF));
    }
}
