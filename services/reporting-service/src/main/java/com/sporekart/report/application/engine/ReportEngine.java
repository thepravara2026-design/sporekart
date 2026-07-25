package com.sporekart.report.application.engine;

import com.sporekart.report.domain.model.*;
import com.sporekart.report.domain.repository.ReportRepositoryPort;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.*;

@Component
public class ReportEngine {
    private final ReportRepositoryPort repository;

    public ReportEngine(ReportRepositoryPort repository) {
        this.repository = repository;
    }

    public List<Report> generateAllReports() {
        List<Report> all = new ArrayList<>();
        all.addAll(generateExecutiveReports());
        all.addAll(generateRevenueReports());
        all.addAll(generateSalesReports());
        all.addAll(generateOrderReports());
        all.addAll(generateInventoryReports());
        all.addAll(generateCustomerReports());
        all.addAll(generateProductReports());
        all.addAll(generateMarketplaceReports());
        all.addAll(generateTrainingReports());
        all.addAll(generateVendorReports());
        all.addAll(generateGrowerReports());
        all.addAll(generateAiPlatformReports());
        all.addAll(generateAutomationReports());
        all.addAll(generatePlatformHealthReports());
        all.addAll(generateRiskReports());
        all.addAll(generateBusinessHealthReports());
        all.addAll(generateComplianceReports());
        return all;
    }

    public List<Report> generateReportsForCategory(ReportCategory category) {
        return switch (category) {
            case EXECUTIVE -> generateExecutiveReports();
            case REVENUE -> generateRevenueReports();
            case SALES -> generateSalesReports();
            case ORDERS -> generateOrderReports();
            case INVENTORY -> generateInventoryReports();
            case CUSTOMERS -> generateCustomerReports();
            case PRODUCTS -> generateProductReports();
            case MARKETPLACE -> generateMarketplaceReports();
            case TRAINING -> generateTrainingReports();
            case VENDORS -> generateVendorReports();
            case GROWERS -> generateGrowerReports();
            case AI_PLATFORM -> generateAiPlatformReports();
            case AUTOMATION -> generateAutomationReports();
            case PLATFORM_HEALTH -> generatePlatformHealthReports();
            case RISK -> generateRiskReports();
            case BUSINESS_HEALTH -> generateBusinessHealthReports();
            case COMPLIANCE -> generateComplianceReports();
        };
    }

    public Report generateReport(String title, String description, ReportType type, ReportCategory category,
                                 String owner, String summary, String businessHealth,
                                 List<String> recommendations, List<String> risks,
                                 Map<String, Object> kpis, Map<String, Object> supportingMetrics,
                                 String templateId) {
        Report report = Report.create(title, description, type, category, owner,
            summary, businessHealth, recommendations, risks, kpis, supportingMetrics, templateId);
        report = report.withGeneratedAt(Instant.now()).withExecutionTime(new Random().nextLong(50, 500));
        return repository.saveReport(report);
    }

    private List<Report> generateExecutiveReports() {
        return List.of(
            report("CEO Daily Report", "Executive summary of daily business operations",
                ReportType.DAILY, ReportCategory.EXECUTIVE, "CEO",
                "Strong revenue performance with 12% growth. Inventory levels stable. No critical alerts.",
                "HEALTHY",
                List.of("Monitor inventory turnover", "Review marketing spend efficiency"),
                List.of("Revenue concentration risk", "Supply chain disruption potential"),
                Map.of("revenueGrowth", 12.0, "activeOrders", 1542, "fulfillmentRate", 94.5),
                Map.of("dailyActiveUsers", 45200, "newCustomers", 328, "avgOrderValue", 89.50),
                "executive-summary-template"),
            report("Monthly Business Report", "Comprehensive monthly business performance analysis",
                ReportType.MONTHLY, ReportCategory.EXECUTIVE, "CFO",
                "Monthly revenue exceeded targets by 8%. Customer acquisition costs decreased 15%.",
                "HEALTHY",
                List.of("Expand high-margin product categories", "Optimize fulfillment network"),
                List.of("Market volatility", "Rising vendor costs"),
                Map.of("monthlyRevenue", 2840000.0, "monthlyGrowth", 8.2, "grossMargin", 42.3),
                Map.of("customerAcquisitionCost", 32.50, "lifetimeValue", 420.0, "churnRate", 2.1),
                "monthly-executive-template")
        );
    }

