package com.sporekart.operations.copilot.infrastructure.monitoring;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Service;

@Service
public class OperationsMetricsService {

    private final MeterRegistry meterRegistry;

    private final Counter inventoryRequests;
    private final Counter procurementRequests;
    private final Counter orderRequests;
    private final Counter warehouseRequests;
    private final Counter logisticsRequests;
    private final Counter forecastRequests;
    private final Counter supplyChainRequests;
    private final Counter kpiRequests;
    private final Counter decisionRequests;
    private final Counter costRequests;
    private final Counter totalRequests;
    private final Counter errors;

    public OperationsMetricsService(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;

        this.inventoryRequests = Counter.builder("operations.copilot.inventory.requests")
            .description("Inventory intelligence requests").register(meterRegistry);
        this.procurementRequests = Counter.builder("operations.copilot.procurement.requests")
            .description("Procurement requests").register(meterRegistry);
        this.orderRequests = Counter.builder("operations.copilot.order.requests")
            .description("Order operations requests").register(meterRegistry);
        this.warehouseRequests = Counter.builder("operations.copilot.warehouse.requests")
            .description("Warehouse intelligence requests").register(meterRegistry);
        this.logisticsRequests = Counter.builder("operations.copilot.logistics.requests")
            .description("Logistics requests").register(meterRegistry);
        this.forecastRequests = Counter.builder("operations.copilot.forecast.requests")
            .description("Demand forecast requests").register(meterRegistry);
        this.supplyChainRequests = Counter.builder("operations.copilot.supplychain.requests")
            .description("Supply chain monitoring requests").register(meterRegistry);
        this.kpiRequests = Counter.builder("operations.copilot.kpi.requests")
            .description("KPI requests").register(meterRegistry);
        this.decisionRequests = Counter.builder("operations.copilot.decision.requests")
            .description("Decision support requests").register(meterRegistry);
        this.costRequests = Counter.builder("operations.copilot.cost.requests")
            .description("Cost optimization requests").register(meterRegistry);
        this.totalRequests = Counter.builder("operations.copilot.total.requests")
            .description("Total operations copilot requests").register(meterRegistry);
        this.errors = Counter.builder("operations.copilot.error.count")
            .description("Operations copilot error count").register(meterRegistry);
    }

    public void recordInventoryRequest() { inventoryRequests.increment(); totalRequests.increment(); }
    public void recordProcurementRequest() { procurementRequests.increment(); totalRequests.increment(); }
    public void recordOrderRequest() { orderRequests.increment(); totalRequests.increment(); }
    public void recordWarehouseRequest() { warehouseRequests.increment(); totalRequests.increment(); }
    public void recordLogisticsRequest() { logisticsRequests.increment(); totalRequests.increment(); }
    public void recordForecastRequest() { forecastRequests.increment(); totalRequests.increment(); }
    public void recordSupplyChainRequest() { supplyChainRequests.increment(); totalRequests.increment(); }
    public void recordKPIRequest() { kpiRequests.increment(); totalRequests.increment(); }
    public void recordDecisionRequest() { decisionRequests.increment(); totalRequests.increment(); }
    public void recordCostRequest() { costRequests.increment(); totalRequests.increment(); }
    public void recordError() { errors.increment(); }

    public Timer.Sample startTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopTimer(Timer.Sample sample, String operation) {
        sample.stop(Timer.builder("operations.copilot." + operation + ".duration")
            .description("Operations copilot " + operation + " duration")
            .register(meterRegistry));
    }

    public Counter getTotalRequests() { return totalRequests; }
    public Counter getErrors() { return errors; }
}
