package com.sporekart.alert.domain.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BusinessRiskTest {

    @Test
    void createShouldReturnBusinessRiskWithGivenValues() {
        var risk = BusinessRisk.create("Inventory Risk", "Risk of stockout",
                RiskCategory.OPERATIONAL, RiskSeverity.HIGH, "inventory",
                "Potential stockout", 0.75, 0.80, "Increase safety stock");
        assertNotNull(risk.id());
        assertEquals("Inventory Risk", risk.title());
        assertEquals("Risk of stockout", risk.description());
        assertEquals(RiskCategory.OPERATIONAL, risk.category());
        assertEquals(RiskSeverity.HIGH, risk.severity());
        assertEquals("inventory", risk.domain());
        assertEquals("Potential stockout", risk.impact());
        assertEquals(0.75, risk.likelihood());
        assertEquals(0.80, risk.riskScore());
    }

    @Test
    void createShouldSetActiveStatus() {
        var risk = BusinessRisk.create("Risk", "Desc",
                RiskCategory.REVENUE, RiskSeverity.MEDIUM, "finance",
                "Impact", 0.5, 0.6, "Mitigate");
        assertEquals("ACTIVE", risk.status());
        assertNotNull(risk.createdAt());
    }
}
