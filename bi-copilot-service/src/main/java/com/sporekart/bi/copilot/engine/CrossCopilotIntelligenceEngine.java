package com.sporekart.bi.copilot.engine;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.sporekart.bi.copilot.domain.BusinessInsight;
import com.sporekart.bi.copilot.domain.CrossCopilotMetric;

@Component
public class CrossCopilotIntelligenceEngine {

    private static final Logger log = LoggerFactory.getLogger(CrossCopilotIntelligenceEngine.class);
    private static final Random RANDOM = new Random(404);

    private static final List<String> COPILOTS = List.of("Customer Copilot", "Admin Copilot", "Trainer Copilot", "Grower Copilot");
    private static final double[] REVENUE_SHARES = {0.45, 0.20, 0.15, 0.20};
    private static final double[] LATENCIES_MS = {120, 95, 145, 110};
    private static final double[] SATISFACTION = {4.5, 4.2, 4.3, 4.6};
    private static final int[] ACTIVE_SESSIONS = {850, 320, 180, 420};

    public CrossCopilotIntelligenceEngine() {
        log.info("CrossCopilotIntelligenceEngine initialized");
    }

    public List<CrossCopilotMetric> getCrossCopilotMetrics() {
        List<CrossCopilotMetric> metrics = new ArrayList<>();
        String[] metricNames = {"revenue", "active_sessions", "avg_latency", "satisfaction"};

        for (int i = 0; i < COPILOTS.size(); i++) {
            String copilot = COPILOTS.get(i);
            for (String metricName : metricNames) {
                double currentValue = switch (metricName) {
                    case "revenue" -> REVENUE_SHARES[i] * 100;
                    case "active_sessions" -> ACTIVE_SESSIONS[i];
                    case "avg_latency" -> LATENCIES_MS[i] + RANDOM.nextDouble() * 10 - 5;
                    case "satisfaction" -> SATISFACTION[i] + RANDOM.nextDouble() * 0.2 - 0.1;
                    default -> 0;
                };
                double previousValue = currentValue * (0.92 + RANDOM.nextDouble() * 0.08);
                double change = previousValue > 0 ? (currentValue - previousValue) / previousValue * 100 : 0;

                metrics.add(new CrossCopilotMetric(
                    UUID.randomUUID().toString(),
                    copilot,
                    metricName,
                    Math.round(currentValue * 100) / 100.0,
                    Math.round(previousValue * 100) / 100.0,
                    Math.round(change * 100) / 100.0,
                    change >= 0 ? "up" : "down",
                    copilot,
                    OffsetDateTime.now()
                ));
            }
        }
        return metrics;
    }

    public Map<String, Object> getCopilotPerformanceComparison() {
        Map<String, Object> comparison = new LinkedHashMap<>();

        for (int i = 0; i < COPILOTS.size(); i++) {
            Map<String, Object> perf = new LinkedHashMap<>();
            perf.put("activeSessions", ACTIVE_SESSIONS[i]);
            perf.put("avgLatencyMs", Math.round((LATENCIES_MS[i] + RANDOM.nextDouble() * 10 - 5) * 100) / 100.0);
            perf.put("userSatisfaction", Math.round((SATISFACTION[i] + RANDOM.nextDouble() * 0.2 - 0.1) * 100) / 100.0);
            perf.put("revenueShare", REVENUE_SHARES[i] * 100);
            perf.put("totalQueries", ACTIVE_SESSIONS[i] * (20 + RANDOM.nextInt(30)));
            perf.put("resolutionRate", Math.round((0.85 + RANDOM.nextDouble() * 0.12) * 10000) / 100.0);
            comparison.put(COPILOTS.get(i), perf);
        }

        return comparison;
    }

    public Map<String, Object> getUnifiedBusinessHealth() {
        double revenueHealth = 85 + RANDOM.nextDouble() * 10;
        double customerHealth = 78 + RANDOM.nextDouble() * 12;
        double trainingHealth = 72 + RANDOM.nextDouble() * 15;
        double cultivationHealth = 82 + RANDOM.nextDouble() * 10;
        double overall = (revenueHealth + customerHealth + trainingHealth + cultivationHealth) / 4.0;

        Map<String, Object> health = new LinkedHashMap<>();
        health.put("overallHealthScore", Math.round(overall * 100) / 100.0);
        health.put("status", overall >= 80 ? "healthy" : overall >= 60 ? "warning" : "critical");

        Map<String, Object> components = new LinkedHashMap<>();
        components.put("revenue", Math.round(revenueHealth * 100) / 100.0);
        components.put("customer", Math.round(customerHealth * 100) / 100.0);
        components.put("training", Math.round(trainingHealth * 100) / 100.0);
        components.put("cultivation", Math.round(cultivationHealth * 100) / 100.0);
        health.put("componentScores", components);

        List<String> alerts = List.of(
            "Customer acquisition cost increased 12% this quarter",
            "Training enrollment growing at 8% MoM",
            "Cultivation yield within expected range"
        );
        health.put("activeAlerts", alerts);

        return health;
    }

