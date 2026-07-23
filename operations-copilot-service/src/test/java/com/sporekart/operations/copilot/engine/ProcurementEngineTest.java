package com.sporekart.operations.copilot.engine;

import com.sporekart.operations.copilot.dto.ProcurementRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ProcurementEngineTest {

    @InjectMocks
    private ProcurementEngine procurementEngine;

    @Test
    void testRecommendProcurement() {
        var request = new ProcurementRequest("Mushroom spawn", "procurement", null, "MUSH-001", "standard");
        var response = procurementEngine.recommendProcurement(request);
        assertNotNull(response);
        assertNotNull(response.recommendation());
        assertFalse(response.vendorSuggestions().isEmpty());
    }

    @Test
    void testGetVendorSuggestions() {
        var vendors = procurementEngine.getVendorSuggestions("MUSH-001");
        assertFalse(vendors.isEmpty());
        assertEquals(5, vendors.size());
    }

    @Test
    void testCreatePurchaseOrder() {
        var po = procurementEngine.createPurchaseOrder("VEN-001", "MUSH-001", 100, "WH-MAIN");
        assertNotNull(po);
        assertNotNull(po.poId());
        assertEquals(com.sporekart.operations.copilot.domain.PurchaseOrder.POStatus.DRAFT, po.status());
    }

    @Test
    void testCalculateEOQ() {
        var eoq = procurementEngine.calculateEOQ(10000, 500, 20);
        assertTrue(eoq > 0);
    }

    @Test
    void testCalculateEOQWithZeroHoldingCost() {
        var eoq = procurementEngine.calculateEOQ(10000, 500, 0);
        assertEquals(0, eoq);
    }

    @Test
    void testAnalyzeVendorRisk() {
        var risk = procurementEngine.analyzeVendorRisk("VEN-001");
        assertNotNull(risk);
        assertTrue(risk.containsKey("overallRiskScore"));
    }

    @Test
    void testGetProcurementTimelineEmergency() {
        var timeline = procurementEngine.getProcurementTimeline("emergency");
        assertFalse(timeline.isEmpty());
        assertTrue(timeline.get(0).contains("Same day"));
    }

    @Test
    void testGetProcurementTimelineStandard() {
        var timeline = procurementEngine.getProcurementTimeline("standard");
        assertFalse(timeline.isEmpty());
    }

    @Test
    void testGetProcurementTimelineDefault() {
        var timeline = procurementEngine.getProcurementTimeline("unknown");
        assertEquals(1, timeline.size());
    }
}
