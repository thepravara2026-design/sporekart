package com.sporekart.executive.copilot.engine;

import com.sporekart.executive.copilot.domain.*;
import com.sporekart.executive.copilot.dto.DashboardResponse;
import com.sporekart.executive.copilot.dto.DashboardResponse.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ExecutiveDashboardEngine {

    private static final Logger log = LoggerFactory.getLogger(ExecutiveDashboardEngine.class);

    public DashboardResponse getDashboard() {
        log.info("Generating executive dashboard");
        var summary = "SporeKart is performing well with strong growth across all key metrics. "
            + "Revenue up 13.6% YoY, customer base growing 18%, and operational efficiency improving. "
            + "Key watch items: inventory dead stock and revenue concentration.";

        var health = new DashboardResponse.CompanyHealth(82.5, 2.5, "LOW",
            Map.of("revenue", 82.5, "profitability", 75.0, "growth", 88.0, "customer", 78.5,
                "operations", 85.0, "marketing", 80.0, "inventory", 72.0, "training", 90.0));

        var financialHighlights = new LinkedHashMap<String, Object>();
        financialHighlights.put("revenue", "Rs. 1.25Cr");
        financialHighlights.put("profit", "Rs. 30L");
        financialHighlights.put("margin", "24%");
        financialHighlights.put("cashPosition", "Rs. 45L");
        financialHighlights.put("growthRate", "13.6%");

        var recommendations = List.of(
            new DashboardResponse.RecommendationItem("South India Expansion", "EXPANSION", 185.0, 92.0, 0.85, "6-9 months"),
            new DashboardResponse.RecommendationItem("Warehouse Automation Investment", "INVESTMENT", 150.0, 85.0, 0.82, "4-6 months"),
            new DashboardResponse.RecommendationItem("B2B Training Vertical", "TRAINING", 165.0, 78.0, 0.80, "3-4 months")
        );

        var risks = new DashboardResponse.RiskSummary(42.5, "MODERATE", 0, 2);

        var metrics = List.of(
            new DashboardResponse.MetricItem("Revenue Growth", "Financial", 13.6, 12.0, 20.0, "%", "UP"),
            new DashboardResponse.MetricItem("Customer Retention", "Customer", 78.0, 75.0, 85.0, "%", "UP"),
            new DashboardResponse.MetricItem("Order Fulfillment", "Operations", 94.5, 93.0, 98.0, "%", "UP"),
            new DashboardResponse.MetricItem("Training Completion", "Training", 92.0, 88.0, 95.0, "%", "UP"),
            new DashboardResponse.MetricItem("Inventory Turnover", "Inventory", 6.5, 6.0, 8.0, "x", "UP")
        );

        var alerts = List.of(
            "Inventory dead stock increased to Rs. 85K - review clearance strategy",
            "Revenue concentration: Top 3 products = 62% of revenue",
            "Customer acquisition cost up 8% QoQ"
        );

        return new DashboardResponse(summary, health, financialHighlights, recommendations, risks, metrics, alerts);
    }

    public Map<String, Object> getTodaySummary() {
        log.debug("Getting today's business summary");
        var summary = new LinkedHashMap<String, Object>();
        summary.put("date", java.time.LocalDate.now().toString());
        summary.put("ordersToday", 85);
        summary.put("revenueToday", 125000.0);
        summary.put("newCustomersToday", 12);
        summary.put("alertsActive", 3);
        summary.put("topPerformer", "Training department");
        summary.put("needsAttention", "Inventory management");
        return summary;
    }

    public Map<String, Object> getWeeklyReport() {
        log.debug("Getting weekly executive report");
        var report = new LinkedHashMap<String, Object>();
        report.put("week", "Week 30");
        report.put("orders", 580);
        report.put("revenue", 875000.0);
        report.put("newCustomers", 85);
        report.put("fulfillmentRate", 94.2);
        report.put("topSKU", "Oyster Mushroom Spawn");
        report.put("keyEvent", "New training batch launched - 45 enrollments");
        return report;
    }

    public Map<String, Object> getMonthlyReport() {
        log.debug("Getting monthly executive report");
        var report = new LinkedHashMap<String, Object>();
        report.put("month", "July 2026");
        report.put("orders", 2450);
        report.put("revenue", 3800000.0);
        report.put("expenses", 2900000.0);
        report.put("profit", 900000.0);
        report.put("newCustomers", 350);
        report.put("activeCustomers", 12500);
        report.put("topDepartment", "Training (92% completion rate)");
        return report;
    }
}