    public List<BusinessInsight> getCorrelationInsights() {
        List<BusinessInsight> insights = new ArrayList<>();

        insights.add(new BusinessInsight(
            UUID.randomUUID().toString(),
            "Customer Engagement Drives Revenue",
            "High customer satisfaction scores correlate with 23% higher revenue per customer",
            "correlation",
            "positive",
            "Invest in customer experience and retention programs",
            0.87,
            Map.of("correlationCoefficient", 0.87, "sampleSize", 1200),
            List.of("customer_satisfaction", "revenue_per_customer"),
            true,
            OffsetDateTime.now(),
            OffsetDateTime.now().plusDays(90)
        ));

        insights.add(new BusinessInsight(
            UUID.randomUUID().toString(),
            "Training Impact on Yield Quality",
            "Growers who completed advanced training show 34% lower contamination rates",
            "correlation",
            "positive",
            "Expand training programs for commercial growers",
            0.82,
            Map.of("correlationCoefficient", 0.82, "trainedGrowers", 145),
            List.of("training_completion", "contamination_rate"),
            true,
            OffsetDateTime.now(),
            OffsetDateTime.now().plusDays(90)
        ));

        insights.add(new BusinessInsight(
            UUID.randomUUID().toString(),
            "Seasonal Demand Patterns",
            "Revenue shows 18% uplift in Q4 driven by festive season demand",
            "seasonal",
            "info",
            "Plan inventory and marketing campaigns for Q4 surge",
            0.76,
            Map.of("q4Uplift", 0.18, "peakMonths", List.of(10, 11, 12)),
            List.of("seasonal_revenue", "inventory_turnover"),
            false,
            OffsetDateTime.now(),
            OffsetDateTime.now().plusDays(60)
        ));

        insights.add(new BusinessInsight(
            UUID.randomUUID().toString(),
            "Wholesale Channel Growth Opportunity",
            "Wholesale channel revenue growing at 15% MoM with 40% higher margins",
            "opportunity",
            "positive",
            "Allocate more resources to wholesale channel development",
            0.79,
            Map.of("moMGrowth", 0.15, "marginPremium", 0.40),
            List.of("wholesale_revenue", "channel_margins"),
            true,
            OffsetDateTime.now(),
            OffsetDateTime.now().plusDays(60)
        ));

        return insights;
    }

    public Map<String, Double> getRevenueContributionByCopilot() {
        Map<String, Double> contribution = new LinkedHashMap<>();
        for (int i = 0; i < COPILOTS.size(); i++) {
            contribution.put(COPILOTS.get(i), REVENUE_SHARES[i] * 100);
        }
        return contribution;
    }

    public List<BusinessInsight> getTopCombinedInsights(int limit) {
        List<BusinessInsight> all = getCorrelationInsights();
        return all.subList(0, Math.min(limit, all.size()));
    }

    public Map<String, Object> generateWeeklyDigest() {
        Map<String, Object> digest = new LinkedHashMap<>();
        digest.put("week", "2025-W" + (30 + RANDOM.nextInt(20)));
        digest.put("generatedAt", OffsetDateTime.now().toString());

        Map<String, Object> summary = new LinkedHashMap<>();
        summary.put("totalRevenue", Math.round((5000000 + RANDOM.nextDouble() * 1000000) * 100) / 100.0);
        summary.put("totalOrders", 2500 + RANDOM.nextInt(500));
        summary.put("activeCustomers", 1200 + RANDOM.nextInt(200));
        summary.put("newCustomers", 80 + RANDOM.nextInt(40));
        summary.put("avgOrderValue", Math.round((350 + RANDOM.nextDouble() * 100) * 100) / 100.0);
        summary.put("overallSatisfaction", Math.round((4.0 + RANDOM.nextDouble() * 0.5) * 100) / 100.0);

        Map<String, Object> copilotMetrics = new LinkedHashMap<>();
        for (int i = 0; i < COPILOTS.size(); i++) {
            Map<String, Object> cm = new LinkedHashMap<>();
            cm.put("activeSessions", ACTIVE_SESSIONS[i] + RANDOM.nextInt(50) - 25);
            cm.put("avgLatencyMs", Math.round((LATENCIES_MS[i] + RANDOM.nextDouble() * 10 - 5) * 100) / 100.0);
            cm.put("satisfaction", Math.round((SATISFACTION[i] + RANDOM.nextDouble() * 0.2 - 0.1) * 100) / 100.0);
            copilotMetrics.put(COPILOTS.get(i), cm);
        }

        digest.put("summary", summary);
        digest.put("copilotMetrics", copilotMetrics);
        digest.put("topInsight", getCorrelationInsights().getFirst());

        return digest;
    }
}
