package com.sporekart.operations.copilot.service;

import com.sporekart.operations.copilot.domain.*;
import com.sporekart.operations.copilot.dto.*;
import com.sporekart.operations.copilot.engine.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OperationsOrchestrator {

    private static final Logger log = LoggerFactory.getLogger(OperationsOrchestrator.class);

    private final InventoryEngine inventoryEngine;
    private final ProcurementEngine procurementEngine;
    private final OrderOperationsEngine orderOperationsEngine;
    private final WarehouseEngine warehouseEngine;
    private final LogisticsEngine logisticsEngine;
    private final DemandPlanningEngine demandPlanningEngine;
    private final SupplyChainMonitorEngine supplyChainMonitorEngine;
    private final OperationalKPIEngine operationalKPIEngine;
    private final DecisionSupportEngine decisionSupportEngine;
    private final CostOptimizationEngine costOptimizationEngine;

    public OperationsOrchestrator(InventoryEngine inventoryEngine, ProcurementEngine procurementEngine,
                                   OrderOperationsEngine orderOperationsEngine, WarehouseEngine warehouseEngine,
                                   LogisticsEngine logisticsEngine, DemandPlanningEngine demandPlanningEngine,
                                   SupplyChainMonitorEngine supplyChainMonitorEngine,
                                   OperationalKPIEngine operationalKPIEngine,
                                   DecisionSupportEngine decisionSupportEngine,
                                   CostOptimizationEngine costOptimizationEngine) {
        this.inventoryEngine = inventoryEngine;
        this.procurementEngine = procurementEngine;
        this.orderOperationsEngine = orderOperationsEngine;
        this.warehouseEngine = warehouseEngine;
        this.logisticsEngine = logisticsEngine;
        this.demandPlanningEngine = demandPlanningEngine;
        this.supplyChainMonitorEngine = supplyChainMonitorEngine;
        this.operationalKPIEngine = operationalKPIEngine;
        this.decisionSupportEngine = decisionSupportEngine;
        this.costOptimizationEngine = costOptimizationEngine;
    }

    public OperationsQueryResponse routeQuery(OperationsQueryRequest request) {
        var startTime = System.currentTimeMillis();
        var queryId = UUID.randomUUID().toString();
        log.info("Routing operations query: intent={} query={}", request.intent(), request.query());

        Map<String, Object> data;
        List<String> recommendations;
        List<String> alerts;
        String summary;

        switch (request.intent().toLowerCase()) {
            case "inventory":
            case "inventory_status":
                var invStatus = inventoryEngine.getInventoryStatus(request.warehouseId(), request.category());
                data = Map.of("inventory", invStatus);
                recommendations = inventoryEngine.generateRestockSuggestions(request.warehouseId());
                alerts = List.of("Critical: MUSH-005 stock at 5 units", "Warning: MUSH-001 below reorder point");
                summary = "Inventory status: " + invStatus.totalSkuCount() + " SKUs, " + invStatus.criticalCount() + " critical";
                break;

            case "inventory_alerts":
                var invAlerts = inventoryEngine.getAlerts(request.warehouseId());
                data = Map.of("alerts", invAlerts);
                recommendations = invAlerts.alerts().stream().map(a -> a.severity() + ": " + a.message()).toList();
                alerts = invAlerts.alerts().stream().map(a -> "[" + a.severity() + "] " + a.productName() + " - " + a.message()).toList();
                summary = invAlerts.totalAlerts() + " inventory alerts (" + invAlerts.criticalAlerts() + " critical)";
                break;

            case "procurement":
                var procReq = new ProcurementRequest(request.query(), request.intent(), null, request.category(), "standard");
                var procResp = procurementEngine.recommendProcurement(procReq);
                data = Map.of("procurement", procResp);
                recommendations = procResp.actionItems();
                alerts = List.of();
                summary = procResp.recommendation();
                break;

            case "orders":
            case "order_ops":
                var orderResp = orderOperationsEngine.analyzeOrderQueue(request.warehouseId(), null);
                data = Map.of("orders", orderResp);
                recommendations = orderResp.recommendations();
                alerts = orderResp.bottlenecks();
                summary = "Order queue: " + orderResp.fulfillmentRate() + "% fulfillment rate";
                break;

            case "warehouse":
                var whResp = warehouseEngine.analyzeWarehouse(request.warehouseId());
                data = Map.of("warehouse", whResp);
                recommendations = whResp.recommendations();
                alerts = List.of("Zone-A at 85% utilization");
                summary = "Warehouse " + whResp.warehouseId() + " health: " + whResp.health();
                break;

            case "logistics":
            case "shipping":
                var logReq = new LogisticsRequest(request.query(), request.intent(), null, request.region(), null, null);
                var logResp = logisticsEngine.planShipment(logReq);
                data = Map.of("logistics", logResp);
                recommendations = List.of("Use " + logResp.recommendation());
                alerts = logResp.alerts();
                summary = "Logistics plan: " + logResp.estimatedDays() + " days, Rs. " + logResp.estimatedCost();
                break;

            case "forecast":
            case "demand":
                var fcReq = new ForecastRequest(request.category(), request.region(), 30);
                var fcResp = demandPlanningEngine.forecastDemand(fcReq);
                data = Map.of("forecast", fcResp);
                recommendations = fcResp.recommendations();
                alerts = List.of();
                summary = "Demand forecast: " + fcResp.predictedDemand() + " units predicted, " + fcResp.confidence() + "% confidence";
                break;

            case "supply_chain":
                var scResp = supplyChainMonitorEngine.monitorSupplyChain(null, request.region());
                data = Map.of("supplyChain", scResp);
                recommendations = scResp.recommendations();
                alerts = scResp.risks().stream().map(r -> "[" + r.severity() + "] " + r.title()).toList();
                summary = "Supply chain health: " + scResp.healthScore() + "/100";
                break;

            case "kpi":
            case "metrics":
                var kpiResp = operationalKPIEngine.getKPIs(new KPIRequest(request.category(), null));
                data = Map.of("kpis", kpiResp);
                recommendations = kpiResp.kpis().stream()
                    .filter(k -> k.trend().equals("DECLINING") || k.trend().equals("CRITICAL"))
                    .map(k -> "KPI " + k.name() + " is " + k.trend() + " (current: " + k.current() + ", target: " + k.target() + ")")
                    .toList();
                alerts = kpiResp.kpis().stream()
                    .filter(k -> k.trend().equals("CRITICAL"))
                    .map(k -> "CRITICAL: " + k.name())
                    .toList();
                summary = "Operational KPIs: " + kpiResp.kpis().size() + " metrics tracked";
                break;

            case "decision":
            case "recommend":
                var decReq = new DecisionSupportRequest(request.query(), request.intent(), request.category(), null);
                var decResp = decisionSupportEngine.getRecommendation(decReq);
                data = Map.of("decision", decResp);
                recommendations = List.of(decResp.recommendation());
                alerts = List.of();
                summary = "Decision recommendation: " + decResp.recommendation();
                break;

            case "cost":
            case "cost_optimization":
                var costResp = costOptimizationEngine.analyzeCosts(new CostOptimizationRequest(request.category(), null));
                data = Map.of("costOptimization", costResp);
                recommendations = costResp.opportunities().stream()
                    .map(o -> o.suggestion() + " (Savings: Rs. " + o.estimatedSavings() + ")")
                    .toList();
                alerts = List.of();
                summary = "Total operational cost: Rs. " + costResp.totalOperationalCost() + ", potential savings: Rs. " + costResp.totalPotentialSavings();
                break;

            default:
                data = Map.of("message", "Unknown intent: " + request.intent());
                recommendations = List.of("Available intents: inventory, procurement, orders, warehouse, logistics, forecast, supply_chain, kpi, decision, cost");
                alerts = List.of();
                summary = "Unrecognized operations intent: " + request.intent();
        }

        var processingTime = System.currentTimeMillis() - startTime;
        return new OperationsQueryResponse(queryId, request.intent(), summary, data, recommendations, alerts, processingTime);
    }

    public DashboardResponse getDashboard() {
        var invStatus = inventoryEngine.getInventoryStatus(null, null);
        var orderOps = orderOperationsEngine.analyzeOrderQueue(null, null);
        var wh = warehouseEngine.analyzeWarehouse(null);
        var kpis = operationalKPIEngine.getKPIs(new KPIRequest(null, null));

        Map<String, Object> invSummary = new LinkedHashMap<>();
        invSummary.put("totalSkuCount", invStatus.totalSkuCount());
        invSummary.put("lowStock", invStatus.lowStockCount());
        invSummary.put("critical", invStatus.criticalCount());
        invSummary.put("outOfStock", invStatus.outOfStockCount());
        invSummary.put("totalValue", invStatus.totalInventoryValue());
        Map<String, Object> orderSummary = new LinkedHashMap<>();
        orderSummary.put("pending", orderOps.orderSummary().get("pending"));
        orderSummary.put("fulfillmentRate", orderOps.fulfillmentRate());
        Map<String, Object> whSummary = new LinkedHashMap<>();
        whSummary.put("warehouse", wh.warehouseId());
        whSummary.put("health", wh.health());
        whSummary.put("efficiency", wh.efficiency());
        Map<String, Object> logSummary = new LinkedHashMap<>();
        logSummary.put("activeShipments", 45);
        logSummary.put("avgDeliveryDays", 3.2);
        Map<String, Object> kpiSummary = new LinkedHashMap<>();
        kpiSummary.put("overallHealth", kpis.summary().get("overallHealth"));

        var alerts = List.of(
            new DashboardResponse.AlertItem("ALT-001", "MUSH-005 critically low", "INVENTORY", "HIGH", "Only 5 units remaining"),
            new DashboardResponse.AlertItem("ALT-002", "Packing bottleneck detected", "ORDER", "MEDIUM", "Queue at 85% capacity")
        );
        var recs = List.of(
            new DashboardResponse.RecommendationItem("REC-001", "Restock MUSH-005 urgently", "INVENTORY", 90.0, 0.95),
            new DashboardResponse.RecommendationItem("REC-002", "Optimize packing workflow", "PROCESS", 75.0, 0.88)
        );
        return new DashboardResponse(invSummary, orderSummary, whSummary, logSummary, kpiSummary, alerts, recs);
    }

    public List<OperationsAlert> getActiveAlerts(String category) {
        var alerts = new ArrayList<OperationsAlert>();
        alerts.add(new OperationsAlert("ALT-001", "Critical Stock Alert", "MUSH-005 stock at 5 units",
            OperationsAlert.AlertCategory.INVENTORY, OperationsAlert.AlertPriority.URGENT, true,
            "Create emergency purchase order", "MUSH-005", java.time.LocalDateTime.now(), false));
        alerts.add(new OperationsAlert("ALT-002", "Packing Bottleneck", "Packing station at 85% capacity",
            OperationsAlert.AlertCategory.ORDER, OperationsAlert.AlertPriority.HIGH, true,
            "Add temporary packing station", "WH-MAIN-ZONE-B", java.time.LocalDateTime.now(), false));
        alerts.add(new OperationsAlert("ALT-003", "Warehouse Capacity", "Zone-A at 92% capacity",
            OperationsAlert.AlertCategory.WAREHOUSE, OperationsAlert.AlertPriority.MEDIUM, true,
            "Review storage allocation", "WH-MAIN-ZONE-A", java.time.LocalDateTime.now(), false));
        if (category != null && !category.isBlank()) {
            return alerts.stream().filter(a -> a.category().name().equalsIgnoreCase(category)).toList();
        }
        return alerts;
    }

    // Delegation methods
    public InventoryStatusResponse getInventoryStatus(String warehouseId, String category) {
        return inventoryEngine.getInventoryStatus(warehouseId, category);
    }
    public InventoryAlertResponse getInventoryAlerts(String warehouseId) {
        return inventoryEngine.getAlerts(warehouseId);
    }
    public ProcurementResponse recommendProcurement(ProcurementRequest request) {
        return procurementEngine.recommendProcurement(request);
    }
    public OrderOperationsResponse analyzeOrderQueue(String warehouseId, String status) {
        return orderOperationsEngine.analyzeOrderQueue(warehouseId, status);
    }
    public WarehouseResponse analyzeWarehouse(String warehouseId) {
        return warehouseEngine.analyzeWarehouse(warehouseId);
    }
    public LogisticsResponse planShipment(LogisticsRequest request) {
        return logisticsEngine.planShipment(request);
    }
    public ForecastResponse forecastDemand(ForecastRequest request) {
        return demandPlanningEngine.forecastDemand(request);
    }
    public SupplyChainResponse monitorSupplyChain(String vendorId, String region) {
        return supplyChainMonitorEngine.monitorSupplyChain(vendorId, region);
    }
    public KPIResponse getKPIs(KPIRequest request) {
        return operationalKPIEngine.getKPIs(request);
    }
    public DecisionSupportResponse getDecisionRecommendation(DecisionSupportRequest request) {
        return decisionSupportEngine.getRecommendation(request);
    }
    public CostOptimizationResponse analyzeCosts(CostOptimizationRequest request) {
        return costOptimizationEngine.analyzeCosts(request);
    }
}
