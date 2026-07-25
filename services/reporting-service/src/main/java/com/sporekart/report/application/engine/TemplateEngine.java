package com.sporekart.report.application.engine;

import com.sporekart.report.domain.model.*;
import com.sporekart.report.domain.repository.ReportRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class TemplateEngine {
    private final ReportRepositoryPort repository;

    public TemplateEngine(ReportRepositoryPort repository) {
        this.repository = repository;
    }

    public List<ReportTemplate> generateAllTemplates() {
        List<ReportTemplate> all = new ArrayList<>();
        all.addAll(generateExecutiveTemplates());
        all.addAll(generateBusinessTemplates());
        all.addAll(generateRevenueTemplates());
        all.addAll(generateOperationalTemplates());
        all.addAll(generateAiTemplates());
        all.addAll(generateHealthTemplates());
        all.addAll(generateRiskTemplates());
        all.addAll(generateComplianceTemplates());
        return all;
    }

    public ReportTemplate createTemplate(String name, String description, ReportCategory category,
                                          ReportType type, String owner, List<String> sections,
                                          Map<String, Object> defaultConfig) {
        ReportTemplate template = ReportTemplate.create(name, description, category, type,
            owner, sections, defaultConfig);
        return repository.saveTemplate(template);
    }

    public ReportTemplate getTemplate(String templateId) {
        return repository.findTemplateById(templateId)
            .orElseThrow(() -> new IllegalArgumentException("Template not found: " + templateId));
    }

    public List<ReportTemplate> getAllTemplates() {
        return repository.findAllTemplates();
    }

    public List<ReportTemplate> getActiveTemplates() {
        return repository.findActiveTemplates();
    }

    public ReportTemplate updateTemplate(String templateId, String name, String description,
                                          List<String> sections, Map<String, Object> defaultConfig) {
        ReportTemplate existing = getTemplate(templateId);
        ReportTemplate updated = ReportTemplate.create(
            name != null ? name : existing.name(),
            description != null ? description : existing.description(),
            existing.category(), existing.type(), existing.owner(),
            sections != null ? sections : existing.sections(),
            defaultConfig != null ? defaultConfig : existing.defaultConfig()
        );
        return repository.saveTemplate(updated);
    }

    public ReportTemplate deactivateTemplate(String templateId) {
        ReportTemplate existing = getTemplate(templateId);
        return repository.saveTemplate(existing.withActive(false));
    }

    public ReportTemplate activateTemplate(String templateId) {
        ReportTemplate existing = getTemplate(templateId);
        return repository.saveTemplate(existing.withActive(true));
    }

    private List<ReportTemplate> generateExecutiveTemplates() {
        return List.of(
            template("Executive Summary Template", "Standard executive report layout",
                ReportCategory.EXECUTIVE, ReportType.EXECUTIVE, "Executive",
                List.of("Executive Overview", "Key Metrics", "Business Health", "Recommendations", "Risk Flags"),
                Map.of("includeCharts", true, "maxKpis", 10, "theme", "professional")),
            template("CEO Daily Brief Template", "Daily briefing format for CEO",
                ReportCategory.EXECUTIVE, ReportType.DAILY, "CEO",
                List.of("Daily Summary", "Revenue Flash", "Critical Alerts", "Pipeline Status"),
                Map.of("includeCharts", true, "maxKpis", 5, "theme", "concise"))
        );
    }

    private List<ReportTemplate> generateBusinessTemplates() {
        return List.of(
            template("Business Overview Template", "Comprehensive business overview layout",
                ReportCategory.BUSINESS_HEALTH, ReportType.MONTHLY, "Executive",
                List.of("Executive Summary", "Financial Performance", "Operational Metrics", "Customer Insights", "Market Position"),
                Map.of("includeCharts", true, "includeComparison", true, "maxKpis", 15)),
            template("Business Health Template", "Business health assessment format",
                ReportCategory.BUSINESS_HEALTH, ReportType.MONTHLY, "Executive",
                List.of("Health Score", "Trend Analysis", "Risk Assessment", "Action Items"),
                Map.of("includeHealthScore", true, "includeTrends", true))
        );
    }

    private List<ReportTemplate> generateRevenueTemplates() {
        return List.of(
            template("Revenue Summary Template", "Revenue breakdown and analysis",
                ReportCategory.REVENUE, ReportType.WEEKLY, "Finance",
                List.of("Revenue Summary", "Channel Breakdown", "Category Performance", "Forecast"),
                Map.of("includeCharts", true, "includeForecast", true, "currencyFormat", "USD")),
            template("Revenue Forecast Template", "Revenue projection format",
                ReportCategory.REVENUE, ReportType.QUARTERLY, "Finance",
                List.of("Projection Overview", "Assumptions", "Scenario Analysis", "Risk Factors"),
                Map.of("includeScenarios", true, "includeCharts", true))
        );
    }

    private List<ReportTemplate> generateOperationalTemplates() {
        return List.of(
            template("Order Operations Template", "Order processing and fulfillment report",
                ReportCategory.ORDERS, ReportType.WEEKLY, "Operations",
                List.of("Order Volume", "Fulfillment Metrics", "Carrier Performance", "Returns Analysis"),
                Map.of("includeVolumeChart", true, "includeCarrierBreakdown", true)),
            template("Inventory Health Template", "Inventory status and analysis",
                ReportCategory.INVENTORY, ReportType.WEEKLY, "Inventory",
                List.of("Inventory Summary", "Stockout Risk", "Overstock Items", "Turnover Analysis"),
                Map.of("includeStockoutAlerts", true, "includeValueChart", true))
        );
    }

    private List<ReportTemplate> generateAiTemplates() {
        return List.of(
            template("AI Platform Utilization Template", "AI model usage and cost report",
                ReportCategory.AI_PLATFORM, ReportType.WEEKLY, "AI",
                List.of("Model Usage Summary", "Cost Breakdown", "Performance Metrics", "Optimization Recommendations"),
                Map.of("includeCostChart", true, "includeLatencyMetrics", true)),
            template("Automation Operations Template", "Workflow automation metrics",
                ReportCategory.AUTOMATION, ReportType.WEEKLY, "Automation",
                List.of("Workflow Summary", "Success Rates", "Task Execution", "Time Savings"),
                Map.of("includeSuccessChart", true, "includeTimeSavings", true))
        );
    }

    private List<ReportTemplate> generateHealthTemplates() {
        return List.of(
            template("Platform Health Template", "Infrastructure and platform health",
                ReportCategory.PLATFORM_HEALTH, ReportType.DAILY, "Engineering",
                List.of("Uptime Summary", "Incident Log", "Resource Utilization", "Latency Metrics"),
                Map.of("includeUptimeChart", true, "includeResourceGraphs", true)),
            template("Platform Health Weekly Template", "Weekly platform health deep dive",
                ReportCategory.PLATFORM_HEALTH, ReportType.WEEKLY, "Engineering",
                List.of("Weekly Summary", "Incident Analysis", "Capacity Planning", "SLO Compliance"),
                Map.of("includeSloMetrics", true, "includeCapacityForecast", true))
        );
    }

    private List<ReportTemplate> generateRiskTemplates() {
        return List.of(
            template("Risk Summary Template", "Enterprise risk assessment report",
                ReportCategory.RISK, ReportType.MONTHLY, "Risk",
                List.of("Risk Overview", "Critical Risks", "Mitigation Status", "Trend Analysis"),
                Map.of("includeRiskMatrix", true, "includeMitigationTracker", true))
        );
    }

    private List<ReportTemplate> generateComplianceTemplates() {
        return List.of(
            template("Compliance Status Template", "Regulatory compliance report",
                ReportCategory.COMPLIANCE, ReportType.MONTHLY, "Compliance",
                List.of("Compliance Summary", "Audit Status", "Open Findings", "Upcoming Deadlines"),
                Map.of("includeAuditTimeline", true, "includeFindingsTracker", true))
        );
    }

    private ReportTemplate template(String name, String description, ReportCategory category,
                                     ReportType type, String owner,
                                     List<String> sections, Map<String, Object> defaultConfig) {
        ReportTemplate template = ReportTemplate.create(name, description, category, type,
            owner, sections, defaultConfig);
        return repository.saveTemplate(template);
    }
}
