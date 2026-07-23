package com.sporekart.admin.engine;

import com.sporekart.admin.domain.PerformanceReport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ReportingEngineTest {

    private ReportingEngine engine;

    @BeforeEach
    void setUp() {
        engine = new ReportingEngine();
    }

    @Test
    void generateReportShouldReturnPerformanceReport() {
        PerformanceReport report = engine.generateReport("Test Report", "PDF",
            LocalDate.now().minusDays(30), LocalDate.now(), List.of("revenue", "orders"));

        assertNotNull(report);
        assertNotNull(report.id());
        assertNotNull(report.title());
        assertEquals("Test Report", report.title());
        assertNotNull(report.type());
        assertNotNull(report.generatedAt());
        assertNotNull(report.metrics());
        assertFalse(report.metrics().isEmpty());
    }

    @Test
    void generateSalesReportHasSalesMetrics() {
        PerformanceReport report = engine.generateSalesReport("Monthly Sales", "MONTHLY");

        assertNotNull(report);
        assertTrue(report.metrics().containsKey("totalRevenue"));
        assertTrue(report.metrics().containsKey("totalOrders"));
        assertTrue(report.metrics().containsKey("averageOrderValue"));
        assertTrue(report.metrics().containsKey("growthRate"));
        assertTrue(report.metrics().containsKey("topProducts"));
    }

    @Test
    void generateDashboardReportContainsKPIData() {
        PerformanceReport report = engine.generateDashboardReport("Executive Dashboard");

        assertNotNull(report);
        assertNotNull(report.metrics());
        assertTrue(report.metrics().containsKey("kpis"));
        assertTrue(report.metrics().containsKey("period"));
    }

    @Test
    void exportReportReturnsBytesForCSV() {
        PerformanceReport report = engine.generateReport("Export Test", "CSV",
            LocalDate.now().minusDays(7), LocalDate.now(), List.of("revenue", "orders"));

        byte[] csvBytes = engine.exportReport(report, "CSV");

        assertNotNull(csvBytes);
        assertTrue(csvBytes.length > 0);
        String content = new String(csvBytes);
        assertTrue(content.contains("revenue") || content.contains("Revenue"));
    }

    @Test
    void differentFormatsReturnDifferentContent() {
        PerformanceReport report = engine.generateReport("Format Test", "PDF",
            LocalDate.now().minusDays(7), LocalDate.now(), List.of("revenue"));

        byte[] csvBytes = engine.exportReport(report, "CSV");
        byte[] jsonBytes = engine.exportReport(report, "JSON");

        assertNotNull(csvBytes);
        assertNotNull(jsonBytes);
        String csvContent = new String(csvBytes);
        String jsonContent = new String(jsonBytes);
        assertFalse(csvContent.equals(jsonContent));
    }
}
