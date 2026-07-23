package com.sporekart.executive.copilot.engine;

import com.sporekart.executive.copilot.dto.ReportRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardReportEngineTest {

    private BoardReportEngine engine;

    @BeforeEach
    void setUp() {
        engine = new BoardReportEngine();
    }

    @Test
    void generateReport_shouldReturnValidResponse() {
        var request = new ReportRequest("quarterly", "current_quarter");
        var response = engine.generateReport(request);
        assertNotNull(response);
        assertNotNull(response.reportId());
        assertNotNull(response.title());
        assertNotNull(response.period());
    }

    @Test
    void generateReport_shouldHaveExecutiveSummary() {
        var request = new ReportRequest("quarterly", "current_quarter");
        var response = engine.generateReport(request);
        assertNotNull(response.executiveSummary());
        assertFalse(response.executiveSummary().isBlank());
    }

    @Test
    void generateReport_shouldHaveFinancialSummary() {
        var request = new ReportRequest("quarterly", "current_quarter");
        var response = engine.generateReport(request);
        assertNotNull(response.financialSummary());
        assertTrue(response.financialSummary().containsKey("totalRevenue"));
    }

    @Test
    void generateReport_shouldHaveKPIs() {
        var request = new ReportRequest("quarterly", "current_quarter");
        var response = engine.generateReport(request);
        assertNotNull(response.businessKPIs());
        assertTrue(response.businessKPIs().containsKey("orderFulfillmentRate"));
    }

    @Test
    void generateReport_shouldHaveRisks() {
        var request = new ReportRequest("quarterly", "current_quarter");
        var response = engine.generateReport(request);
        assertNotNull(response.strategicRisks());
        assertFalse(response.strategicRisks().isEmpty());
    }

    @Test
    void generateReport_shouldHaveOpportunities() {
        var request = new ReportRequest("quarterly", "current_quarter");
        var response = engine.generateReport(request);
        assertNotNull(response.growthOpportunities());
        assertFalse(response.growthOpportunities().isEmpty());
    }

    @Test
    void generateReport_shouldHaveDepartmentStatus() {
        var request = new ReportRequest("quarterly", "current_quarter");
        var response = engine.generateReport(request);
        assertNotNull(response.departmentalStatus());
        assertTrue(response.departmentalStatus().containsKey("operations"));
    }

    @Test
    void generateReport_shouldHaveOutlook() {
        var request = new ReportRequest("quarterly", "current_quarter");
        var response = engine.generateReport(request);
        assertNotNull(response.futureOutlook());
        assertFalse(response.futureOutlook().isBlank());
    }

    @Test
    void generateReport_annual_shouldReturnValidResponse() {
        var request = new ReportRequest("annual", "fy_2026");
        var response = engine.generateReport(request);
        assertNotNull(response);
        assertTrue(response.title().toLowerCase().contains("annual"));
    }

    @Test
    void generateReport_withNullPeriod_shouldDefault() {
        var request = new ReportRequest("quarterly", null);
        var response = engine.generateReport(request);
        assertEquals("current_quarter", response.period());
    }

    @Test
    void generateBoardReport_shouldReturnValidReport() {
        var report = engine.generateBoardReport("current_quarter");
        assertNotNull(report);
        assertNotNull(report.title());
        assertNotNull(report.executiveSummary());
        assertNotNull(report.financialSummary());
        assertNotNull(report.businessKPIs());
    }
}