    private List<Report> generateRevenueReports() {
        return List.of(
            report("Weekly Revenue Summary", "Weekly revenue breakdown by channel and category",
                ReportType.WEEKLY, ReportCategory.REVENUE, "Finance",
                "Total revenue $712,000. Online channels contributed 68%, retail 22%, wholesale 10%.",
                "HEALTHY",
                List.of("Boost wholesale channel", "Optimize pricing for top 10 SKUs"),
                List.of("Revenue concentration in top 3 categories", "Seasonal decline expected"),
                Map.of("totalRevenue", 712000.0, "onlineRevenue", 484160.0, "retailRevenue", 156640.0, "wholesaleRevenue", 71200.0),
                Map.of("transactions", 8450, "avgOrderValue", 84.26, "refundRate", 2.3),
                "revenue-summary-template"),
            report("Quarterly Revenue Forecast", "Revenue projections for next quarter",
                ReportType.QUARTERLY, ReportCategory.REVENUE, "Finance",
                "Projected Q4 revenue $3.2M driven by holiday season. Conservative estimate $2.8M.",
                "WARNING",
                List.of("Increase inventory for holiday season", "Launch early-bird promotions"),
                List.of("Economic downturn risk", "Competitor pricing pressure"),
                Map.of("projectedRevenue", 3200000.0, "conservativeEstimate", 2800000.0, "growthRate", 15.0),
                Map.of("lastYearQ4", 2450000.0, "marketGrowth", 8.5),
                "revenue-forecast-template")
        );
    }

    private List<Report> generateSalesReports() {
        return List.of(
            report("Daily Sales Report", "Real-time sales performance tracking",
                ReportType.DAILY, ReportCategory.SALES, "Sales",
                "Today's sales $48,200 with 580 orders. Top category: Electronics (32%).",
                "HEALTHY",
                List.of("Upsell accessories with electronics", "Target repeat customers"),
                List.of("Below-average conversion rate", "Cart abandonment above 65%"),
                Map.of("dailySales", 48200.0, "orderCount", 580, "conversionRate", 3.2, "cartAbandonmentRate", 67.0),
                Map.of("topSku", "SKU-ELECT-001", "topCategory", "Electronics", "avgItemsPerOrder", 2.4),
                "daily-sales-template")
        );
    }

    private List<Report> generateOrderReports() {
        return List.of(
            report("Order Operations Report", "Order processing and fulfillment metrics",
                ReportType.WEEKLY, ReportCategory.ORDERS, "Operations",
                "5,420 orders processed. Average fulfillment time 1.8 days. 94.2% on-time delivery.",
                "HEALTHY",
                List.of("Optimize pick-pack process", "Add evening dispatch shift"),
                List.of("Peak season capacity constraint", "Carrier reliability issues"),
                Map.of("ordersProcessed", 5420, "avgFulfillmentHours", 43.2, "onTimeDelivery", 94.2),
                Map.of("returnRate", 3.1, "damageRate", 0.8, "processingCost", 4.50),
                "order-operations-template")
        );
    }

    private List<Report> generateInventoryReports() {
        return List.of(
            report("Inventory Health Report", "Current inventory status across all warehouses",
                ReportType.WEEKLY, ReportCategory.INVENTORY, "Inventory",
                "Overall inventory health: 87%. 3 SKUs below reorder point. 2 overstocked items.",
                "HEALTHY",
                List.of("Reorder SKU-INV-001, SKU-INV-004", "Run promotion for overstock items"),
                List.of("Stockout risk for 3 SKUs", "Overstock carrying cost increasing"),
                Map.of("inventoryHealth", 87.0, "totalSkuCount", 1250, "stockoutRisk", 3, "overstockCount", 2),
                Map.of("inventoryValue", 4200000.0, "turnoverRate", 4.2, "carryingCost", 125000.0),
                "inventory-health-template")
        );
    }

    private List<Report> generateCustomerReports() {
        return List.of(
            report("Customer Insights Report", "Customer behavior and segmentation analysis",
                ReportType.MONTHLY, ReportCategory.CUSTOMERS, "Marketing",
                "Active customer base grew 8% to 45,200. Repeat purchase rate 42%. NPS score 72.",
                "HEALTHY",
                List.of("Launch loyalty program for high-value segment", "Re-engage dormant customers"),
                List.of("Customer concentration risk", "Declining NPS in 25-35 age group"),
                Map.of("activeCustomers", 45200, "repeatRate", 42.0, "npsScore", 72, "customerGrowth", 8.0),
                Map.of("avgLifetimeValue", 420.0, "acquisitionCost", 32.50, "satisfactionScore", 4.2),
                "customer-insights-template")
        );
    }

