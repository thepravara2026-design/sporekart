package com.sporekart.alert.config;

import com.sporekart.alert.domain.model.*;
import com.sporekart.alert.domain.repository.AlertRepositoryPort;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class AlertDataSeeder {

    private final AlertRepositoryPort repository;

    public AlertDataSeeder(AlertRepositoryPort repository) { this.repository = repository; }

    @PostConstruct
    public void seed() {
        seedAlerts();
        seedRisks();
        seedAnomalies();
        seedTimeline();
    }

    private void seedAlerts() {
        repository.saveAlert(Alert.create("Revenue Drop Detected", "Revenue dropped 15% below forecast in last 24 hours",
                AlertCategory.BUSINESS, AlertSeverity.CRITICAL, AlertPriority.P0, "REVENUE",
                "Potential $45K revenue loss if not addressed", "Investigate sales channels and adjust pricing",
                Map.of("dropPercent", 15, "forecastAmount", 300000, "actualAmount", 255000)));

        repository.saveAlert(Alert.create("Inventory Below Threshold", "3 SKUs below minimum stock threshold",
                AlertCategory.OPERATIONAL, AlertSeverity.HIGH, AlertPriority.P1, "INVENTORY",
                "Risk of stockout for high-demand products", "Trigger reorder for SKU-101, SKU-203, SKU-405",
                Map.of("affectedSKUs", 3, "threshold", 50, "currentMin", 23)));

        repository.saveAlert(Alert.create("Order Failure Spike", "Order failure rate increased to 5.2%",
                AlertCategory.OPERATIONAL, AlertSeverity.HIGH, AlertPriority.P1, "ORDERS",
                "Customer satisfaction and revenue at risk", "Investigate checkout pipeline and payment gateway",
                Map.of("failureRate", 5.2, "normalRate", 1.2, "affectedOrders", 42)));

        repository.saveAlert(Alert.create("High Cancellation Rate", "Order cancellation rate at 8.3%",
                AlertCategory.BUSINESS, AlertSeverity.MEDIUM, AlertPriority.P2, "ORDERS",
                "Potential $12K lost revenue this month", "Review cancellation reasons and improve fulfillment SLAs",
                Map.of("cancellationRate", 8.3, "targetRate", 3.0, "cancelledOrders", 67)));

        repository.saveAlert(Alert.create("Low Customer Activity", "Customer login rate dropped 22% week-over-week",
                AlertCategory.BUSINESS, AlertSeverity.MEDIUM, AlertPriority.P2, "CUSTOMERS",
                "Declining engagement may indicate churn risk", "Launch re-engagement campaign and survey inactive users",
                Map.of("dropPercent", 22, "activeUsers", 567, "priorWeek", 728)));

        repository.saveAlert(Alert.create("Inactive Vendors", "12 vendors inactive for 30+ days",
                AlertCategory.OPERATIONAL, AlertSeverity.LOW, AlertPriority.P3, "VENDORS",
                "Supply chain diversity at risk", "Reach out to inactive vendors or source alternatives",
                Map.of("inactiveVendors", 12, "totalVendors", 45, "thresholdDays", 30)));

        repository.saveAlert(Alert.create("Inactive Growers", "8 growers inactive for 45+ days",
                AlertCategory.OPERATIONAL, AlertSeverity.LOW, AlertPriority.P3, "GROWERS",
                "Reduced supply capacity from grower network", "Contact growers and offer certification incentives",
                Map.of("inactiveGrowers", 8, "totalGrowers", 128, "thresholdDays", 45)));

        repository.saveAlert(Alert.create("Training Completion Drop", "Training completion rate fell to 72%",
                AlertCategory.BUSINESS, AlertSeverity.MEDIUM, AlertPriority.P2, "TRAINING",
                "Learner satisfaction and certification rates declining", "Review course content and introduce micro-learning",
                Map.of("completionRate", 72.0, "targetRate", 85.0, "activeCourses", 24)));

        repository.saveAlert(Alert.create("Marketplace Slowdown", "Marketplace transaction volume dropped 18%",
                AlertCategory.BUSINESS, AlertSeverity.HIGH, AlertPriority.P1, "MARKETPLACE",
                "Revenue and platform activity declining", "Analyze traffic sources and optimize product discovery",
                Map.of("volumeDrop", 18, "priorVolume", 15234, "currentVolume", 12492)));

        repository.saveAlert(Alert.create("Workflow Failure", "Order fulfillment workflow failure rate at 3.1%",
                AlertCategory.OPERATIONAL, AlertSeverity.HIGH, AlertPriority.P1, "WORKFLOW",
                "Operational efficiency impacted", "Review workflow steps and implement retry logic",
                Map.of("failureRate", 3.1, "targetRate", 0.5, "failedWorkflows", 28)));

        repository.saveAlert(Alert.create("Gateway Failure", "Payment gateway timeout rate at 2.8%",
                AlertCategory.OPERATIONAL, AlertSeverity.CRITICAL, AlertPriority.P0, "GATEWAY",
                "Revenue loss due to failed transactions", "Escalate to payment provider and enable fallback gateway",
                Map.of("timeoutRate", 2.8, "normalRate", 0.3, "failedTransactions", 156)));

        repository.saveAlert(Alert.create("AI Runtime Failure", "AI inference error rate at 4.7%",
                AlertCategory.PERFORMANCE, AlertSeverity.HIGH, AlertPriority.P1, "AI_PLATFORM",
                "AI agent reliability degraded", "Review model endpoints and rollback recent deployments",
                Map.of("errorRate", 4.7, "targetRate", 1.0, "failedInferences", 523)));

        repository.saveAlert(Alert.create("Memory Runtime Alert", "Memory usage at 87% of allocated capacity",
                AlertCategory.PERFORMANCE, AlertSeverity.MEDIUM, AlertPriority.P2, "SYSTEM",
                "Potential OOM risk if trend continues", "Scale up memory allocation and review memory leaks",
                Map.of("usagePercent", 87, "thresholdPercent", 80, "allocatedGB", 8)));

        repository.saveAlert(Alert.create("Knowledge Runtime Warning", "Knowledge base query latency at 320ms",
                AlertCategory.PERFORMANCE, AlertSeverity.LOW, AlertPriority.P3, "AI_PLATFORM",
                "User experience may degrade", "Optimize knowledge base index and cache frequent queries",
                Map.of("latencyMs", 320, "targetLatencyMs", 150, "cacheHitRate", 74.2)));

        repository.saveAlert(Alert.create("Revenue Spike", "Revenue up 42% in last 6 hours (unexpected)",
                AlertCategory.BUSINESS, AlertSeverity.INFO, AlertPriority.P4, "REVENUE",
                "Positive anomaly — verify data integrity", "Validate revenue spike source and adjust forecasts",
                Map.of("spikePercent", 42, "expectedRevenue", 125000, "actualRevenue", 177500)));
    }

    private void seedRisks() {
        repository.saveRisk(BusinessRisk.create("Revenue Risk", "Revenue volatility may impact quarterly targets",
                RiskCategory.REVENUE, RiskSeverity.HIGH, "REVENUE", "Potential $200K shortfall", 65.0, 78.0,
                "Diversify revenue streams and implement dynamic pricing"));

        repository.saveRisk(BusinessRisk.create("Inventory Risk", "Low stock levels for 5 critical products",
                RiskCategory.INVENTORY, RiskSeverity.HIGH, "INVENTORY", "Stockout risk for 3 high-demand SKUs", 70.0, 82.0,
                "Implement automated reorder system and safety stock increase"));

        repository.saveRisk(BusinessRisk.create("Operational Risk", "Workflow failure rate above acceptable threshold",
                RiskCategory.OPERATIONAL, RiskSeverity.MEDIUM, "WORKFLOW", "Operational efficiency at 92%", 45.0, 55.0,
                "Implement retry mechanisms and workflow monitoring"));

        repository.saveRisk(BusinessRisk.create("Marketplace Risk", "Marketplace transaction volume declining",
                RiskCategory.MARKETPLACE, RiskSeverity.HIGH, "MARKETPLACE", "18% volume drop month-over-month", 60.0, 72.0,
                "Enhance product discovery and optimize marketplace UX"));

        repository.saveRisk(BusinessRisk.create("Customer Risk", "Customer engagement declining across key metrics",
                RiskCategory.CUSTOMER, RiskSeverity.MEDIUM, "CUSTOMERS", "22% drop in login frequency", 50.0, 58.0,
                "Launch customer re-engagement program and loyalty incentives"));

        repository.saveRisk(BusinessRisk.create("Vendor Risk", "25% of vendors inactive or underperforming",
                RiskCategory.VENDOR, RiskSeverity.MEDIUM, "VENDORS", "Supply chain concentration risk", 55.0, 60.0,
                "Diversify vendor base and implement scorecard system"));

        repository.saveRisk(BusinessRisk.create("Training Risk", "Training completion rates below target",
                RiskCategory.TRAINING, RiskSeverity.LOW, "TRAINING", "Certification pipeline slowing", 35.0, 40.0,
                "Revise curriculum and introduce micro-learning modules"));

        repository.saveRisk(BusinessRisk.create("Platform Risk", "API response times degrading under load",
                RiskCategory.PLATFORM, RiskSeverity.HIGH, "PLATFORM", "Average response time up 25%", 68.0, 75.0,
                "Implement caching layer and database query optimization"));

        repository.saveRisk(BusinessRisk.create("Automation Risk", "AI agent error rate increasing",
                RiskCategory.AUTOMATION, RiskSeverity.MEDIUM, "AI_PLATFORM", "4.7% inference error rate", 52.0, 62.0,
                "Rollback recent AI model changes and improve monitoring"));

        repository.saveRisk(BusinessRisk.create("AI Risk", "AI platform resource utilization at 87%",
                RiskCategory.AI, RiskSeverity.MEDIUM, "AI_PLATFORM", "Memory capacity nearing limits", 48.0, 56.0,
                "Scale infrastructure and optimize AI model memory footprint"));
    }

    private void seedAnomalies() {
        repository.saveAnomaly(Anomaly.create(AnomalyType.UNEXPECTED_DECLINE, "REVENUE",
                AlertSeverity.CRITICAL, "Revenue dropped 15% below forecast", 300000, 255000, 15.0, 94.0));
        repository.saveAnomaly(Anomaly.create(AnomalyType.UNEXPECTED_GROWTH, "REVENUE",
                AlertSeverity.INFO, "Revenue spiked 42% in 6 hours", 125000, 177500, 42.0, 88.0));
        repository.saveAnomaly(Anomaly.create(AnomalyType.VOLUME_SPIKE, "ORDERS",
                AlertSeverity.HIGH, "Order failure rate spiked to 5.2%", 1.2, 5.2, 4.0, 92.0));
        repository.saveAnomaly(Anomaly.create(AnomalyType.TREND_BREAK, "CUSTOMERS",
                AlertSeverity.MEDIUM, "Customer login trend broken — 22% decline", 728, 567, 22.1, 86.0));
        repository.saveAnomaly(Anomaly.create(AnomalyType.PERFORMANCE_DROP, "AI_PLATFORM",
                AlertSeverity.HIGH, "AI inference error rate at 4.7%", 1.0, 4.7, 3.7, 90.0));
        repository.saveAnomaly(Anomaly.create(AnomalyType.OUTLIER, "INVENTORY",
                AlertSeverity.HIGH, "3 SKUs below minimum threshold — outlier detected", 50, 23, 54.0, 95.0));
        repository.saveAnomaly(Anomaly.create(AnomalyType.OPERATIONAL_FAILURE, "WORKFLOW",
                AlertSeverity.HIGH, "Workflow failure rate at 3.1%", 0.5, 3.1, 2.6, 87.0));
        repository.saveAnomaly(Anomaly.create(AnomalyType.SEASONALITY_CHANGE, "MARKETPLACE",
                AlertSeverity.MEDIUM, "Marketplace volume deviating from seasonal pattern", 15234, 12492, 18.0, 83.0));
    }

    private void seedTimeline() {
        repository.saveTimelineEvent(TimelineEvent.create(TimelineEventType.BUSINESS, "REVENUE", "REVENUE",
                "Revenue Alert Generated", "Revenue drop of 15% detected", AlertSeverity.CRITICAL, "alert-engine",
                Map.of("dropPercent", 15, "domain", "REVENUE")));
        repository.saveTimelineEvent(TimelineEvent.create(TimelineEventType.ALERT, "INVENTORY", "INVENTORY",
                "Inventory Alert", "3 SKUs below threshold", AlertSeverity.HIGH, "alert-engine",
                Map.of("affectedSKUs", 3, "domain", "INVENTORY")));
        repository.saveTimelineEvent(TimelineEvent.create(TimelineEventType.RISK, "REVENUE", "REVENUE",
                "Revenue Risk Assessment", "Revenue risk score updated to 78", AlertSeverity.HIGH, "risk-engine",
                Map.of("riskScore", 78, "severity", "HIGH")));
        repository.saveTimelineEvent(TimelineEvent.create(TimelineEventType.PLATFORM, "SYSTEM", "PLATFORM",
                "Platform Health Check", "Memory usage at 87%", AlertSeverity.MEDIUM, "anomaly-engine",
                Map.of("memoryUsage", 87, "threshold", 80)));
        repository.saveTimelineEvent(TimelineEvent.create(TimelineEventType.AI, "AI_PLATFORM", "AI_PLATFORM",
                "AI Runtime Warning", "AI inference error rate at 4.7%", AlertSeverity.HIGH, "anomaly-engine",
                Map.of("errorRate", 4.7, "modelVersion", "v2.3.1")));
        repository.saveTimelineEvent(TimelineEvent.create(TimelineEventType.WORKFLOW, "WORKFLOW", "WORKFLOW",
                "Workflow Failure", "Order fulfillment workflow failure rate at 3.1%", AlertSeverity.HIGH, "alert-engine",
                Map.of("failureRate", 3.1, "workflowType", "ORDER_FULFILLMENT")));
        repository.saveTimelineEvent(TimelineEvent.create(TimelineEventType.TRAINING, "TRAINING", "TRAINING",
                "Training Alert", "Training completion rate fell to 72%", AlertSeverity.MEDIUM, "alert-engine",
                Map.of("completionRate", 72.0, "targetRate", 85.0)));
        repository.saveTimelineEvent(TimelineEvent.create(TimelineEventType.INVENTORY, "INVENTORY", "INVENTORY",
                "Inventory Anomaly", "Inventory outlier detected for 3 SKUs", AlertSeverity.HIGH, "anomaly-engine",
                Map.of("anomalyType", "OUTLIER", "affectedSKUs", 3)));
        repository.saveTimelineEvent(TimelineEvent.create(TimelineEventType.MARKETPLACE, "MARKETPLACE", "MARKETPLACE",
                "Marketplace Slowdown", "Transaction volume dropped 18%", AlertSeverity.HIGH, "alert-engine",
                Map.of("volumeDrop", 18, "domain", "MARKETPLACE")));
    }
}
