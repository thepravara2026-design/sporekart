package com.sporekart.executive.copilot.engine;

import com.sporekart.executive.copilot.dto.RiskRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RiskIntelligenceEngineTest {

    private RiskIntelligenceEngine engine;

    @BeforeEach
    void setUp() {
        engine = new RiskIntelligenceEngine();
    }

    @Test
    void assessRisks_shouldReturnValidResponse() {
        var request = new RiskRequest("all", "all");
        var response = engine.assessRisks(request);
        assertNotNull(response);
        assertTrue(response.overallRiskScore() >= 0);
        assertNotNull(response.riskLevel());
    }

    @Test
    void assessRisks_shouldHaveRiskCategories() {
        var request = new RiskRequest("all", "all");
        var response = engine.assessRisks(request);
        assertNotNull(response.criticalRisks());
        assertNotNull(response.highRisks());
        assertNotNull(response.mediumRisks());
        assertNotNull(response.lowRisks());
    }

    @Test
    void assessRisks_shouldHaveRisks() {
        var request = new RiskRequest("all", "all");
        var response = engine.assessRisks(request);
        assertFalse(response.highRisks().isEmpty());
    }

    @Test
    void getRiskMatrix_shouldReturnValidMatrix() {
        var matrix = engine.getRiskMatrix();
        assertNotNull(matrix);
        assertTrue(matrix.overallRiskScore() >= 0);
        assertNotNull(matrix.riskLevel());
        assertNotNull(matrix.criticalRisks());
        assertNotNull(matrix.highRisks());
        assertNotNull(matrix.mediumRisks());
        assertNotNull(matrix.lowRisks());
    }

    @Test
    void getTopRisks_shouldReturnRequestedCount() {
        var result = engine.getTopRisks(3);
        assertNotNull(result);
        assertTrue(result.containsKey("totalRisks"));
        assertTrue(result.containsKey("topRisks"));
        var topRisks = (java.util.List<?>) result.get("topRisks");
        assertTrue(topRisks.size() <= 3);
    }

    @Test
    void getTopRisks_default_shouldReturnAll() {
        var result = engine.getTopRisks(10);
        var topRisks = (java.util.List<?>) result.get("topRisks");
        assertEquals(8, topRisks.size());
    }

    @Test
    void detectRevenueDeclineRisk_shouldReturnAssessment() {
        var result = engine.detectRevenueDeclineRisk();
        assertNotNull(result);
        assertFalse((Boolean) result.get("riskDetected"));
        assertEquals("GROWING", result.get("currentTrend"));
    }

    @Test
    void detectRevenueDeclineRisk_shouldHaveWarningSignals() {
        var result = engine.detectRevenueDeclineRisk();
        var signals = (java.util.List<?>) result.get("warningSignals");
        assertFalse(signals.isEmpty());
    }

    @Test
    void assessRisks_riskItemsShouldHaveAllFields() {
        var request = new RiskRequest("all", "all");
        var response = engine.assessRisks(request);
        var risk = response.highRisks().get(0);
        assertNotNull(risk.title());
        assertNotNull(risk.category());
        assertTrue(risk.severity() >= 0);
        assertTrue(risk.probability() >= 0);
        assertTrue(risk.score() >= 0);
        assertNotNull(risk.impact());
        assertNotNull(risk.mitigation());
    }

    @Test
    void riskMatrix_shouldHaveCorrectLevel() {
        var matrix = engine.getRiskMatrix();
        assertNotNull(matrix.riskLevel());
    }

    @Test
    void riskMatrix_shouldHaveRisksItemsWithAllFields() {
        var matrix = engine.getRiskMatrix();
        if (!matrix.criticalRisks().isEmpty()) {
            var risk = matrix.criticalRisks().get(0);
            assertNotNull(risk.title());
            assertTrue(risk.riskScore() >= 0);
        }
    }

    @Test
    void getTopRisks_shouldBeSortedByScore() {
        var result = engine.getTopRisks(8);
        var topRisks = (java.util.List<com.sporekart.executive.copilot.dto.RiskResponse.RiskItem>) result.get("topRisks");
        for (int i = 0; i < topRisks.size() - 1; i++) {
            assertTrue(topRisks.get(i).score() >= topRisks.get(i + 1).score());
        }
    }
}