    private List<Report> generateProductReports() {
        return List.of(
            report("Product Performance Report", "Product catalog performance analytics",
                ReportType.MONTHLY, ReportCategory.PRODUCTS, "Product",
                "Top 10 products contribute 34% of revenue. 12 new products launched this month.",
                "HEALTHY",
                List.of("Bundle slow-moving products with top sellers", "Expand top category assortment"),
                List.of("Product concentration risk", "New product adoption below target"),
                Map.of("top10RevenueShare", 34.0, "newProducts", 12, "totalActiveSku", 1250, "avgMargin", 42.3),
                Map.of("bestSeller", "SKU-ELECT-001", "worstSeller", "SKU-HOME-042", "returnRateByProduct", Map.of()),
                "product-performance-template")
        );
    }

    private List<Report> generateMarketplaceReports() {
        return List.of(
            report("Marketplace Activity Report", "Marketplace seller and transaction metrics",
                ReportType.WEEKLY, ReportCategory.MARKETPLACE, "Marketplace",
                "245 active sellers. 1,850 marketplace transactions. Average seller rating 4.3/5.",
                "HEALTHY",
                List.of("Onboard top 20 potential sellers", "Improve seller onboarding flow"),
                List.of("Seller churn increasing", "Quality control issues with 5% of sellers"),
                Map.of("activeSellers", 245, "marketplaceTransactions", 1850, "avgSellerRating", 4.3),
                Map.of("newSellers", 12, "sellerChurn", 3.2, "marketplaceRevenue", 285000.0),
                "marketplace-activity-template")
        );
    }

    private List<Report> generateTrainingReports() {
        return List.of(
            report("Training Progress Report", "Training program completion and effectiveness",
                ReportType.MONTHLY, ReportCategory.TRAINING, "Training",
                "85% completion rate across all programs. 450 active trainees. 92% satisfaction.",
                "HEALTHY",
                List.of("Create advanced modules for top performers", "Reduce course duration"),
                List.of("Low enrollment in advanced courses", "Trainer capacity constraint"),
                Map.of("completionRate", 85.0, "activeTrainees", 450, "satisfactionRate", 92.0),
                Map.of("coursesCompleted", 1280, "avgCompletionDays", 14.5, "certificationRate", 78.0),
                "training-progress-template")
        );
    }

    private List<Report> generateVendorReports() {
        return List.of(
            report("Vendor Performance Report", "Vendor reliability and performance metrics",
                ReportType.MONTHLY, ReportCategory.VENDORS, "Procurement",
                "85 active vendors. 94% on-time delivery. Average lead time 4.2 days.",
                "HEALTHY",
                List.of("Negotiate bulk discounts with top 5 vendors", "Diversify single-source vendors"),
                List.of("Single vendor dependency for 3 critical SKUs", "Rising material costs"),
                Map.of("activeVendors", 85, "onTimeDelivery", 94.0, "avgLeadTimeDays", 4.2),
                Map.of("qualityScore", 4.5, "defectRate", 0.8, "costVariance", 3.2),
                "vendor-performance-template")
        );
    }

    private List<Report> generateGrowerReports() {
        return List.of(
            report("Grower Performance Report", "Agricultural grower metrics and yield analysis",
                ReportType.MONTHLY, ReportCategory.GROWERS, "Agriculture",
                "45 active growers. 92% yield target achieved. Average quality grade A.",
                "HEALTHY",
                List.of("Introduce sustainable farming incentives", "Expand grower network in new regions"),
                List.of("Weather dependency risk", "Quality variance across regions"),
                Map.of("activeGrowers", 45, "yieldTargetAchieved", 92.0, "avgQualityGrade", "A"),
                Map.of("totalHarvest", 284000.0, "avgYieldPerAcre", 3.2, "sustainabilityScore", 85.0),
                "grower-performance-template")
        );
    }

    private List<Report> generateAiPlatformReports() {
        return List.of(
            report("AI Platform Utilization Report", "AI model usage, performance, and cost metrics",
                ReportType.WEEKLY, ReportCategory.AI_PLATFORM, "AI",
                "12 active models. 98.5% uptime. Average inference latency 245ms. Cost $4,200 this week.",
                "HEALTHY",
                List.of("Optimize high-cost models", "Evaluate model consolidation opportunities"),
                List.of("Cost growth outpacing usage growth", "Model drift detected in 2 models"),
                Map.of("activeModels", 12, "uptime", 98.5, "avgLatencyMs", 245, "weeklyCost", 4200.0),
                Map.of("totalInferences", 1850000, "costPerInference", 0.0023, "modelsInDrift", 2),
                "ai-platform-utilization-template")
        );
    }

