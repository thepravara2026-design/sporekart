package com.sporekart.executive.copilot.engine;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExecutiveDashboardEngineTest {

    private ExecutiveDashboardEngine engine;

    @BeforeEach
    void setUp() {
        engine = new ExecutiveDashboardEngine();
    }

    @Test
    void getDashboard_shouldReturnValidResponse() {
        var dashboard = engine.getDashboard();
        assertNotNull(dashboard);
        assertNotNull(dashboard.summary());
        assertNotNull(dashboard.health());
    }

    @Test
    void getDashboard_shouldHaveHealth() {
        var dashboard = engine.getDashboard();
        assertTrue(dashboard.health().overallScore() > 0);
        assertNotNull(dashboard.health().riskLevel());
    }

    @Test
    void getDashboard_shouldHaveFinancialHighlights() {
        var dashboard = engine.getDashboard();
        assertNotNull(dashboard.financialHighlights());
        assertTrue(dashboard.financialHighlights().containsKey("revenue"));
    }

    @Test
    void getDashboard_shouldHaveRecommendations() {
        var dashboard = engine.getDashboard();
        assertNotNull(dashboard.recommendations());
        assertFalse(dashboard.recommendations().isEmpty());
    }

    @Test
    void getDashboard_shouldHaveRisks() {
        var dashboard = engine.getDashboard();
        assertTrue(dashboard.risks().overallScore() >= 0);
        assertNotNull(dashboard.risks().level());
    }

    @Test
    void getDashboard_shouldHaveKeyMetrics() {
        var dashboard = engine.getDashboard();
        assertNotNull(dashboard.keyMetrics());
        assertFalse(dashboard.keyMetrics().isEmpty());
    }

    @Test
    void getDashboard_shouldHaveAlerts() {
        var dashboard = engine.getDashboard();
        assertNotNull(dashboard.alerts());
        assertFalse(dashboard.alerts().isEmpty());
    }

    @Test
    void getTodaySummary_shouldReturnValidSummary() {
        var summary = engine.getTodaySummary();
        assertNotNull(summary);
        assertNotNull(summary.get("date"));
        assertTrue((Integer) summary.get("ordersToday") > 0);
    }

    @Test
    void getTodaySummary_shouldHaveRevenue() {
        var summary = engine.getTodaySummary();
        assertTrue((Double) summary.get("revenueToday") > 0);
    }

    @Test
    void getWeeklyReport_shouldReturnValidReport() {
        var report = engine.getWeeklyReport();
        assertNotNull(report);
        assertNotNull(report.get("week"));
        assertTrue((Integer) report.get("orders") > 0);
    }

    @Test
    void getWeeklyReport_shouldHaveFulfillmentRate() {
        var report = engine.getWeeklyReport();
        assertTrue((Double) report.get("fulfillmentRate") > 0);
    }

    @Test
    void getMonthlyReport_shouldReturnValidReport() {
        var report = engine.getMonthlyReport();
        assertNotNull(report);
        assertNotNull(report.get("month"));
        assertTrue((Integer) report.get("orders") > 0);
    }

    @Test
    void getMonthlyReport_shouldHaveProfit() {
        var report = engine.getMonthlyReport();
        assertTrue((Double) report.get("profit") > 0);
    }
}
