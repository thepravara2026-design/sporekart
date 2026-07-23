package com.sporekart.bi.copilot.engine;

import com.sporekart.bi.copilot.domain.ReportDefinition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ReportingEngineTest {

    private ReportingEngine engine;

    @BeforeEach
    void setUp() {
        engine = new ReportingEngine();
    }

    @Test
    void generateRevenueReport_shouldContainRevenueData() {
        Map<String, Object> report = engine.generateRevenueReport("pdf", Map.of("period", "2025-06"));
        assertEquals("revenue", report.get("type"));
        assertTrue(report.containsKey("data"));
        Map<String, Object> data = (Map<String, Object>) report.get("data");
        assertTrue(data.containsKey("totalRevenue"));
        assertTrue(data.containsKey("totalOrders"));
    }

    @Test
    void generateCustomerReport_shouldContainCustomerData() {
        Map<String, Object> report = engine.generateCustomerReport("csv", Map.of());
        assertEquals("customer", report.get("type"));
        Map<String, Object> data = (Map<String, Object>) report.get("data");
        assertTrue(data.containsKey("totalCustomers"));
        assertTrue(data.containsKey("churnRate"));
    }

    @Test
    void generateExecutiveSummary_shouldHaveKeyElements() {
        Map<String, Object> summary = engine.generateExecutiveSummary("pdf");
        assertEquals("executive_summary", summary.get("type"));
        Map<String, Object> data = (Map<String, Object>) summary.get("data");
        assertTrue(data.containsKey("keyMetrics"));
        assertTrue(data.containsKey("highlights"));
        assertTrue(data.containsKey("risks"));
        assertTrue(data.containsKey("recommendations"));
    }

    @Test
    void scheduleReport_shouldReturnScheduledDefinition() {
        ReportDefinition def = new ReportDefinition(null, "Weekly Revenue", "revenue", "pdf",
                "0 0 9 * * MON", List.of("totalRevenue"), List.of("region"), Map.of(),
                List.of("admin@sporekart.com"), null, null);
        ReportDefinition scheduled = engine.scheduleReport(def);
        assertNotNull(scheduled.reportId());
        assertEquals("scheduled", scheduled.status());
    }

    @Test
    void getScheduledReports_shouldReturnEmptyInitially() {
        List<ReportDefinition> reports = engine.getScheduledReports();
        assertTrue(reports.isEmpty());
    }

    @Test
    void getScheduledReports_shouldIncludeScheduledAfterSchedule() {
        ReportDefinition def = new ReportDefinition(null, "Monthly", "customer", "xlsx",
                "0 0 0 1 * *", List.of(), List.of(), Map.of(), List.of(), null, null);
        engine.scheduleReport(def);
        List<ReportDefinition> reports = engine.getScheduledReports();
        assertEquals(1, reports.size());
    }

    @Test
    void generateReport_shouldReturnCompletedStatus() {
        ReportDefinition def = new ReportDefinition(null, "Test Report", "revenue", "pdf",
                null, List.of("revenue"), List.of(), Map.of(), List.of(), null, null);
        Map<String, Object> result = engine.generateReport(def);
        assertEquals("completed", result.get("status"));
    }

    @Test
    void generateReport_shouldHaveReportId() {
        ReportDefinition def = new ReportDefinition(null, "Test", "customer", "csv",
                null, List.of(), List.of(), Map.of(), List.of(), null, null);
        Map<String, Object> result = engine.generateReport(def);
        assertNotNull(result.get("reportId"));
    }

    @Test
    void generateTrainingReport_shouldContainTrainingData() {
        Map<String, Object> report = engine.generateTrainingReport("pdf", Map.of());
        assertEquals("training", report.get("type"));
        Map<String, Object> data = (Map<String, Object>) report.get("data");
        assertTrue(data.containsKey("totalStudents"));
        assertTrue(data.containsKey("averageScore"));
    }

    @Test
    void generateCultivationReport_shouldContainCultivationData() {
        Map<String, Object> report = engine.generateCultivationReport("pdf", Map.of());
        assertEquals("cultivation", report.get("type"));
        Map<String, Object> data = (Map<String, Object>) report.get("data");
        assertTrue(data.containsKey("totalYieldKg"));
        assertTrue(data.containsKey("contaminationRate"));
    }

    @Test
    void generateComprehensiveReport_shouldMergeAllSections() {
        Map<String, Object> report = engine.generateComprehensiveReport("pdf");
        assertEquals("comprehensive", report.get("type"));
        Map<String, Object> data = (Map<String, Object>) report.get("data");
        assertTrue(data.containsKey("executiveSummary"));
        assertTrue(data.containsKey("revenue"));
        assertTrue(data.containsKey("customer"));
        assertTrue(data.containsKey("training"));
        assertTrue(data.containsKey("cultivation"));
    }
}