    private List<Report> generateAutomationReports() {
        return List.of(
            report("Automation Operations Report", "Workflow automation performance and coverage",
                ReportType.WEEKLY, ReportCategory.AUTOMATION, "Automation",
                "45 active workflows. 92% success rate. 3,400 automated tasks executed this week.",
                "HEALTHY",
                List.of("Identify 5 new workflows for automation", "Reduce failure rate below 5%"),
                List.of("Legacy system integration gaps", "Manual override dependency increasing"),
                Map.of("activeWorkflows", 45, "successRate", 92.0, "automatedTasks", 3400),
                Map.of("timeSavedHours", 680, "errorRate", 3.2, "manualOverrides", 45),
                "automation-operations-template")
        );
    }

    private List<Report> generatePlatformHealthReports() {
        return List.of(
            report("Platform Health Report", "Overall platform health and infrastructure status",
                ReportType.DAILY, ReportCategory.PLATFORM_HEALTH, "Engineering",
                "99.2% uptime. 2 incidents today (both resolved). Avg response time 2.3 minutes.",
                "HEALTHY",
                List.of("Increase redundancy for database tier", "Implement auto-scaling for peak"),
                List.of("Single availability zone dependency", "Database connection pool near limit"),
                Map.of("uptime", 99.2, "incidents", 2, "avgResponseMin", 2.3),
                Map.of("apiLatency", 45.0, "errorRate", 0.12, "cpuUtilization", 62.0, "memoryUtilization", 71.0),
                "platform-health-template")
        );
    }

    private List<Report> generateRiskReports() {
        return List.of(
            report("Risk Summary Report", "Enterprise risk assessment and mitigation status",
                ReportType.MONTHLY, ReportCategory.RISK, "Risk",
                "14 active risks identified. 3 critical, 5 high, 4 medium, 2 low. 8 mitigation plans active.",
                "WARNING",
                List.of("Accelerate mitigation for critical risks", "Monthly risk review with stakeholders"),
                List.of("Critical revenue concentration risk", "Supply chain disruption high risk"),
                Map.of("activeRisks", 14, "critical", 3, "high", 5, "medium", 4, "low", 2, "mitigationPlans", 8),
                Map.of("riskScore", 72.0, "trendingUp", true, "topRisk", "Revenue Concentration"),
                "risk-summary-template")
        );
    }

    private List<Report> generateBusinessHealthReports() {
        return List.of(
            report("Business Health Report", "Comprehensive business health assessment",
                ReportType.MONTHLY, ReportCategory.BUSINESS_HEALTH, "Executive",
                "Overall business health: HEALTHY. Revenue growth 12%, customer growth 8%, NPS 72.",
                "HEALTHY",
                List.of("Maintain current growth trajectory", "Address customer churn in 25-35 segment"),
                List.of("Market competition intensifying", "Talent retention in engineering"),
                Map.of("businessHealth", "HEALTHY", "revenueGrowth", 12.0, "customerGrowth", 8.0, "npsScore", 72),
                Map.of("employeeCount", 340, "satisfactionScore", 4.1, "marketShare", 18.5),
                "business-health-template")
        );
    }

    private List<Report> generateComplianceReports() {
        return List.of(
            report("Compliance Status Report", "Regulatory compliance and audit status",
                ReportType.MONTHLY, ReportCategory.COMPLIANCE, "Compliance",
                "All regulatory requirements met. 0 compliance incidents. 3 audits passed this quarter.",
                "HEALTHY",
                List.of("Prepare for upcoming SOC 2 audit", "Update data retention policies"),
                List.of("GDPR requirements evolving", "Cross-border data transfer regulations"),
                Map.of("complianceStatus", "PASS", "incidents", 0, "auditsPassed", 3),
                Map.of("lastAuditDate", "2026-06-15", "nextAuditDate", "2026-09-15", "openFindings", 2),
                "compliance-status-template")
        );
    }

    private Report report(String title, String description, ReportType type, ReportCategory category,
                          String owner, String summary, String businessHealth,
                          List<String> recommendations, List<String> risks,
                          Map<String, Object> kpis, Map<String, Object> metrics, String templateId) {
        Report report = Report.create(title, description, type, category, owner,
            summary, businessHealth, recommendations, risks, kpis, metrics, templateId);
        report = report.withGeneratedAt(Instant.now()).withExecutionTime(new Random().nextLong(50, 500));
        return repository.saveReport(report);
    }
}
