package com.sporekart.alert.application.engine;

import com.sporekart.alert.config.AlertConfig;
import com.sporekart.alert.domain.model.*;
import com.sporekart.alert.domain.repository.AlertRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AlertEngine {

    private final AlertConfig config;
    private final AlertRepositoryPort repository;

    public AlertEngine(AlertConfig config, AlertRepositoryPort repository) {
        this.config = config;
        this.repository = repository;
    }

    public List<Alert> generateAllAlerts() {
        List<Alert> alerts = new ArrayList<>();
        alerts.addAll(generateBusinessAlerts());
        alerts.addAll(generateOperationalAlerts());
        alerts.addAll(generatePerformanceAlerts());
        alerts.addAll(generateSecurityAlerts());
        alerts.addAll(generateMaintenanceAlerts());
        return alerts;
    }

    public List<Alert> generateAlertsForCategory(AlertCategory category) {
        return switch (category) {
            case BUSINESS -> generateBusinessAlerts();
            case OPERATIONAL -> generateOperationalAlerts();
            case PERFORMANCE -> generatePerformanceAlerts();
            case SECURITY -> generateSecurityAlerts();
            case MAINTENANCE -> generateMaintenanceAlerts();
            default -> List.of();
        };
    }

    private List<Alert> generateBusinessAlerts() {
        return List.of(
                alert("Revenue Anomaly", "Unusual revenue pattern detected across 3 channels",
                        AlertCategory.BUSINESS, AlertSeverity.HIGH, AlertPriority.P1, "REVENUE",
                        "Revenue variation of 12% from expected pattern may indicate market shift",
                        "Analyze channel performance and adjust pricing strategy",
                        Map.of("variation", 12.0, "channels", 3, "confidence", 87.0)),
                alert("Customer Churn Risk", "Customer retention rate dropped 4% this quarter",
                        AlertCategory.BUSINESS, AlertSeverity.MEDIUM, AlertPriority.P2, "CUSTOMERS",
                        "Approximately 120 customers at risk of churn", "Launch retention campaign with personalized offers",
                        Map.of("retentionDrop", 4.0, "atRiskCustomers", 120, "quarter", "Q3")));
    }

    private List<Alert> generateOperationalAlerts() {
        return List.of(
                alert("Order Processing Delay", "Average order processing time exceeded SLA by 35%",
                        AlertCategory.OPERATIONAL, AlertSeverity.HIGH, AlertPriority.P1, "ORDERS",
                        "Customer delivery SLAs at risk", "Investigate bottlenecks in order processing pipeline",
                        Map.of("slaOvershoot", 35.0, "avgProcessingTime", 185, "targetTime", 120)),
                alert("Warehouse Capacity Warning", "Warehouse utilization at 92% capacity",
                        AlertCategory.OPERATIONAL, AlertSeverity.MEDIUM, AlertPriority.P2, "INVENTORY",
                        "Incoming shipment may not have storage space", "Review warehouse expansion plan and optimize layout",
                        Map.of("utilization", 92.0, "capacitySqFt", 50000, "usedSqFt", 46000)));
    }

    private List<Alert> generatePerformanceAlerts() {
        return List.of(
                alert("API Latency Increase", "P95 API response time increased to 850ms",
                        AlertCategory.PERFORMANCE, AlertSeverity.MEDIUM, AlertPriority.P2, "PLATFORM",
                        "User experience degradation across platform", "Optimize database queries and review caching strategy",
                        Map.of("p95Latency", 850, "baseline", 350, "degradation", 143.0)),
                alert("Database Connection Pool Exhaustion", "Connection pool at 85% utilization",
                        AlertCategory.PERFORMANCE, AlertSeverity.HIGH, AlertPriority.P1, "PLATFORM",
                        "Risk of connection timeouts during peak load", "Increase max pool size and review connection leaks",
                        Map.of("poolUtilization", 85.0, "activeConnections", 68, "maxConnections", 80)));
    }

    private List<Alert> generateSecurityAlerts() {
        return List.of(
                alert("Failed Login Attempts Spike", "Failed login attempts up 300% in last hour",
                        AlertCategory.SECURITY, AlertSeverity.CRITICAL, AlertPriority.P0, "SECURITY",
                        "Possible brute force attack in progress", "Enable rate limiting and review access logs",
                        Map.of("failedAttempts", 1256, "normalRate", 314, "increasePercent", 300)),
                alert("Suspicious API Access Pattern", "Unusual API access pattern detected from unknown IP range",
                        AlertCategory.SECURITY, AlertSeverity.HIGH, AlertPriority.P1, "PLATFORM",
                        "Potential unauthorized access attempt", "Block IP range and review API access logs",
                        Map.of("unknownIPs", 5, "suspiciousEndpoints", 3, "riskLevel", "HIGH")));
    }

    private List<Alert> generateMaintenanceAlerts() {
        return List.of(
                alert("SSL Certificate Expiring", "SSL certificate for api.sporekart.com expires in 7 days",
                        AlertCategory.MAINTENANCE, AlertSeverity.HIGH, AlertPriority.P1, "PLATFORM",
                        "Service will be unavailable after expiry", "Renew SSL certificate before expiry date",
                        Map.of("daysToExpiry", 7, "domain", "api.sporekart.com", "renewalContact", "devops@sporekart.com")),
                alert("Database Backup Failed", "Scheduled database backup failed for warehouse cluster",
                        AlertCategory.MAINTENANCE, AlertSeverity.CRITICAL, AlertPriority.P0, "PLATFORM",
                        "Data recovery capability compromised", "Investigate backup failure and trigger manual backup",
                        Map.of("cluster", "warehouse-db", "schedule", "daily-0200", "retryCount", 3)));
    }

    private Alert alert(String title, String desc, AlertCategory cat, AlertSeverity sev,
                         AlertPriority pri, String domain, String impact, String resolution,
                         Map<String, Object> metrics) {
        Alert a = Alert.create(title, desc, cat, sev, pri, domain, impact, resolution, metrics);
        return repository.saveAlert(a);
    }
}
