package com.sporekart.operations.copilot.engine;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SupplyChainMonitorEngineTest {

    @InjectMocks
    private SupplyChainMonitorEngine supplyChainMonitorEngine;

    @Test
    void testMonitorSupplyChain() {
        var response = supplyChainMonitorEngine.monitorSupplyChain("VEN-001", "West");
        assertNotNull(response);
        assertTrue(response.healthScore() > 0);
        assertFalse(response.risks().isEmpty());
        assertFalse(response.recommendations().isEmpty());
    }

    @Test
    void testMonitorSupplyChainWithNulls() {
        var response = supplyChainMonitorEngine.monitorSupplyChain(null, null);
        assertNotNull(response);
    }

    @Test
    void testGetActiveAlerts() {
        var alerts = supplyChainMonitorEngine.getActiveAlerts(null);
        assertEquals(3, alerts.size());
    }

    @Test
    void testGetActiveAlertsBySeverity() {
        var alerts = supplyChainMonitorEngine.getActiveAlerts("CRITICAL");
        assertEquals(1, alerts.size());
    }

    @Test
    void testDetectAnomalies() {
        var anomalies = supplyChainMonitorEngine.detectAnomalies("WH-MAIN");
        assertNotNull(anomalies);
        assertTrue(anomalies.containsKey("anomaliesDetected"));
    }

    @Test
    void testCalculateSupplyChainHealth() {
        var health = supplyChainMonitorEngine.calculateSupplyChainHealth();
        assertNotNull(health);
        assertTrue(health.containsKey("overallHealthScore"));
    }

    @Test
    void testGetVendorRiskAssessment() {
        var assessment = supplyChainMonitorEngine.getVendorRiskAssessment("VEN-001");
        assertFalse(assessment.isEmpty());
        assertTrue(assessment.get(0).contains("Financial"));
    }
}
