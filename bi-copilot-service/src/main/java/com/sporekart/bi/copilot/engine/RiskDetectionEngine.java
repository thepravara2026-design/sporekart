package com.sporekart.bi.copilot.engine;

import com.sporekart.bi.copilot.domain.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class RiskDetectionEngine {

    private static final Logger log = LoggerFactory.getLogger(RiskDetectionEngine.class);

    private final Map<String, RiskAlert> activeRisks = new LinkedHashMap<>();

    public List<RiskAlert> detectRisks(String period) {
        log.info("Detecting risks for period={}", period);
        RevenueAnalytics revenue = seedRevenueAnalytics();
        CustomerAnalytics customers = seedCustomerAnalytics();
        TrainingAnalytics training = seedTrainingAnalytics();
        InventoryAnalytics inventory = seedInventoryAnalytics();

        List<RiskAlert> all = new ArrayList<>();
        all.addAll(detectRevenueDecline(revenue));
        all.addAll(detectCustomerChurn(customers));
        all.addAll(detectInventoryShortage(inventory));
        all.addAll(detectDemandSpikes(revenue));
        all.addAll(detectOperationalBottlenecks());
        all.addAll(detectTrainingCapacityIssues(training));
        all.addAll(detectPaymentAnomalies());
        all.forEach(r -> activeRisks.put(r.riskId(), r));
        return all;
    }

    public List<RiskAlert> detectRevenueDecline(RevenueAnalytics revenue) {
        List<RiskAlert> alerts = new ArrayList<>();
        if (revenue.revenueGrowth() < -15) {
            alerts.add(buildRisk("RISK-REV-001", "REVENUE_DECLINE", "CRITICAL",
                    "Critical revenue decline detected",
                    "Revenue growth of " + revenue.revenueGrowth() + "%% exceeds -15%% critical threshold",
                    0.85, 0.9, "Revenue", "Launch emergency sales campaign and review pricing strategy"));
        } else if (revenue.revenueGrowth() < 0) {
            alerts.add(buildRisk("RISK-REV-002", "REVENUE_DECLINE", "HIGH",
                    "Consecutive revenue decline detected",
                    revenue.revenueGrowth() + "%% growth suggests downward trend over 2+ months",
                    0.7, 0.75, "Revenue", "Review sales pipeline and customer acquisition channels"));
        }
        return alerts;
    }

    public List<RiskAlert> detectCustomerChurn(CustomerAnalytics customers) {
        List<RiskAlert> alerts = new ArrayList<>();
        double churnPct = customers.churnRate() * 100;
        if (customers.churnRate() > 0.15) {
            alerts.add(buildRisk("RISK-CUS-001", "CUSTOMER_CHURN", "CRITICAL",
                    "Critical customer churn rate",
                    "Churn rate of " + String.format("%.1f", churnPct) + "%% exceeds critical threshold of 15%%",
                    0.8, 0.85, "Customer", "Launch emergency retention program and conduct exit interviews"));
        } else if (customers.churnRate() > 0.10) {
            alerts.add(buildRisk("RISK-CUS-002", "CUSTOMER_CHURN", "HIGH",
                    "High customer churn risk",
                    "Churn rate of " + String.format("%.1f", churnPct) + "%% exceeds 10%% threshold",
                    0.75, 0.7, "Customer", "Analyze churn patterns and launch targeted retention campaign"));
        }
        if (customers.inactiveCustomers() > customers.totalCustomers() * 0.2) {
            double inactivePct = (double) customers.inactiveCustomers() / customers.totalCustomers() * 100;
            alerts.add(buildRisk("RISK-CUS-003", "CUSTOMER_CHURN", "HIGH",
                    "High inactive customer count",
                    customers.inactiveCustomers() + " customers inactive — " + String.format("%.0f", inactivePct) + "%% of base",
                    0.65, 0.6, "Customer", "Launch re-engagement campaign for inactive segment"));
        }
        return alerts;
    }

    public List<RiskAlert> detectInventoryShortage(InventoryAnalytics inventory) {
        List<RiskAlert> alerts = new ArrayList<>();
        if (inventory.lowStockItems() >= 10) {
            alerts.add(buildRisk("RISK-INV-001", "INVENTORY_SHORTAGE", "CRITICAL",
                    "Critical inventory shortage — multiple items",
                    inventory.lowStockItems() + " items below reorder level — risk of widespread stockouts",
                    0.85, 0.8, "Inventory", "Place emergency orders for all low-stock items and prioritize by sales velocity"));
        } else if (inventory.lowStockItems() >= 5) {
            alerts.add(buildRisk("RISK-INV-002", "INVENTORY_SHORTAGE", "HIGH",
                    "Multiple items below reorder level",
                    inventory.lowStockItems() + " items at risk of stockout — reorder needed",
                    0.75, 0.7, "Inventory", "Review reorder points and place replenishment orders"));
        }
        if (inventory.inventoryRisk() > 0.5) {
            alerts.add(buildRisk("RISK-INV-003", "INVENTORY_SHORTAGE", "MODERATE",
                    "Elevated inventory risk score",
                    "Overall inventory risk score is " + String.format("%.2f", inventory.inventoryRisk()),
                    0.55, 0.5, "Inventory", "Conduct full inventory audit and review stock policies"));
        }
        return alerts;
    }

    public List<RiskAlert> detectDemandSpikes(RevenueAnalytics revenue) {
        List<RiskAlert> alerts = new ArrayList<>();
        if (revenue.revenueGrowth() > 25) {
            alerts.add(buildRisk("RISK-DEM-001", "DEMAND_SPIKE", "HIGH",
                    "Demand surge detected",
                    "Revenue grew " + revenue.revenueGrowth() + "%% — supply chain may be strained",
                    0.7, 0.65, "Supply Chain", "Increase production capacity and secure additional supplier contracts"));
        }
        if (revenue.orderCount() > 350) {
            alerts.add(buildRisk("RISK-DEM-002", "DEMAND_SPIKE", "MODERATE",
                    "Order volume spike detected",
                    "Order count of " + revenue.orderCount() + " exceeds normal range",
                    0.6, 0.55, "Operations", "Review fulfillment capacity and consider temporary staffing increase"));
        }
        return alerts;
    }

    public List<RiskAlert> detectOperationalBottlenecks() {
        return List.of(
                buildRisk("RISK-OPS-001", "OPERATIONAL_BOTTLENECK", "MODERATE",
                        "Warehouse capacity approaching limit",
                        "Current warehouse utilization at 82%% — expansion planning recommended within 60 days",
                        0.45, 0.5, "Operations", "Evaluate warehouse expansion options and optimize storage layout"),
                buildRisk("RISK-OPS-002", "OPERATIONAL_BOTTLENECK", "LOW",
                        "Order fulfillment time increasing",
                        "Average fulfillment time increased from 2.1 to 2.8 days over last quarter",
                        0.35, 0.4, "Operations", "Review picking and packing processes for efficiency improvements")
        );
    }

    public List<RiskAlert> detectTrainingCapacityIssues(TrainingAnalytics training) {
        List<RiskAlert> alerts = new ArrayList<>();
        training.enrollmentByCourse().forEach((course, enrolled) -> {
            int capacity = 40;
            double utilization = (double) enrolled / capacity * 100;
            if (utilization >= 95) {
                alerts.add(buildRisk("RISK-TRN-001", "TRAINING_CAPACITY", "HIGH",
                        "Course at full capacity: " + course,
                        enrolled + "/" + capacity + " enrolled — " + String.format("%.0f", utilization) + "%% utilization",
                        0.8, 0.7, "Training", "Schedule additional batch for " + course + " and increase capacity"));
            } else if (utilization >= 90) {
                alerts.add(buildRisk("RISK-TRN-002", "TRAINING_CAPACITY", "MODERATE",
                        "Course nearing capacity: " + course,
                        enrolled + "/" + capacity + " enrolled — " + String.format("%.0f", utilization) + "%% utilization",
                        0.6, 0.5, "Training", "Plan additional batch for " + course + " before next enrollment cycle"));
            }
        });
        return alerts;
    }

    public List<RiskAlert> detectPaymentAnomalies() {
        return List.of(
                buildRisk("RISK-PAY-001", "PAYMENT_ANOMALY", "MODERATE",
                        "Refund rate increase detected",
                        "Current refund rate of 5.3%% exceeds 3%% threshold — investigate root cause",
                        0.5, 0.6, "Finance", "Audit recent refunds for patterns and contact affected customers")
        );
    }

    public List<RiskAlert> getActiveRisks() {
        return List.copyOf(activeRisks.values());
    }

    public Map<String, Object> getRiskSummary() {
        var all = getActiveRisks();
        if (all.isEmpty()) {
            all = detectRisks("current");
        }

        Map<String, Long> byType = all.stream()
                .collect(Collectors.groupingBy(RiskAlert::riskType, Collectors.counting()));
        Map<String, Long> bySeverity = all.stream()
                .collect(Collectors.groupingBy(RiskAlert::severity, Collectors.counting()));

        return Map.of(
                "totalRisks", all.size(),
                "byType", byType,
                "bySeverity", bySeverity,
                "criticalCount", bySeverity.getOrDefault("CRITICAL", 0L),
                "highCount", bySeverity.getOrDefault("HIGH", 0L),
                "moderateCount", bySeverity.getOrDefault("MODERATE", 0L),
                "lowCount", bySeverity.getOrDefault("LOW", 0L),
                "risks", all
        );
    }

    public RiskAlert mitigateRisk(String riskId, String action) {
        RiskAlert existing = activeRisks.get(riskId);
        if (existing != null) {
            RiskAlert mitigated = new RiskAlert(
                    existing.riskId(), existing.riskType(), existing.severity(),
                    existing.title(), existing.description() + " [Mitigation: " + action + "]",
                    existing.probability(), existing.impact(), existing.affectedArea(),
                    existing.recommendedAction(), "MITIGATED", OffsetDateTime.now());
            activeRisks.put(riskId, mitigated);
            return mitigated;
        }
        log.warn("Risk {} not found for mitigation", riskId);
        return null;
    }

    private RiskAlert buildRisk(String id, String type, String severity, String title,
                                String description, double probability, double impact,
                                String area, String action) {
        return new RiskAlert(id, type, severity, title, description, probability, impact,
                area, action, "ACTIVE", OffsetDateTime.now());
    }

    private RevenueAnalytics seedRevenueAnalytics() {
        return new RevenueAnalytics("current", 1250000.0, 1125000.0, 25000.0, 4500.0, -8.5,
                Map.of(), Map.of(), Map.of(), Map.of(), Map.of(), Map.of(), 1350000.0, 280, 15);
    }

    private CustomerAnalytics seedCustomerAnalytics() {
        return new CustomerAnalytics("current", 2500, 180, 1200, 85, 0.72, 0.12, 8500.0, 0.48, 320,
                List.of(), Map.of(), Map.of());
    }

    private TrainingAnalytics seedTrainingAnalytics() {
        return new TrainingAnalytics("current", 12, 8, 4, 240, 0.85, 78.5, 0.82, 180,
                List.of(),
                Map.of(),
                Map.of("Mushroom Farming 101", 38, "Commercial Mushroom Farming", 36,
                        "Advanced Techniques", 39, "Composting Workshop", 35, "Pest Management", 25));
    }

    private InventoryAnalytics seedInventoryAnalytics() {
        return new InventoryAnalytics("current", 15000, 8, 45,
                List.of(), List.of(), 6.5, List.of(), 0.35);
    }
}
