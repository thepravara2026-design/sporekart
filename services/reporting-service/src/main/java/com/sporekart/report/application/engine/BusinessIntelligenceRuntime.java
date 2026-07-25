package com.sporekart.report.application.engine;

import com.sporekart.report.domain.model.*;
import com.sporekart.report.domain.repository.ReportRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class BusinessIntelligenceRuntime {
    private final ReportRepositoryPort repository;

    public BusinessIntelligenceRuntime(ReportRepositoryPort repository) {
        this.repository = repository;
    }

    public List<BusinessIntelligenceReport> generateBiReports() {
        List<BusinessIntelligenceReport> all = new ArrayList<>();
        all.add(generateExecutiveDashboard());
        all.add(generateRevenueIntelligence());
        all.add(generateOperationalIntelligence());
        all.add(generateCustomerIntelligence());
        all.add(generateAiIntelligence());
        all.add(generatePlatformIntelligence());
        all.add(generateRiskIntelligence());
        all.add(generateComplianceIntelligence());
        return all;
    }

    public List<BusinessIntelligenceReport> generateBiReportsForCategory(ReportCategory category) {
        return switch (category) {
            case EXECUTIVE, BUSINESS_HEALTH -> List.of(generateExecutiveDashboard());
            case REVENUE -> List.of(generateRevenueIntelligence());
            case ORDERS, INVENTORY -> List.of(generateOperationalIntelligence());
            case CUSTOMERS -> List.of(generateCustomerIntelligence());
            case AI_PLATFORM -> List.of(generateAiIntelligence());
            case PLATFORM_HEALTH -> List.of(generatePlatformIntelligence());
            case RISK -> List.of(generateRiskIntelligence());
            case COMPLIANCE -> List.of(generateComplianceIntelligence());
            default -> List.of(generateExecutiveDashboard());
        };
    }

    public BusinessIntelligenceReport getAggregatedReport(String reportId) {
        return repository.findBiReportById(reportId)
            .orElseThrow(() -> new IllegalArgumentException("BI Report not found: " + reportId));
    }

    public List<BusinessIntelligenceReport> getAllBiReports() {
        return repository.findAllBiReports();
    }

    public List<BusinessIntelligenceReport> getBiReportsByCategory(ReportCategory category) {
        return repository.findBiReportsByCategory(category);
    }

    private BusinessIntelligenceReport generateExecutiveDashboard() {
        BusinessIntelligenceReport report = BusinessIntelligenceReport.create(
            "Executive Business Intelligence Dashboard",
            "Aggregated business intelligence across all domains",
            ReportType.EXECUTIVE, ReportCategory.EXECUTIVE,
            "Enterprise performance is HEALTHY with strong revenue growth and stable operations. "
                + "Monitor revenue concentration risk and supply chain vulnerabilities.",
            "HEALTHY",
            List.of("Maintain current growth trajectory", "Address AI platform cost efficiency"),
            List.of("Revenue concentration in top 3 categories", "Supply chain single-vendor dependency"),
            Map.of("totalRevenue", 2840000.0, "revenueGrowth", 12.0, "customerGrowth", 8.0,
                   "npsScore", 72, "uptime", 99.2, "fulfillmentRate", 94.5),
            Map.of("activeCustomers", 45200, "activeOrders", 1542, "activeModels", 12),
            List.of(Map.of("source", "Analytics Engine", "type", "revenue", "confidence", 0.95),
                    Map.of("source", "Alert Engine", "type", "alerts", "criticalCount", 0, "highCount", 2),
                    Map.of("source", "Decision Engine", "type", "recommendations", "count", 5))
        );
        return repository.saveBiReport(report);
    }

    private BusinessIntelligenceReport generateRevenueIntelligence() {
        BusinessIntelligenceReport report = BusinessIntelligenceReport.create(
            "Revenue Intelligence Report",
            "Deep-dive revenue analytics with predictive insights",
            ReportType.MONTHLY, ReportCategory.REVENUE,
            "Revenue trends positive with 12% YoY growth. Online channel dominates at 68%. "
                + "Forecast indicates 15% growth next quarter driven by holiday season.",
            "HEALTHY",
            List.of("Expand wholesale channel", "Optimize pricing for elastic categories"),
            List.of("Revenue concentration risk in Electronics", "Seasonal dip expected in January"),
            Map.of("currentRevenue", 2840000.0, "projectedRevenue", 3200000.0, "growthRate", 15.0,
                   "onlineShare", 68.0, "retailShare", 22.0, "wholesaleShare", 10.0),
            Map.of("avgOrderValue", 84.26, "customerLtv", 420.0, "refundRate", 2.3),
            List.of(Map.of("source", "Analytics Engine", "type", "revenue_trend", "period", "monthly"),
                    Map.of("source", "Prediction Engine", "type", "forecast", "confidence", 0.88))
        );
        return repository.saveBiReport(report);
    }

    private BusinessIntelligenceReport generateOperationalIntelligence() {
        BusinessIntelligenceReport report = BusinessIntelligenceReport.create(
            "Operational Intelligence Report",
            "Operations and fulfillment intelligence",
            ReportType.WEEKLY, ReportCategory.ORDERS,
            "Operations running efficiently with 94.2% on-time delivery. "
                + "Fulfillment time averaging 1.8 days. Inventory health at 87%.",
            "HEALTHY",
            List.of("Increase warehouse capacity for holiday peak", "Implement predictive reordering"),
            List.of("3 SKUs at stockout risk", "Carrier capacity constraints"),
            Map.of("onTimeDelivery", 94.2, "fulfillmentDays", 1.8, "inventoryHealth", 87.0,
                   "ordersProcessed", 5420, "returnRate", 3.1),
            Map.of("warehouseCapacity", 72.0, "carrierReliability", 96.0, "processingCostPerOrder", 4.50),
            List.of(Map.of("source", "Analytics Engine", "type", "operational_metrics"),
                    Map.of("source", "Alert Engine", "type", "inventory_alerts"))
        );
        return repository.saveBiReport(report);
    }

    private BusinessIntelligenceReport generateCustomerIntelligence() {
        BusinessIntelligenceReport report = BusinessIntelligenceReport.create(
            "Customer Intelligence Report",
            "Customer behavior, segmentation, and lifetime value analysis",
            ReportType.MONTHLY, ReportCategory.CUSTOMERS,
            "Customer base growing at 8% monthly. Repeat purchase rate at 42%. "
                + "NPS score of 72 indicates good customer satisfaction.",
            "HEALTHY",
            List.of("Launch loyalty program for high-value segment", "Reduce churn in 25-35 age group"),
            List.of("Customer concentration in top 10%", "Acquisition cost rising 5% QoQ"),
            Map.of("activeCustomers", 45200, "repeatRate", 42.0, "npsScore", 72,
                   "customerGrowth", 8.0, "acquisitionCost", 32.50),
            Map.of("avgLifetimeValue", 420.0, "satisfactionScore", 4.2, "churnRate", 2.1),
            List.of(Map.of("source", "Analytics Engine", "type", "customer_segments"),
                    Map.of("source", "Decision Engine", "type", "retention_strategies"))
        );
        return repository.saveBiReport(report);
    }

    private BusinessIntelligenceReport generateAiIntelligence() {
        BusinessIntelligenceReport report = BusinessIntelligenceReport.create(
            "AI Platform Intelligence Report",
            "AI model performance, cost efficiency, and optimization insights",
            ReportType.WEEKLY, ReportCategory.AI_PLATFORM,
            "AI platform HEALTHY with 98.5% uptime. Model costs increasing 8% weekly. "
                + "Two models showing drift requiring attention.",
            "WARNING",
            List.of("Optimize high-cost models for efficiency", "Retrain drifting models"),
            List.of("Cost growth outpacing usage growth", "Model drift reducing prediction accuracy"),
            Map.of("activeModels", 12, "uptime", 98.5, "avgLatencyMs", 245, "weeklyCost", 4200.0),
            Map.of("totalInferences", 1850000, "costPerInference", 0.0023, "modelsInDrift", 2),
            List.of(Map.of("source", "Analytics Engine", "type", "model_performance"),
                    Map.of("source", "Alert Engine", "type", "ai_alerts"))
        );
        return repository.saveBiReport(report);
    }

    private BusinessIntelligenceReport generatePlatformIntelligence() {
        BusinessIntelligenceReport report = BusinessIntelligenceReport.create(
            "Platform Intelligence Report",
            "Infrastructure health, capacity, and reliability intelligence",
            ReportType.DAILY, ReportCategory.PLATFORM_HEALTH,
            "Platform healthy with 99.2% uptime. No critical incidents. "
                + "Resource utilization within acceptable ranges.",
            "HEALTHY",
            List.of("Plan capacity upgrade for Q4 traffic", "Improve database redundancy"),
            List.of("Database connection pool near limit", "Single availability zone dependency"),
            Map.of("uptime", 99.2, "incidents", 2, "avgResponseMin", 2.3,
                   "apiLatencyMs", 45, "errorRate", 0.12),
            Map.of("cpuUtilization", 62.0, "memoryUtilization", 71.0, "diskUtilization", 55.0),
            List.of(Map.of("source", "Analytics Engine", "type", "infrastructure_metrics"),
                    Map.of("source", "Alert Engine", "type", "platform_alerts"))
        );
        return repository.saveBiReport(report);
    }

    private BusinessIntelligenceReport generateRiskIntelligence() {
        BusinessIntelligenceReport report = BusinessIntelligenceReport.create(
            "Risk Intelligence Report",
            "Aggregated risk assessment with predictive risk scoring",
            ReportType.MONTHLY, ReportCategory.RISK,
            "Enterprise risk score: 72 (WARNING). 14 active risks. "
                + "Three critical risks require immediate attention.",
            "WARNING",
            List.of("Accelerate critical risk mitigation", "Implement risk monitoring dashboard"),
            List.of("Revenue concentration (score: 85)", "Supply chain disruption (score: 78)"),
            Map.of("riskScore", 72.0, "activeRisks", 14, "criticalRisks", 3,
                   "highRisks", 5, "mitigationPlans", 8),
            Map.of("trendingUp", true, "topRiskCategory", "Revenue Concentration"),
            List.of(Map.of("source", "Alert Engine", "type", "risk_assessments"),
                    Map.of("source", "Decision Engine", "type", "mitigation_strategies"))
        );
        return repository.saveBiReport(report);
    }

    private BusinessIntelligenceReport generateComplianceIntelligence() {
        BusinessIntelligenceReport report = BusinessIntelligenceReport.create(
            "Compliance Intelligence Report",
            "Regulatory compliance status and audit readiness",
            ReportType.MONTHLY, ReportCategory.COMPLIANCE,
            "All compliance requirements met. Zero incidents. Next SOC 2 audit scheduled.",
            "HEALTHY",
            List.of("Prepare SOC 2 evidence package", "Update privacy policy for new regulations"),
            List.of("Evolving GDPR requirements", "Cross-border data transfer complexity"),
            Map.of("complianceStatus", "PASS", "incidents", 0, "auditsPassed", 3, "openFindings", 2),
            Map.of("lastAudit", "2026-06-15", "nextAudit", "2026-09-15", "coverageScore", 95.0),
            List.of(Map.of("source", "Alert Engine", "type", "compliance_alerts"))
        );
        return repository.saveBiReport(report);
    }
}
