package com.sporekart.operations.copilot.engine;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class WarehouseEngineTest {

    @InjectMocks
    private WarehouseEngine warehouseEngine;

    @Test
    void testAnalyzeWarehouse() {
        var response = warehouseEngine.analyzeWarehouse("WH-MAIN");
        assertNotNull(response);
        assertEquals("WH-MAIN", response.warehouseId());
        assertNotNull(response.capacity());
        assertTrue(response.efficiency() > 0);
        assertFalse(response.recommendations().isEmpty());
    }

    @Test
    void testAnalyzeWarehouseWithNull() {
        var response = warehouseEngine.analyzeWarehouse(null);
        assertNotNull(response);
        assertEquals("WH-MAIN", response.warehouseId());
    }

    @Test
    void testGetWarehouseDetail() {
        var wh = warehouseEngine.getWarehouseDetail("WH-MAIN");
        assertNotNull(wh);
        assertEquals("WH-MAIN", wh.warehouseId());
        assertNotNull(wh.zones());
        assertFalse(wh.zones().isEmpty());
    }

    @Test
    void testOptimizeStorage() {
        var optimizations = warehouseEngine.optimizeStorage("WH-MAIN");
        assertFalse(optimizations.isEmpty());
    }

    @Test
    void testAnalyzePickingEfficiency() {
        var efficiency = warehouseEngine.analyzePickingEfficiency("WH-MAIN");
        assertNotNull(efficiency);
        assertTrue(efficiency.containsKey("efficiencyScore"));
    }

    @Test
    void testGetWorkerProductivity() {
        var productivity = warehouseEngine.getWorkerProductivity("WH-MAIN");
        assertNotNull(productivity);
        assertTrue(productivity.containsKey("totalWorkers"));
    }

    @Test
    void testGetBinUtilization() {
        var utilization = warehouseEngine.getBinUtilization("WH-MAIN");
        assertTrue(utilization > 0);
    }
}
