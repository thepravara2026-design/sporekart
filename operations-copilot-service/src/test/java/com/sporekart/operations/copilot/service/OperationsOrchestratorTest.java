package com.sporekart.operations.copilot.service;

import com.sporekart.operations.copilot.dto.*;
import com.sporekart.operations.copilot.engine.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OperationsOrchestratorTest {

    @Mock private InventoryEngine inventoryEngine;
    @Mock private ProcurementEngine procurementEngine;
    @Mock private OrderOperationsEngine orderOperationsEngine;
    @Mock private WarehouseEngine warehouseEngine;
    @Mock private LogisticsEngine logisticsEngine;
    @Mock private DemandPlanningEngine demandPlanningEngine;
    @Mock private SupplyChainMonitorEngine supplyChainMonitorEngine;
    @Mock private OperationalKPIEngine operationalKPIEngine;
    @Mock private DecisionSupportEngine decisionSupportEngine;
    @Mock private CostOptimizationEngine costOptimizationEngine;

    private OperationsOrchestrator orchestrator;

    @BeforeEach
    void setUp() {
        orchestrator = new OperationsOrchestrator(inventoryEngine, procurementEngine, orderOperationsEngine,
            warehouseEngine, logisticsEngine, demandPlanningEngine, supplyChainMonitorEngine,
            operationalKPIEngine, decisionSupportEngine, costOptimizationEngine);
    }

    @Test
    void testRouteQueryInventory() {
        when(inventoryEngine.getInventoryStatus(any(), any())).thenReturn(
            new InventoryStatusResponse(10, 5, 2, 1, 1, 50000.0, 6.5, List.of()));
        when(inventoryEngine.generateRestockSuggestions(any())).thenReturn(List.of("Suggestion"));
        var request = new OperationsQueryRequest("Show inventory", "inventory", null, null, null);
        var response = orchestrator.routeQuery(request);
        assertNotNull(response);
        assertEquals("inventory", response.intent());
        assertTrue(response.data().containsKey("inventory"));
    }

    @Test
    void testRouteQueryInventoryAlerts() {
        when(inventoryEngine.getAlerts(any())).thenReturn(
            new InventoryAlertResponse(2, 1, 1, List.of()));
        var request = new OperationsQueryRequest("Show alerts", "inventory_alerts", null, null, null);
        var response = orchestrator.routeQuery(request);
        assertNotNull(response);
        assertEquals("inventory_alerts", response.intent());
    }

    @Test
    void testRouteQueryProcurement() {
        when(procurementEngine.recommendProcurement(any())).thenReturn(
            new ProcurementResponse("Recommendation", 5000.0, 600.0, List.of(), List.of()));
        var request = new OperationsQueryRequest("Procure spawn", "procurement", null, null, null);
        var response = orchestrator.routeQuery(request);
        assertNotNull(response);
        assertEquals("procurement", response.intent());
    }

    @Test
    void testRouteQueryOrders() {
        when(orderOperationsEngine.analyzeOrderQueue(any(), any())).thenReturn(
            new OrderOperationsResponse(Map.of("pending", 10), List.of(), List.of(), 94.5));
        var request = new OperationsQueryRequest("Order status", "orders", null, null, null);
        var response = orchestrator.routeQuery(request);
        assertNotNull(response);
    }

    @Test
    void testRouteQueryWarehouse() {
        when(warehouseEngine.analyzeWarehouse(any())).thenReturn(
            new WarehouseResponse("WH-MAIN", "HEALTHY", Map.of(), 85.0, List.of(), List.of()));
        var request = new OperationsQueryRequest("Warehouse status", "warehouse", null, null, null);
        var response = orchestrator.routeQuery(request);
        assertNotNull(response);
    }

    @Test
    void testRouteQueryLogistics() {
        when(logisticsEngine.planShipment(any())).thenReturn(
            new LogisticsResponse("Use Delhivery", List.of(), 100.0, 3, List.of()));
        var request = new OperationsQueryRequest("Ship to Delhi", "logistics", null, "Delhi", null);
        var response = orchestrator.routeQuery(request);
        assertNotNull(response);
    }

    @Test
    void testRouteQueryForecast() {
        when(demandPlanningEngine.forecastDemand(any())).thenReturn(
            new ForecastResponse("MUSH-001", "Product", 100, 500, 600, 85.0, Map.of(), List.of()));
        var request = new OperationsQueryRequest("Forecast demand", "forecast", null, null, null);
        var response = orchestrator.routeQuery(request);
        assertNotNull(response);
    }

    @Test
    void testRouteQuerySupplyChain() {
        when(supplyChainMonitorEngine.monitorSupplyChain(any(), any())).thenReturn(
            new SupplyChainResponse("Health okay", 80.0, List.of(), List.of()));
        var request = new OperationsQueryRequest("Supply chain health", "supply_chain", null, null, null);
        var response = orchestrator.routeQuery(request);
        assertNotNull(response);
    }

    @Test
    void testRouteQueryKPI() {
        when(operationalKPIEngine.getKPIs(any())).thenReturn(
            new KPIResponse(List.of(), Map.of()));
        var request = new OperationsQueryRequest("Show KPIs", "kpi", null, null, null);
        var response = orchestrator.routeQuery(request);
        assertNotNull(response);
    }

    @Test
    void testRouteQueryDecision() {
        when(decisionSupportEngine.getRecommendation(any())).thenReturn(
            new DecisionSupportResponse("Increase stock", 80.0, 50000.0, 0.85, "LOW", List.of(), List.of()));
        var request = new OperationsQueryRequest("What should I do?", "decision", null, null, null);
        var response = orchestrator.routeQuery(request);
        assertNotNull(response);
    }

    @Test
    void testRouteQueryCost() {
        when(costOptimizationEngine.analyzeCosts(any())).thenReturn(
            new CostOptimizationResponse(1000000.0, Map.of(), List.of(), 200000.0));
        var request = new OperationsQueryRequest("Optimize costs", "cost", null, null, null);
        var response = orchestrator.routeQuery(request);
        assertNotNull(response);
    }

    @Test
    void testRouteQueryDefault() {
        var request = new OperationsQueryRequest("Unknown", "unknown_intent", null, null, null);
        var response = orchestrator.routeQuery(request);
        assertNotNull(response);
        assertEquals("unknown_intent", response.intent());
    }

    @Test
    void testGetDashboard() {
        when(inventoryEngine.getInventoryStatus(any(), any())).thenReturn(
            new InventoryStatusResponse(10, 5, 2, 1, 1, 50000.0, 6.5, List.of()));
        when(orderOperationsEngine.analyzeOrderQueue(any(), any())).thenReturn(
            new OrderOperationsResponse(Map.of("pending", 10), List.of(), List.of(), 94.5));
        when(warehouseEngine.analyzeWarehouse(any())).thenReturn(
            new WarehouseResponse("WH-MAIN", "HEALTHY", Map.of(), 85.0, List.of(), List.of()));
        when(operationalKPIEngine.getKPIs(any())).thenReturn(
            new KPIResponse(List.of(), Map.of("overallHealth", 85.0)));
        var dashboard = orchestrator.getDashboard();
        assertNotNull(dashboard);
        assertNotNull(dashboard.inventorySummary());
        assertNotNull(dashboard.orderSummary());
    }

    @Test
    void testGetActiveAlerts() {
        var alerts = orchestrator.getActiveAlerts(null);
        assertFalse(alerts.isEmpty());
    }

    @Test
    void testGetActiveAlertsByCategory() {
        var alerts = orchestrator.getActiveAlerts("INVENTORY");
        assertFalse(alerts.isEmpty());
    }

    @Test
    void testGetInventoryStatus() {
        when(inventoryEngine.getInventoryStatus(any(), any())).thenReturn(
            new InventoryStatusResponse(10, 5, 2, 1, 1, 50000.0, 6.5, List.of()));
        var response = orchestrator.getInventoryStatus("WH-MAIN", null);
        assertNotNull(response);
    }

    @Test
    void testGetInventoryAlerts() {
        when(inventoryEngine.getAlerts(any())).thenReturn(new InventoryAlertResponse(2, 1, 1, List.of()));
        var response = orchestrator.getInventoryAlerts("WH-MAIN");
        assertNotNull(response);
    }

    @Test
    void testRecommendProcurement() {
        when(procurementEngine.recommendProcurement(any())).thenReturn(
            new ProcurementResponse("Rec", 1000.0, 100.0, List.of(), List.of()));
        var request = new ProcurementRequest("Buy items", "procurement", null, null, null);
        var response = orchestrator.recommendProcurement(request);
        assertNotNull(response);
    }

    @Test
    void testAnalyzeOrderQueue() {
        when(orderOperationsEngine.analyzeOrderQueue(any(), any())).thenReturn(
            new OrderOperationsResponse(Map.of(), List.of(), List.of(), 90.0));
        var response = orchestrator.analyzeOrderQueue("WH-MAIN", null);
        assertNotNull(response);
    }

    @Test
    void testAnalyzeWarehouse() {
        when(warehouseEngine.analyzeWarehouse(any())).thenReturn(
            new WarehouseResponse("WH-MAIN", "HEALTHY", Map.of(), 85.0, List.of(), List.of()));
        var response = orchestrator.analyzeWarehouse("WH-MAIN");
        assertNotNull(response);
    }

    @Test
    void testPlanShipment() {
        when(logisticsEngine.planShipment(any())).thenReturn(
            new LogisticsResponse("Use Delhivery", List.of(), 100.0, 3, List.of()));
        var request = new LogisticsRequest("Ship items", "logistics", null, null, null, null);
        var response = orchestrator.planShipment(request);
        assertNotNull(response);
    }

    @Test
    void testForecastDemand() {
        when(demandPlanningEngine.forecastDemand(any())).thenReturn(
            new ForecastResponse("P1", "Product", 100, 500, 600, 85.0, Map.of(), List.of()));
        var request = new ForecastRequest("P1", null, null);
        var response = orchestrator.forecastDemand(request);
        assertNotNull(response);
    }

    @Test
    void testMonitorSupplyChain() {
        when(supplyChainMonitorEngine.monitorSupplyChain(any(), any())).thenReturn(
            new SupplyChainResponse("OK", 80.0, List.of(), List.of()));
        var response = orchestrator.monitorSupplyChain("VEN-001", "West");
        assertNotNull(response);
    }

    @Test
    void testGetKPIs() {
        when(operationalKPIEngine.getKPIs(any())).thenReturn(new KPIResponse(List.of(), Map.of()));
        var request = new KPIRequest(null, null);
        var response = orchestrator.getKPIs(request);
        assertNotNull(response);
    }

    @Test
    void testGetDecisionRecommendation() {
        when(decisionSupportEngine.getRecommendation(any())).thenReturn(
            new DecisionSupportResponse("Rec", 80.0, 0.0, 0.85, "LOW", List.of(), List.of()));
        var request = new DecisionSupportRequest("Query", "intent", null, null);
        var response = orchestrator.getDecisionRecommendation(request);
        assertNotNull(response);
    }

    @Test
    void testAnalyzeCosts() {
        when(costOptimizationEngine.analyzeCosts(any())).thenReturn(
            new CostOptimizationResponse(1000000.0, Map.of(), List.of(), 200000.0));
        var request = new CostOptimizationRequest(null, null);
        var response = orchestrator.analyzeCosts(request);
        assertNotNull(response);
    }
}
