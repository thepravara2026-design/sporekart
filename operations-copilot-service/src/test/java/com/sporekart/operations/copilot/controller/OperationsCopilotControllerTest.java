package com.sporekart.operations.copilot.controller;

import com.sporekart.operations.copilot.dto.*;
import com.sporekart.operations.copilot.service.OperationsOrchestrator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OperationsCopilotControllerTest {

    @Mock private OperationsOrchestrator orchestrator;
    @InjectMocks private OperationsCopilotController controller;

    @Test
    void testHandleQuery() {
        when(orchestrator.routeQuery(any())).thenReturn(
            new OperationsQueryResponse("q1", "inventory", "Summary", Map.of(), List.of(), List.of(), 100L));
        var request = new OperationsQueryRequest("Show stock", "inventory", null, null, null);
        var response = controller.handleQuery(request);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().success());
    }

    @Test
    void testGetInventory() {
        when(orchestrator.getInventoryStatus(any(), any())).thenReturn(
            new InventoryStatusResponse(10, 5, 2, 1, 1, 50000.0, 6.5, List.of()));
        var response = controller.getInventory("WH-MAIN", null);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testGetProcurement() {
        when(orchestrator.recommendProcurement(any())).thenReturn(
            new ProcurementResponse("Buy", 1000.0, 100.0, List.of(), List.of()));
        var request = new ProcurementRequest("Buy items", "procurement", null, null, null);
        var response = controller.getProcurement(request);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testGetOrders() {
        when(orchestrator.analyzeOrderQueue(any(), any())).thenReturn(
            new OrderOperationsResponse(Map.of("pending", 10), List.of(), List.of(), 94.5));
        var response = controller.getOrders("WH-MAIN", null);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testGetWarehouse() {
        when(orchestrator.analyzeWarehouse(any())).thenReturn(
            new WarehouseResponse("WH-MAIN", "HEALTHY", Map.of(), 85.0, List.of(), List.of()));
        var response = controller.getWarehouse("WH-MAIN");
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testGetLogistics() {
        when(orchestrator.planShipment(any())).thenReturn(
            new LogisticsResponse("Use Delhivery", List.of(), 100.0, 3, List.of()));
        var request = new LogisticsRequest("Ship", "logistics", null, null, null, null);
        var response = controller.getLogistics(request);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testGetForecast() {
        when(orchestrator.forecastDemand(any())).thenReturn(
            new ForecastResponse("P1", "Product", 100, 500, 600, 85.0, Map.of(), List.of()));
        var request = new ForecastRequest("P1", null, null);
        var response = controller.getForecast(request);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testGetDashboard() {
        when(orchestrator.getDashboard()).thenReturn(
            new DashboardResponse(Map.of(), Map.of(), Map.of(), Map.of(), Map.of(), List.of(), List.of()));
        var response = controller.getDashboard();
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testGetAlerts() {
        when(orchestrator.getActiveAlerts(any())).thenReturn(List.of());
        var response = controller.getAlerts("INVENTORY");
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testGetSupplyChain() {
        when(orchestrator.monitorSupplyChain(any(), any())).thenReturn(
            new SupplyChainResponse("OK", 80.0, List.of(), List.of()));
        var request = new SupplyChainRequest("Monitor", "supply_chain", null, null);
        var response = controller.getSupplyChain(request);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testGetKPIs() {
        when(orchestrator.getKPIs(any())).thenReturn(new KPIResponse(List.of(), Map.of()));
        var request = new KPIRequest(null, null);
        var response = controller.getKPIs(request);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testGetDecision() {
        when(orchestrator.getDecisionRecommendation(any())).thenReturn(
            new DecisionSupportResponse("Rec", 80.0, 0.0, 0.85, "LOW", List.of(), List.of()));
        var request = new DecisionSupportRequest("What to do?", "decision", null, null);
        var response = controller.getDecision(request);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testGetCostOptimization() {
        when(orchestrator.analyzeCosts(any())).thenReturn(
            new CostOptimizationResponse(1000000.0, Map.of(), List.of(), 200000.0));
        var request = new CostOptimizationRequest(null, null);
        var response = controller.getCostOptimization(request);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testHealth() {
        var response = controller.health();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().success());
    }
}
