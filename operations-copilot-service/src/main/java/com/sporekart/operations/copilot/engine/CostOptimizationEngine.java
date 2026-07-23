package com.sporekart.operations.copilot.engine;

import com.sporekart.operations.copilot.domain.ProcessOptimization;
import com.sporekart.operations.copilot.dto.CostOptimizationRequest;
import com.sporekart.operations.copilot.dto.CostOptimizationResponse;
import com.sporekart.operations.copilot.dto.CostOptimizationResponse.SavingsOpportunity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CostOptimizationEngine {

    private static final Logger log = LoggerFactory.getLogger(CostOptimizationEngine.class);

    public CostOptimizationResponse analyzeCosts(CostOptimizationRequest request) {
        log.info("Analyzing operational costs for category: {} period: {}", request.category(), request.period());
        var costBreakdown = new LinkedHashMap<String, Double>();
        costBreakdown.put("inventory_holding", 450000.0);
        costBreakdown.put("procurement", 1250000.0);
        costBreakdown.put("warehouse_operations", 680000.0);
        costBreakdown.put("shipping", 920000.0);
        costBreakdown.put("labor", 1100000.0);
        costBreakdown.put("technology", 350000.0);
        var totalCost = costBreakdown.values().stream().mapToDouble(Double::doubleValue).sum();

        var opportunities = List.of(
            new SavingsOpportunity("Inventory", "Reduce dead stock through clearance sales", 125000.0, 1.0, "LOW"),
            new SavingsOpportunity("Procurement", "Negotiate bulk discounts with top 3 vendors", 185000.0, 2.0, "LOW"),
            new SavingsOpportunity("Warehouse", "Optimize bin utilization to reduce storage needs", 95000.0, 2.0, "LOW"),
            new SavingsOpportunity("Shipping", "Switch to economical courier for non-urgent orders", 145000.0, 1.0, "LOW"),
            new SavingsOpportunity("Labor", "Implement automated picking for high-volume items", 220000.0, 3.0, "MEDIUM")
        );
        var totalSavings = opportunities.stream().mapToDouble(SavingsOpportunity::estimatedSavings).sum();
        return new CostOptimizationResponse(totalCost, costBreakdown, opportunities, totalSavings);
    }

    public List<ProcessOptimization> optimizeProcesses(String area) {
        log.info("Optimizing processes in area: {}", area);
        return List.of(
            new ProcessOptimization("OPT-001", "Order Picking", "Manual zone picking", "Automated batch picking",
                250000.0, 800000.0, 35.0, "3 months",
                List.of("Install conveyor system", "Upgrade WMS software", "Train staff"), "MEDIUM"),
            new ProcessOptimization("OPT-002", "Inventory Counting", "Annual physical count", "Cycle counting with RFID",
                180000.0, 450000.0, 60.0, "2 months",
                List.of("Deploy RFID readers", "Implement cycle counting schedule", "Update inventory system"), "LOW"),
            new ProcessOptimization("OPT-003", "Returns Processing", "Manual inspection", "Automated QC station",
                95000.0, 300000.0, 40.0, "6 weeks",
                List.of("Set up QC station", "Train returns team", "Implement RMA system"), "LOW")
        ).stream().filter(p -> area == null || p.processName().toLowerCase().contains(area.toLowerCase())).toList();
    }

    public Map<String, Object> getCostTrends(String period) {
        log.debug("Getting cost trends for period: {}", period);
        var trends = new LinkedHashMap<String, Object>();
        trends.put("period", period != null ? period : "last_quarter");
        trends.put("totalCost", 4750000.0);
        trends.put("trend", "INCREASING");
        trends.put("changePct", 8.5);
        trends.put("breakdown", Map.of(
            "inventory_holding", 9.5,
            "procurement", 12.0,
            "shipping", 7.2,
            "labor", 5.8
        ));
        return trends;
    }

    public double calculateSavings(String optimizationId, double currentCost, double projectedCost) {
        return Math.round((currentCost - projectedCost) * 100.0) / 100.0;
    }
}
