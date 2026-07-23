package com.sporekart.operations.copilot.engine;

import com.sporekart.operations.copilot.dto.CostOptimizationRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CostOptimizationEngineTest {

    @InjectMocks
    private CostOptimizationEngine costOptimizationEngine;

    @Test
    void testAnalyzeCosts() {
        var request = new CostOptimizationRequest(null, null);
        var response = costOptimizationEngine.analyzeCosts(request);
        assertNotNull(response);
        assertTrue(response.totalOperationalCost() > 0);
        assertFalse(response.costBreakdown().isEmpty());
        assertFalse(response.opportunities().isEmpty());
        assertTrue(response.totalPotentialSavings() > 0);
    }

    @Test
    void testAnalyzeCostsWithCategory() {
        var request = new CostOptimizationRequest("Warehouse", "monthly");
        var response = costOptimizationEngine.analyzeCosts(request);
        assertNotNull(response);
    }

    @Test
    void testOptimizeProcesses() {
        var optimizations = costOptimizationEngine.optimizeProcesses(null);
        assertEquals(3, optimizations.size());
    }

    @Test
    void testOptimizeProcessesWithArea() {
        var optimizations = costOptimizationEngine.optimizeProcesses("Picking");
        assertFalse(optimizations.isEmpty());
    }

    @Test
    void testGetCostTrends() {
        var trends = costOptimizationEngine.getCostTrends("last_quarter");
        assertNotNull(trends);
        assertTrue(trends.containsKey("totalCost"));
    }

    @Test
    void testGetCostTrendsWithNull() {
        var trends = costOptimizationEngine.getCostTrends(null);
        assertNotNull(trends);
    }

    @Test
    void testCalculateSavings() {
        var savings = costOptimizationEngine.calculateSavings("OPT-001", 100000.0, 75000.0);
        assertEquals(25000.0, savings, 0.01);
    }
}
