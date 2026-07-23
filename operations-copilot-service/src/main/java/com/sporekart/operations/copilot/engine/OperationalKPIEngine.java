package com.sporekart.operations.copilot.engine;

import com.sporekart.operations.copilot.domain.OperationalKPI;
import com.sporekart.operations.copilot.domain.OperationalKPI.KpiTrend;
import com.sporekart.operations.copilot.dto.KPIRequest;
import com.sporekart.operations.copilot.dto.KPIResponse;
import com.sporekart.operations.copilot.dto.KPIResponse.KPIData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class OperationalKPIEngine {

    private static final Logger log = LoggerFactory.getLogger(OperationalKPIEngine.class);

    public KPIResponse getKPIs(KPIRequest request) {
        log.info("Getting KPIs for category: {} period: {}", request.category(), request.period());
        var domainKpis = generateKPIList();
        if (request.category() != null && !request.category().isBlank()) {
            domainKpis = domainKpis.stream().filter(k -> k.category().equalsIgnoreCase(request.category())).toList();
        }
        var kpiDataList = domainKpis.stream().map(k -> new KPIData(k.name(), k.category(), k.currentValue(),
            k.targetValue(), k.previousValue(), k.unit(), k.trend().name())).toList();
        var summary = new LinkedHashMap<String, Object>();
        summary.put("totalKPIs", kpiDataList.size());
        summary.put("improvingKPIs", domainKpis.stream().filter(k -> k.trend() == KpiTrend.IMPROVING).count());
        summary.put("decliningKPIs", domainKpis.stream().filter(k -> k.trend() == KpiTrend.DECLINING).count());
        summary.put("criticalKPIs", domainKpis.stream().filter(k -> k.trend() == KpiTrend.CRITICAL).count());
        summary.put("overallHealth", calculateOverallHealth(kpiDataList));
        return new KPIResponse(kpiDataList, summary);
    }

    public List<OperationalKPI> generateKPIList() {
        return List.of(
            new OperationalKPI("KPI-001", "Order Fulfillment Rate", "Fulfillment", 94.5, 98.0, 93.0, "%", KpiTrend.IMPROVING, LocalDate.now()),
            new OperationalKPI("KPI-002", "Inventory Accuracy", "Inventory", 97.2, 99.0, 96.5, "%", KpiTrend.IMPROVING, LocalDate.now()),
            new OperationalKPI("KPI-003", "Warehouse Efficiency", "Warehouse", 87.5, 92.0, 86.0, "%", KpiTrend.IMPROVING, LocalDate.now()),
            new OperationalKPI("KPI-004", "Average Processing Time", "Fulfillment", 4.5, 3.0, 5.0, "hours", KpiTrend.IMPROVING, LocalDate.now()),
            new OperationalKPI("KPI-005", "Average Delivery Time", "Logistics", 3.2, 2.5, 3.5, "days", KpiTrend.DECLINING, LocalDate.now()),
            new OperationalKPI("KPI-006", "Inventory Days", "Inventory", 45.0, 30.0, 42.0, "days", KpiTrend.DECLINING, LocalDate.now()),
            new OperationalKPI("KPI-007", "Stock Availability", "Inventory", 92.0, 95.0, 91.0, "%", KpiTrend.IMPROVING, LocalDate.now()),
            new OperationalKPI("KPI-008", "Operational Cost", "Cost", 125000.0, 100000.0, 130000.0, "INR", KpiTrend.IMPROVING, LocalDate.now()),
            new OperationalKPI("KPI-009", "Order SLA Compliance", "Fulfillment", 88.0, 95.0, 86.0, "%", KpiTrend.IMPROVING, LocalDate.now()),
            new OperationalKPI("KPI-010", "Shipping SLA Compliance", "Logistics", 85.0, 92.0, 84.0, "%", KpiTrend.DECLINING, LocalDate.now())
        );
    }

    public OperationalKPI getKPIDetail(String kpiId) {
        log.debug("Getting KPI detail for: {}", kpiId);
        return new OperationalKPI(kpiId, "Order Fulfillment Rate", "Fulfillment", 94.5, 98.0, 93.0, "%", KpiTrend.IMPROVING, LocalDate.now());
    }

    public List<OperationalKPI> getKPIsByCategory(String category) {
        log.debug("Getting KPIs by category: {}", category);
        return generateKPIList().stream()
            .filter(k -> k.category().equalsIgnoreCase(category))
            .toList();
    }

    public Map<String, Object> getSLACompliance() {
        log.debug("Getting SLA compliance data");
        var sla = new LinkedHashMap<String, Object>();
        sla.put("orderSLA", Map.of("compliance", 88.0, "target", 95.0, "breached", 12, "total", 100));
        sla.put("shippingSLA", Map.of("compliance", 85.0, "target", 92.0, "breached", 15, "total", 100));
        sla.put("fulfillmentSLA", Map.of("compliance", 91.0, "target", 95.0, "breached", 9, "total", 100));
        return sla;
    }

    private double calculateOverallHealth(List<KPIData> kpis) {
        if (kpis.isEmpty()) return 0.0;
        var avg = kpis.stream().mapToDouble(k -> k.current() / k.target() * 100).average().orElse(0.0);
        return Math.round(Math.min(100, avg) * 10.0) / 10.0;
    }
}
