package com.sporekart.admin.engine;

import com.sporekart.admin.domain.BusinessInsight;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class BusinessInsightsEngine {

    public BusinessInsight generateDailySummary() {
        return new BusinessInsight(
            "Strong business performance with double-digit growth across revenue, orders, and customer acquisition. " +
            "Inventory levels require attention with 18 SKUs approaching reorder thresholds. " +
            "Customer retention improved 5% month-over-month driven by loyalty program enhancements.",
            Map.of(
                "totalRevenue", 1250000,
                "totalOrders", 450,
                "activeUsers", 1250,
                "newCustomers", 1240,
                "growthRate", 12.5,
                "retentionRate", 91.5
            ),
            List.of(
                Map.of("metric", "revenue", "direction", "up", "change", 12.5, "period", "30d"),
                Map.of("metric", "orders", "direction", "up", "change", 8.3, "period", "30d"),
                Map.of("metric", "customers", "direction", "up", "change", 5.1, "period", "30d")
            ),
            List.of("Low stock alerts for 18 SKUs requiring immediate replenishment"),
            List.of("Expand cold storage capacity to meet growing demand in tier-2 cities"),
            List.of("Increase safety stock for top 20 high-velocity products before peak season")
        );
    }

    public List<BusinessInsight> getSalesInsights() {
        List<BusinessInsight> insights = new ArrayList<>();

        insights.add(new BusinessInsight(
            "Revenue increased 12.5% compared to previous period, driven by strong produce and dairy sales.",
            Map.of("currentValue", 1250000, "previousValue", 1111111, "changePercentage", 12.5),
            List.of(Map.of("period", "Current", "value", 1250000), Map.of("period", "Previous", "value", 1111111)),
            List.of(), List.of(), List.of()
        ));

        insights.add(new BusinessInsight(
            "Order volume grew 8.3% with average order value increasing to Rs. 2,778.",
            Map.of("currentValue", 450, "previousValue", 415, "changePercentage", 8.43),
            List.of(Map.of("period", "Current", "orders", 450), Map.of("period", "Previous", "orders", 415)),
            List.of(), List.of(), List.of()
        ));

        insights.add(new BusinessInsight(
            "Customer acquisition cost decreased 3.2% while conversion rate improved to 4.8%.",
            Map.of("currentValue", 4.8, "previousValue", 4.2, "changePercentage", 14.29),
            List.of(Map.of("period", "Current", "conversion", 4.8), Map.of("period", "Previous", "conversion", 4.2)),
            List.of(), List.of(), List.of()
        ));

        return insights;
    }

    public List<BusinessInsight> getInventoryInsights() {
        List<BusinessInsight> insights = new ArrayList<>();

        insights.add(new BusinessInsight(
            "Low stock alert: 18 products are below minimum threshold. Immediate replenishment needed for top 5 SKUs.",
            Map.of("currentValue", 18, "previousValue", 12, "changePercentage", 50.0),
            List.of(Map.of("product", "Organic Tomatoes", "stock", 12, "threshold", 50)),
            List.of("Risk of stockout for high-demand produce items"),
            List.of("Implement dynamic reorder points based on sales velocity"),
            List.of("Expedite pending purchase orders for top 10 low-stock items")
        ));

        insights.add(new BusinessInsight(
            "Inventory turnover rate is 6.2x annually, indicating healthy stock movement. Slow-moving items identified in spices category.",
            Map.of("currentValue", 6.2, "previousValue", 5.8, "changePercentage", 6.9),
            List.of(),
            List.of("Slow-moving inventory valued at Rs. 450,000 in spices category"),
            List.of("Bundle slow-moving spices with high-demand items to clear inventory"),
            List.of("Review pricing strategy for slow-moving inventory")
        ));

        return insights;
    }

    public List<BusinessInsight> getRiskIndicators() {
        List<BusinessInsight> risks = new ArrayList<>();

        risks.add(new BusinessInsight(
            "Payment gateway latency exceeds threshold, impacting checkout completion rates.",
            Map.of("currentValue", 5200.0, "previousValue", 2000.0, "changePercentage", 160.0),
            List.of(),
            List.of("Payment gateway response time 5.2s exceeds 2s threshold; 12% checkout abandonment increase"),
            List.of("Enable fallback payment gateway and contact provider for SLA compliance"),
            List.of()
        ));

        risks.add(new BusinessInsight(
            "18 inventory SKUs at critically low levels with 3 already out of stock.",
            Map.of("currentValue", 18, "previousValue", 8, "changePercentage", 125.0),
            List.of(),
            List.of("Potential revenue loss of Rs. 350,000 if stockouts not addressed within 48 hours"),
            List.of("Prioritize replenishment for top 10 revenue-generating low-stock products"),
            List.of()
        ));

        risks.add(new BusinessInsight(
            "Customer churn rate increased to 8.5% this quarter, above target of 5%.",
            Map.of("currentValue", 8.5, "previousValue", 5.0, "changePercentage", 70.0),
            List.of(),
            List.of("Churned customers concentrated in tier-3 cities; delivery experience issues reported"),
            List.of("Launch targeted re-engagement campaign and review delivery partner performance"),
            List.of()
        ));

        return risks;
    }

    public List<String> getActionRecommendations() {
        return List.of(
            "Increase safety stock for top 20 high-velocity products before peak season",
            "Launch targeted retention campaign for inactive customers in tier-3 cities",
            "Optimize pricing strategy for top 10 products to improve margins",
            "Implement dynamic reorder points based on real-time sales velocity",
            "Expand cold storage capacity to meet growing demand in tier-2 cities",
            "Review payment gateway redundancy and failover procedures",
            "Accelerate training program for new warehouse staff to improve fulfillment speed",
            "Deploy automated inventory forecasting for high-demand categories"
        );
    }
}
