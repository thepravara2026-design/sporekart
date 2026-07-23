package com.sporekart.bi.copilot.engine;

import com.sporekart.bi.copilot.domain.ReportDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@Component
public class ReportingEngine {

    private static final Logger log = LoggerFactory.getLogger(ReportingEngine.class);

    private final List<ReportDefinition> scheduledReports;

    public ReportingEngine() {
        this.scheduledReports = new CopyOnWriteArrayList<>();
        log.info("Initialized ReportingEngine");
    }

    public Map<String, Object> generateReport(ReportDefinition request) {
        log.debug("Generating report: {} type={} format={}", request.name(), request.type(), request.format());
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("reportId", UUID.randomUUID().toString());
        result.put("name", request.name());
        result.put("type", request.type());
        result.put("format", request.format());
        result.put("generatedAt", OffsetDateTime.now().toString());
        result.put("status", "completed");
        result.put("content", generateReportContent(request.type(), request.filters()));
        result.put("metrics", request.metrics());
        result.put("dimensions", request.dimensions());
        return result;
    }

    public Map<String, Object> generateRevenueReport(String format, Map<String, Object> filters) {
        log.debug("Generating revenue report format={}", format);
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("totalRevenue", 2450000.0);
        data.put("totalOrders", 4200);
        data.put("averageOrderValue", 583.33);
        data.put("revenueByRegion", Map.of("North", 980000.0, "South", 612500.0, "East", 490000.0, "West", 367500.0));
        data.put("revenueByProduct", Map.of("Oyster Mushroom Spawn", 850000.0, "Shiitake Spawn", 650000.0,
                "Button Mushroom Spawn", 500000.0, "Training Courses", 280000.0, "Equipment", 170000.0));
        data.put("growthRate", 8.5);
        return Map.of("reportId", UUID.randomUUID().toString(), "type", "revenue", "format", format,
                "generatedAt", OffsetDateTime.now().toString(), "filters", filters, "data", data,
                "content", generateReportContent("revenue", data));
    }

    public Map<String, Object> generateCustomerReport(String format, Map<String, Object> filters) {
        log.debug("Generating customer report format={}", format);
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("totalCustomers", 2840);
        data.put("newCustomers", 320);
        data.put("churnedCustomers", 85);
        data.put("churnRate", 3.2);
        data.put("retentionRate", 87.5);
        data.put("customerLifetimeValue", 5200.0);
        data.put("acquisitionCost", 850.0);
        data.put("customersBySegment", Map.of("High-Value", 250, "New Grower", 850, "Hobbyist", 1200,
                "Commercial", 80, "Distributor", 120, "At-Risk", 340));
        return Map.of("reportId", UUID.randomUUID().toString(), "type", "customer", "format", format,
                "generatedAt", OffsetDateTime.now().toString(), "filters", filters, "data", data,
                "content", generateReportContent("customer", data));
    }

    public Map<String, Object> generateTrainingReport(String format, Map<String, Object> filters) {
        log.debug("Generating training report format={}", format);
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("totalStudents", 1560);
        data.put("totalBatches", 48);
        data.put("activeBatches", 12);
        data.put("completedBatches", 36);
        data.put("completionRate", 78.4);
        data.put("averageScore", 82.6);
        data.put("totalCertificationsIssued", 980);
        data.put("pendingCertifications", 220);
        data.put("revenueFromTraining", 280000.0);
        data.put("trainingCost", 195000.0);
        data.put("trainingProfitMargin", 30.3);
        data.put("scoreByModule", Map.of("Mushroom Biology", 85.0, "Sterilization Techniques", 79.0,
                "Substrate Preparation", 83.0, "Environmental Control", 81.0, "Harvesting & Storage", 88.0,
                "Business of Mushroom Farming", 76.0));
        return Map.of("reportId", UUID.randomUUID().toString(), "type", "training", "format", format,
                "generatedAt", OffsetDateTime.now().toString(), "filters", filters, "data", data,
                "content", generateReportContent("training", data));
    }

