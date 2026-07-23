package com.sporekart.executive.copilot.engine;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FinancialIntelligenceEngineTest {

    private FinancialIntelligenceEngine engine;

    @BeforeEach
    void setUp() {
        engine = new FinancialIntelligenceEngine();
    }

    @Test
    void analyzeFinancials_shouldReturnValidResponse() {
        var response = engine.analyzeFinancials("revenue", "last_quarter");
        assertNotNull(response);
        assertNotNull(response.summary());
        assertFalse(response.summary().isEmpty());
        assertNotNull(response.metrics());
        assertFalse(response.metrics().isEmpty());
        assertNotNull(response.revenue());
    }

    @Test
    void analyzeFinancials_shouldHaveCorrectSummary() {
        var response = engine.analyzeFinancials("revenue", "last_quarter");
        assertEquals(12500000.0, response.summary().get("totalRevenue"));
        assertEquals(3000000.0, response.summary().get("netProfit"));
        assertEquals(24.0, response.summary().get("profitMargin"));
    }

    @Test
    void analyzeFinancials_shouldHaveMetrics() {
        var response = engine.analyzeFinancials("revenue", "last_quarter");
        assertTrue(response.metrics().size() >= 5);
        assertEquals("Revenue", response.metrics().get(0).name());
    }

    @Test
    void analyzeFinancials_shouldHaveRevenueCategories() {
        var response = engine.analyzeFinancials("revenue", "last_quarter");
        assertTrue(response.revenue().byCategory().containsKey("Spawn Kits"));
        assertTrue(response.revenue().byCategory().containsKey("Training"));
    }

    @Test
    void getRevenueBreakdown_shouldReturnAllCategories() {
        var breakdown = engine.getRevenueBreakdown();
        assertNotNull(breakdown);
        assertEquals(12500000.0, breakdown.totalRevenue());
        assertEquals(13.6, breakdown.growthRate());
        assertTrue(breakdown.byProductCategory().containsKey("Spawn Kits"));
        assertTrue(breakdown.byRegion().containsKey("North"));
        assertTrue(breakdown.byChannel().containsKey("Direct Website"));
    }

    @Test
    void getRevenueBreakdown_shouldHaveCorrectRegionBreakdown() {
        var breakdown = engine.getRevenueBreakdown();
        assertEquals(3800000.0, breakdown.byRegion().get("North"));
        assertEquals(800000.0, breakdown.byRegion().get("International"));
    }

    @Test
    void getProfitAnalysis_shouldReturnValidAnalysis() {
        var analysis = engine.getProfitAnalysis();
        assertNotNull(analysis);
        assertTrue(analysis.grossProfit() > 0);
        assertEquals(40.0, analysis.grossMargin());
        assertEquals(24.0, analysis.netMargin());
        assertEquals(30.4, analysis.ebitdaMargin());
    }

    @Test
    void calculateROI_shouldReturnCorrectValue() {
        double roi = engine.calculateROI(1000000.0, 2500000.0);
        assertEquals(150.0, roi);
    }

    @Test
    void calculateROI_negativeInvestment_shouldReturnZero() {
        double roi = engine.calculateROI(-1000.0, 5000.0);
        assertEquals(0.0, roi);
    }

    @Test
    void getFinancialTrends_shouldReturnValidTrends() {
        var trends = engine.getFinancialTrends("quarterly");
        assertNotNull(trends);
        assertEquals("quarterly", trends.get("period"));
        assertEquals(13.6, trends.get("revenueGrowth"));
        assertEquals("IMPROVING", trends.get("marginTrend"));
    }

    @Test
    void getFinancialTrends_shouldHaveAllKeys() {
        var trends = engine.getFinancialTrends("annual");
        assertNotNull(trends.get("period"));
        assertNotNull(trends.get("revenueGrowth"));
        assertNotNull(trends.get("profitGrowth"));
        assertNotNull(trends.get("expenseGrowth"));
        assertNotNull(trends.get("marginTrend"));
        assertNotNull(trends.get("projection"));
    }
}
