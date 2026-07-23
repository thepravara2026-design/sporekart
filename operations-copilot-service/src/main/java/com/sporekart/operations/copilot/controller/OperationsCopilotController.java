package com.sporekart.operations.copilot.controller;

import com.sporekart.operations.copilot.dto.*;
import com.sporekart.operations.copilot.service.OperationsOrchestrator;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/copilot/operations")
public class OperationsCopilotController {

    private static final Logger log = LoggerFactory.getLogger(OperationsCopilotController.class);

    private final OperationsOrchestrator orchestrator;

    public OperationsCopilotController(OperationsOrchestrator orchestrator) {
        this.orchestrator = orchestrator;
    }

    @PostMapping("/query")
    public ResponseEntity<OperationsCopilotResponse<OperationsQueryResponse>> handleQuery(
            @Valid @RequestBody OperationsQueryRequest request) {
        log.info("REST: operations query intent={}", request.intent());
        var response = orchestrator.routeQuery(request);
        return ResponseEntity.ok(OperationsCopilotResponse.success(response));
    }

    @PostMapping("/inventory")
    public ResponseEntity<OperationsCopilotResponse<InventoryStatusResponse>> getInventory(
            @RequestParam(required = false) String warehouseId,
            @RequestParam(required = false) String category) {
        log.info("REST: inventory status warehouse={} category={}", warehouseId, category);
        var response = orchestrator.getInventoryStatus(warehouseId, category);
        return ResponseEntity.ok(OperationsCopilotResponse.success(response));
    }

    @PostMapping("/procurement")
    public ResponseEntity<OperationsCopilotResponse<ProcurementResponse>> getProcurement(
            @Valid @RequestBody ProcurementRequest request) {
        log.info("REST: procurement recommendation");
        var response = orchestrator.recommendProcurement(request);
        return ResponseEntity.ok(OperationsCopilotResponse.success(response));
    }

    @PostMapping("/orders")
    public ResponseEntity<OperationsCopilotResponse<OrderOperationsResponse>> getOrders(
            @RequestParam(required = false) String warehouseId,
            @RequestParam(required = false) String status) {
        log.info("REST: order operations warehouse={}", warehouseId);
        var response = orchestrator.analyzeOrderQueue(warehouseId, status);
        return ResponseEntity.ok(OperationsCopilotResponse.success(response));
    }

    @PostMapping("/warehouse")
    public ResponseEntity<OperationsCopilotResponse<WarehouseResponse>> getWarehouse(
            @RequestParam(required = false) String warehouseId) {
        log.info("REST: warehouse analysis id={}", warehouseId);
        var response = orchestrator.analyzeWarehouse(warehouseId);
        return ResponseEntity.ok(OperationsCopilotResponse.success(response));
    }

    @PostMapping("/logistics")
    public ResponseEntity<OperationsCopilotResponse<LogisticsResponse>> getLogistics(
            @Valid @RequestBody LogisticsRequest request) {
        log.info("REST: logistics planning");
        var response = orchestrator.planShipment(request);
        return ResponseEntity.ok(OperationsCopilotResponse.success(response));
    }

    @PostMapping("/forecast")
    public ResponseEntity<OperationsCopilotResponse<ForecastResponse>> getForecast(
            @Valid @RequestBody ForecastRequest request) {
        log.info("REST: demand forecast product={}", request.productId());
        var response = orchestrator.forecastDemand(request);
        return ResponseEntity.ok(OperationsCopilotResponse.success(response));
    }

    @GetMapping("/dashboard")
    public ResponseEntity<OperationsCopilotResponse<DashboardResponse>> getDashboard() {
        log.info("REST: operations dashboard");
        var response = orchestrator.getDashboard();
        return ResponseEntity.ok(OperationsCopilotResponse.success(response));
    }

    @GetMapping("/alerts")
    public ResponseEntity<OperationsCopilotResponse<?>> getAlerts(
            @RequestParam(required = false) String category) {
        log.info("REST: operations alerts category={}", category);
        var alerts = orchestrator.getActiveAlerts(category);
        return ResponseEntity.ok(OperationsCopilotResponse.success(alerts));
    }

    @PostMapping("/supply-chain")
    public ResponseEntity<OperationsCopilotResponse<SupplyChainResponse>> getSupplyChain(
            @Valid @RequestBody SupplyChainRequest request) {
        log.info("REST: supply chain monitoring");
        var response = orchestrator.monitorSupplyChain(request.vendorId(), request.region());
        return ResponseEntity.ok(OperationsCopilotResponse.success(response));
    }

    @PostMapping("/kpi")
    public ResponseEntity<OperationsCopilotResponse<KPIResponse>> getKPIs(
            @Valid @RequestBody KPIRequest request) {
        log.info("REST: operational KPIs");
        var response = orchestrator.getKPIs(request);
        return ResponseEntity.ok(OperationsCopilotResponse.success(response));
    }

    @PostMapping("/decision")
    public ResponseEntity<OperationsCopilotResponse<DecisionSupportResponse>> getDecision(
            @Valid @RequestBody DecisionSupportRequest request) {
        log.info("REST: decision support");
        var response = orchestrator.getDecisionRecommendation(request);
        return ResponseEntity.ok(OperationsCopilotResponse.success(response));
    }

    @PostMapping("/cost-optimization")
    public ResponseEntity<OperationsCopilotResponse<CostOptimizationResponse>> getCostOptimization(
            @Valid @RequestBody CostOptimizationRequest request) {
        log.info("REST: cost optimization");
        var response = orchestrator.analyzeCosts(request);
        return ResponseEntity.ok(OperationsCopilotResponse.success(response));
    }

    @GetMapping("/health")
    public ResponseEntity<OperationsCopilotResponse<String>> health() {
        return ResponseEntity.ok(OperationsCopilotResponse.success("Operations Copilot is operational"));
    }
}