    public Map<String, Object> generateCultivationReport(String format, Map<String, Object> filters) {
        log.debug("Generating cultivation report format={}", format);
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("totalYieldKg", 45000.0);
        data.put("averageYieldPerBatch", 320.0);
        data.put("yieldBySpecies", Map.of("Oyster", 18000.0, "Shiitake", 12000.0, "Button", 10000.0, "Lion's Mane", 3000.0, "Enoki", 2000.0));
        data.put("yieldByRegion", Map.of("North", 13500.0, "South", 11250.0, "East", 10500.0, "West", 9750.0));
        data.put("averageCycleTime", 45.0);
        data.put("contaminationRate", 3.8);
        data.put("diseaseIncidence", 2.1);
        data.put("totalGrowers", 180);
        data.put("growerSatisfactionScore", 4.2);
        return Map.of("reportId", UUID.randomUUID().toString(), "type", "cultivation", "format", format,
                "generatedAt", OffsetDateTime.now().toString(), "filters", filters, "data", data,
                "content", generateReportContent("cultivation", data));
    }

    public Map<String, Object> generateExecutiveSummary(String format) {
        log.debug("Generating executive summary format={}", format);
        Map<String, Object> summary = new LinkedHashMap<>();
        summary.put("period", OffsetDateTime.now().getMonth().name() + " " + OffsetDateTime.now().getYear());
        summary.put("keyMetrics", Map.of(
                "revenue", "Rs.24,50,000", "revenueGrowth", "+8.5%",
                "activeCustomers", 2840, "customerGrowth", "+12.7%",
                "trainingCompletion", "78.4%", "yieldTotal", "45,000 kg"
        ));
        summary.put("highlights", List.of(
                "Revenue growth accelerating with 8.5% month-over-month increase",
                "Customer base expanded 12.7% driven by New Grower segment",
                "Training division achieved 30% profit margin",
                "Contamination rate reduced to 3.8% across all facilities"
        ));
        summary.put("risks", List.of(
                "Customer churn increasing in New Grower segment (18.5%)",
                "South region revenue declining for 2 consecutive quarters",
                "Training completion rate dropped 8%"
        ));
        summary.put("recommendations", List.of(
                "Expand North region distribution network",
                "Revamp new grower onboarding program",
                "Audit South facility sterilization processes"
        ));
        return Map.of("reportId", UUID.randomUUID().toString(), "type", "executive_summary", "format", format,
                "generatedAt", OffsetDateTime.now().toString(), "data", summary,
                "content", generateReportContent("executive_summary", summary));
    }

    public Map<String, Object> generateComprehensiveReport(String format) {
        log.debug("Generating comprehensive report format={}", format);
        Map<String, Object> report = new LinkedHashMap<>();
        report.put("executiveSummary", generateExecutiveSummary(format).get("data"));
        report.put("revenue", generateRevenueReport(format, Map.of()).get("data"));
        report.put("customer", generateCustomerReport(format, Map.of()).get("data"));
        report.put("training", generateTrainingReport(format, Map.of()).get("data"));
        report.put("cultivation", generateCultivationReport(format, Map.of()).get("data"));
        report.put("generatedAt", OffsetDateTime.now().toString());
        return Map.of("reportId", UUID.randomUUID().toString(), "type", "comprehensive", "format", format,
                "generatedAt", OffsetDateTime.now().toString(), "data", report,
                "content", generateReportContent("comprehensive", report));
    }

    public ReportDefinition scheduleReport(ReportDefinition definition) {
        log.debug("Scheduling report: {} type={} schedule={}", definition.name(), definition.type(), definition.schedule());
        ReportDefinition scheduled = new ReportDefinition(
                UUID.randomUUID().toString(), definition.name(), definition.type(), definition.format(),
                definition.schedule(), definition.metrics(), definition.dimensions(),
                definition.filters(), definition.recipients(), "scheduled", null
        );
        scheduledReports.add(scheduled);
        return scheduled;
    }

    public List<ReportDefinition> getScheduledReports() {
        log.debug("Returning {} scheduled reports", scheduledReports.size());
        return List.copyOf(scheduledReports);
    }

    public String generateReportContent(String reportType, Map<String, Object> data) {
        if (data == null || data.isEmpty()) return "No data available";
        StringBuilder sb = new StringBuilder();
        sb.append("=== ").append(reportType.toUpperCase().replace("_", " ")).append(" REPORT ===\n");
        sb.append("Generated: ").append(OffsetDateTime.now()).append("\n\n");
        data.forEach((key, value) -> {
            sb.append(key).append(": ");
            if (value instanceof Map) {
                sb.append("\n");
                ((Map<?, ?>) value).forEach((k, v) -> sb.append("  ").append(k).append(": ").append(v).append("\n"));
            } else if (value instanceof List) {
                sb.append("\n");
                ((List<?>) value).forEach(item -> sb.append("  - ").append(item).append("\n"));
            } else {
                sb.append(value).append("\n");
            }
        });
        return sb.toString();
    }
}
