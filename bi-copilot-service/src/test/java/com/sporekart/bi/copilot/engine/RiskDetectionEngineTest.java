package com.sporekart.bi.copilot.engine;

import com.sporekart.bi.copilot.domain.RiskAlert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class RiskDetectionEngineTest {

    @InjectMocks
    private RiskDetectionEngine engine;

    @Test
    void detectRisksReturnsRiskAlerts() {
        List<RiskAlert> risks = engine.detectRisks("current");
        assertNotNull(risks);
        assertFalse(risks.isEmpty());
    }

    @Test
    void detectRisksReturnsRisksWithSeverity() {
        List<RiskAlert> risks = engine.detectRisks("current");
        risks.forEach(r -> {
            assertNotNull(r.riskId());
            assertNotNull(r.severity());
            assertNotNull(r.riskType());
        });
    }

    @Test
    void detectRevenueDeclineReturnsRevenueRisks() {
        List<RiskAlert> risks = engine.detectRevenueDecline("current");
        assertNotNull(risks);
        risks.forEach(r -> assertEquals("REVENUE_DECLINE", r.riskType()));
    }

    @Test
    void detectCustomerChurnReturnsChurnRisks() {
        List<RiskAlert> risks = engine.detectCustomerChurn("current");
        assertNotNull(risks);
        risks.forEach(r -> assertEquals("CUSTOMER_CHURN", r.riskType()));
    }

    @Test
    void detectInventoryShortageReturnsInventoryRisks() {
        List<RiskAlert> risks = engine.detectInventoryShortage("current");
        assertNotNull(risks);
        risks.forEach(r -> assertEquals("INVENTORY_SHORTAGE", r.riskType()));
    }

    @Test
    void getActiveRisksReturnsOpenRisks() {
        List<RiskAlert> risks = engine.getActiveRisks("current");
        assertNotNull(risks);
        risks.forEach(r -> assertEquals("OPEN", r.status()));
    }

    @Test
    void getRiskSummaryReturnsSummaryData() {
        Map<String, Object> summary = engine.getRiskSummary("current");
        assertNotNull(summary);
        assertTrue(summary.containsKey("total"));
        assertTrue(summary.containsKey("critical"));
        assertTrue(summary.containsKey("high"));
    }

    @Test
    void mitigateRiskUpdatesRiskStatus() {
        RiskAlert mitigated = engine.mitigateRisk("RISK-001", "Increase reorder level");
        assertNotNull(mitigated);
        assertEquals("MITIGATED", mitigated.status());
        assertNotNull(mitigated.recommendedAction());
    }

    @Test
    void detectRisksIncludesProbabilityAndImpact() {
        List<RiskAlert> risks = engine.detectRisks("current");
        risks.forEach(r -> {
            assertTrue(r.probability() >= 0);
            assertTrue(r.probability() <= 1);
            assertTrue(r.impact() >= 0);
        });
    }
}
