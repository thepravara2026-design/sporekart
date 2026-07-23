package com.sporekart.executive.copilot.engine;

import com.sporekart.executive.copilot.domain.BusinessSustainability;
import com.sporekart.executive.copilot.domain.CashFlowIndicators;
import com.sporekart.executive.copilot.domain.CompanyHealth;
import com.sporekart.executive.copilot.dto.HealthResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CompanyHealthEngineTest {

    private CompanyHealthEngine engine;

    @BeforeEach
    void setUp() {
        engine = new CompanyHealthEngine();
    }

    @Test
    void calculateOverallHealth_shouldReturnValidHealth() {
        var health = engine.calculateOverallHealth();
        assertNotNull(health);
        assertTrue(health.overallScore() > 0);
        assertNotNull(health.dimensions());
        assertEquals(8, health.dimensions().size());
        assertNotNull(health.riskLevel());
        assertNotNull(health.summary());
    }

    @Test
    void calculateOverallHealth_shouldHaveAllDimensions() {
        var health = engine.calculateOverallHealth();
        assertTrue(health.dimensions().containsKey("revenue"));
        assertTrue(health.dimensions().containsKey("profitability"));
        assertTrue(health.dimensions().containsKey("growth"));
        assertTrue(health.dimensions().containsKey("customer"));
        assertTrue(health.dimensions().containsKey("operations"));
        assertTrue(health.dimensions().containsKey("marketing"));
        assertTrue(health.dimensions().containsKey("inventory"));
        assertTrue(health.dimensions().containsKey("training"));
    }

    @Test
    void calculateOverallHealth_shouldHaveLowRiskLevel() {
        var health = engine.calculateOverallHealth();
        assertEquals("LOW", health.riskLevel());
    }

    @Test
    void getHealthReport_shouldReturnValidResponse() {
        var report = engine.getHealthReport();
        assertNotNull(report);
        assertTrue(report.overallScore() > 0);
        assertNotNull(report.dimensions());
        assertFalse(report.dimensions().isEmpty());
        assertNotNull(report.summary());
    }

    @Test
    void getHealthReport_shouldHaveDimensionItems() {
        var report = engine.getHealthReport();
        for (var dim : report.dimensions()) {
            assertNotNull(dim.name());
            assertTrue(dim.score() >= 0);
            assertNotNull(dim.status());
            assertNotNull(dim.insight());
        }
    }

    @Test
    void getDimensionHealth_shouldReturnValidHealth() {
        var health = engine.getDimensionHealth("revenue");
        assertNotNull(health);
        assertTrue(health.overallScore() > 0);
        assertTrue(health.dimensions().containsKey("revenue"));
    }

    @Test
    void getDimensionHealth_unknownDimension_shouldReturnZero() {
        var health = engine.getDimensionHealth("unknown");
        assertNotNull(health);
        assertEquals(0.0, health.overallScore());
    }

    @Test
    void assessSustainability_shouldReturnValidIndicators() {
        var sustainability = engine.assessSustainability();
        assertNotNull(sustainability);
        assertTrue(sustainability.sustainabilityScore() >= 0);
        assertTrue(sustainability.revenueDiversification() >= 0);
        assertTrue(sustainability.customerConcentration() >= 0);
        assertNotNull(sustainability.recommendation());
    }

    @Test
    void analyzeCashFlow_shouldReturnValidIndicators() {
        var cashFlow = engine.analyzeCashFlow();
        assertNotNull(cashFlow);
        assertTrue(cashFlow.operatingCashFlow() >= 0);
        assertNotNull(cashFlow.status());
        assertEquals("HEALTHY", cashFlow.status());
    }

    @Test
    void analyzeCashFlow_shouldHaveAllFields() {
        var cashFlow = engine.analyzeCashFlow();
        assertTrue(cashFlow.financingCashFlow() < 0);
        assertTrue(cashFlow.operatingCashFlow() > 0);
        assertTrue(cashFlow.investingCashFlow() < 0);
        assertTrue(cashFlow.freeCashFlow() > 0);
        assertTrue(cashFlow.runwayMonths() > 0);
    }
}
