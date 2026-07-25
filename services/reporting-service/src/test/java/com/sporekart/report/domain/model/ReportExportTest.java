package com.sporekart.report.domain.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ReportExportTest {
    @Test
    void shouldCreateExportWithStaticFactory() {
        ReportExport e = ReportExport.create("report-1", "Sales Report", ExportFormat.PDF,
            "sales-report-2026-07-25.pdf", 1024L);
        assertNotNull(e.id());
        assertEquals("report-1", e.reportId());
        assertEquals(ExportFormat.PDF, e.format());
        assertEquals("COMPLETED", e.status());
        assertEquals("sales-report-2026-07-25.pdf", e.filename());
        assertEquals(1024L, e.fileSizeBytes());
        assertNotNull(e.exportedAt());
    }

    @Test
    void shouldGenerateFilenameForPdf() {
        String filename = ReportExport.generateFilename("Monthly Sales Report", ExportFormat.PDF);
        assertTrue(filename.startsWith("monthly-sales-report-"));
        assertTrue(filename.endsWith(".pdf"));
    }

    @Test
    void shouldGenerateFilenameForCsv() {
        String filename = ReportExport.generateFilename("Daily Summary", ExportFormat.CSV);
        assertTrue(filename.endsWith(".csv"));
    }

    @Test
    void shouldGenerateFilenameForJson() {
        String filename = ReportExport.generateFilename("Data Export", ExportFormat.JSON);
        assertTrue(filename.endsWith(".json"));
    }

    @Test
    void shouldGenerateFilenameForExcel() {
        String filename = ReportExport.generateFilename("Financial Report", ExportFormat.EXCEL);
        assertTrue(filename.endsWith(".xlsx"));
    }
}
