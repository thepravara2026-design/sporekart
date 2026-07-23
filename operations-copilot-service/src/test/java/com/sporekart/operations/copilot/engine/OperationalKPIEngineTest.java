package com.sporekart.operations.copilot.engine;

import com.sporekart.operations.copilot.dto.KPIRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class OperationalKPIEngineTest {

    @InjectMocks
    private OperationalKPIEngine operationalKPIEngine;

    @Test
    void testGetKPIs() {
        var request = new KPIRequest(null, null);
        var response = operationalKPIEngine.getKPIs(request);
        assertNotNull(response);
        assertFalse(response.kpis().isEmpty());
        assertNotNull(response.summary());
        assertTrue((Integer) response.summary().get("totalKPIs") > 0);
    }

    @Test
    void testGetKPIsByCategory() {
        var request = new KPIRequest("Fulfillment", null);
        var response = operationalKPIEngine.getKPIs(request);
        assertNotNull(response);
        assertFalse(response.kpis().isEmpty());
    }

    @Test
    void testGenerateKPIList() {
        var kpis = operationalKPIEngine.generateKPIList();
        assertEquals(10, kpis.size());
    }

    @Test
    void testGetKPIDetail() {
        var kpi = operationalKPIEngine.getKPIDetail("KPI-001");
        assertNotNull(kpi);
        assertEquals("KPI-001", kpi.kpiId());
    }

    @Test
    void testGetKPIsByCategoryInventory() {
        var kpis = operationalKPIEngine.getKPIsByCategory("Inventory");
        assertFalse(kpis.isEmpty());
        assertTrue(kpis.stream().allMatch(k -> k.category().equals("Inventory")));
    }

    @Test
    void testGetSLACompliance() {
        var sla = operationalKPIEngine.getSLACompliance();
        assertNotNull(sla);
        assertTrue(sla.containsKey("orderSLA"));
        assertTrue(sla.containsKey("shippingSLA"));
    }
}
