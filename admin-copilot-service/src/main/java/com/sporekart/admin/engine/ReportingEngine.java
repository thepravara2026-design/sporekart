package com.sporekart.admin.engine;

import com.sporekart.admin.domain.PerformanceReport;
import com.sporekart.admin.domain.PerformanceReport.ReportType;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.*;

@Component
public class ReportingEngine {

    public PerformanceReport generateReport(String title, String type,
                                            LocalDate fromDate, LocalDate toDate,
                                            List<String> metrics) {
        ReportType reportType;
        try {
            reportType = ReportType.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            reportType = ReportType.PDF;
        }

        Map<String, Object> metricsData = new LinkedHashMap<>();
        for (String metric : metrics) {
            switch (metric.toLowerCase()) {
                case "revenue":
                    metricsData.put("totalRevenue", 1250000);
                    metricsData.put("averageDailyRevenue", 41667);
                    break;
                case "orders":
                    metricsData.put("totalOrders", 450);
                    metricsData.put("completedOrders", 427);
                    metricsData.put("cancelledOrders", 23);
                    break;
                case "customers":
                    metricsData.put("totalCustomers", 8920);
                    metricsData.put("newCustomers", 1240);
                    break;
                default:
                    metricsData.put(metric, "N/A");
            }
        }

        return new PerformanceReport(
            UUID.randomUUID().toString(),
            title,
            reportType,
            OffsetDateTime.now(),
            fromDate + " - " + toDate,
            metricsData,
            List.of("revenue_trend", "order_distribution"),
            Map.of("fromDate", fromDate.toString(), "toDate", toDate.toString(), "type", type),
            "/reports/" + UUID.randomUUID()
        );
    }

    public PerformanceReport generateSalesReport(String title, String period) {
        Map<String, Object> salesMetrics = new LinkedHashMap<>();
        salesMetrics.put("totalRevenue", 1250000);
        salesMetrics.put("totalOrders", 450);
        salesMetrics.put("averageOrderValue", 2778);
        salesMetrics.put("growthRate", 12.5);
        salesMetrics.put("topProducts", List.of("Fresh Vegetables Pack", "Organic Milk", "Sourdough Bread"));
        salesMetrics.put("period", period);

        return new PerformanceReport(
            UUID.randomUUID().toString(),
            title,
            ReportType.PDF,
            OffsetDateTime.now(),
            period,
            salesMetrics,
            List.of("sales_trend", "category_breakdown"),
            Map.of("type", "sales", "period", period),
            "/reports/sales/" + UUID.randomUUID()
        );
    }

    public PerformanceReport generateDashboardReport(String title) {
        Map<String, Object> kpiData = new LinkedHashMap<>();
        kpiData.put("kpis", Map.of(
            "revenue", 1250000,
            "orders", 450,
            "customers", 8920,
            "growth", 12.5
        ));
        kpiData.put("period", "Last 30 Days");

        return new PerformanceReport(
            UUID.randomUUID().toString(),
            title,
            ReportType.PDF,
            OffsetDateTime.now(),
            "current",
            kpiData,
            List.of("kpi_summary", "trend_chart"),
            Map.of("type", "dashboard"),
            "/reports/dashboard/" + UUID.randomUUID()
        );
    }

    public byte[] exportReport(PerformanceReport report, String format) {
        switch (format.toUpperCase()) {
            case "CSV":
                return exportToCsv(report);
            case "JSON":
                return exportToJson(report);
            default:
                return exportToCsv(report);
        }
    }

    private byte[] exportToCsv(PerformanceReport report) {
        StringBuilder csv = new StringBuilder();
        csv.append("Metric,Value\n");
        for (Map.Entry<String, Object> entry : report.metrics().entrySet()) {
            csv.append(entry.getKey()).append(",").append(entry.getValue()).append("\n");
        }
        return csv.toString().getBytes(StandardCharsets.UTF_8);
    }

    private byte[] exportToJson(PerformanceReport report) {
        StringBuilder json = new StringBuilder();
        json.append("{\n");
        json.append("  \"title\": \"").append(report.title()).append("\",\n");
        json.append("  \"type\": \"").append(report.type()).append("\",\n");
        json.append("  \"metrics\": {\n");
        int i = 0;
        for (Map.Entry<String, Object> entry : report.metrics().entrySet()) {
            json.append("    \"").append(entry.getKey()).append("\": ");
            if (entry.getValue() instanceof String) {
                json.append("\"").append(entry.getValue()).append("\"");
            } else {
                json.append(entry.getValue());
            }
            if (i < report.metrics().size() - 1) {
                json.append(",");
            }
            json.append("\n");
            i++;
        }
        json.append("  }\n");
        json.append("}\n");
        return json.toString().getBytes(StandardCharsets.UTF_8);
    }
}
