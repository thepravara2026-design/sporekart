package com.sporekart.ai.analytics.engine;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class KpiCalculatorTest {

    private KpiCalculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new KpiCalculator();
    }

    @Test
    void testCalculateAllReturnsAllKpis() {
        Map<String, Double> kpis = calculator.calculateAll();

        assertNotNull(kpis);
        assertEquals(8, kpis.size());
        assertTrue(kpis.containsKey("governanceSuccessRate"));
        assertTrue(kpis.containsKey("policyEvaluationRate"));
        assertTrue(kpis.containsKey("approvalSLACompliance"));
        assertTrue(kpis.containsKey("compliancePassRate"));
        assertTrue(kpis.containsKey("averageTrustScore"));
        assertTrue(kpis.containsKey("averageConfidenceScore"));
        assertTrue(kpis.containsKey("auditCompletionRate"));
        assertTrue(kpis.containsKey("systemAvailability"));
    }

    @Test
    void testDefaultValues() {
        assertEquals(95.0, calculator.governanceSuccessRate());
        assertEquals(90.0, calculator.policyEvaluationRate());
        assertEquals(85.0, calculator.approvalSLACompliance());
        assertEquals(80.0, calculator.compliancePassRate());
        assertEquals(75.0, calculator.averageTrustScore());
        assertEquals(70.0, calculator.averageConfidenceScore());
        assertEquals(95.0, calculator.auditCompletionRate());
        assertEquals(99.9, calculator.systemAvailability());
    }

    @Test
    void testDecisionDistribution() {
        var distribution = calculator.decisionDistribution();
        assertEquals(3, distribution.size());
        assertTrue(distribution.containsKey("allow"));
        assertTrue(distribution.containsKey("deny"));
        assertTrue(distribution.containsKey("escalate"));
    }

    @Test
    void testRiskDistribution() {
        var distribution = calculator.riskDistribution();
        assertEquals(4, distribution.size());
        assertTrue(distribution.containsKey("low"));
        assertTrue(distribution.containsKey("medium"));
        assertTrue(distribution.containsKey("high"));
        assertTrue(distribution.containsKey("critical"));
    }
}
