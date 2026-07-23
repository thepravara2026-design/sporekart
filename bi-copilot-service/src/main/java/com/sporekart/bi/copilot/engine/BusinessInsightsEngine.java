package com.sporekart.bi.copilot.engine;

import com.sporekart.bi.copilot.domain.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class BusinessInsightsEngine {

    private static final Logger log = LoggerFactory.getLogger(BusinessInsightsEngine.class);

    private final List<BusinessInsight> insightTemplates;

    public BusinessInsightsEngine() {
        this.insightTemplates = seedInsightTemplates();
        log.info("Initialized BusinessInsightsEngine with {} insight templates", insightTemplates.size());
    }

    private List<BusinessInsight> seedInsightTemplates() {
        List<BusinessInsight> list = new ArrayList<>();

        list.add(insight("Revenue growth accelerating in North region - consider expanding distribution",
                "revenue", "high", "Expand distribution network in North region to capture growing demand", 0.92));
        list.add(insight("South region revenue declining for 2 consecutive quarters - investigate market conditions",
                "revenue", "critical", "Conduct market analysis for South region and develop recovery plan", 0.88));
        list.add(insight("Average order value increased 12% month-over-month - premium products gaining traction",
                "revenue", "medium", "Increase premium product inventory allocation by 20%", 0.85));
        list.add(insight("Online channel revenue surpassed retail for the first time - digital transformation succeeding",
                "revenue", "high", "Accelerate digital marketing spend and e-commerce platform enhancements", 0.91));
        list.add(insight("Revenue per customer dropped 7% - cross-sell opportunities being missed",
                "revenue", "high", "Launch targeted cross-sell campaign based on purchase history", 0.78));
        list.add(insight("Subscription revenue grew 25% year-over-year - recurring revenue model working",
                "revenue", "medium", "Develop new subscription tiers to capture additional customer segments", 0.94));
        list.add(insight("Seasonal dip detected in Q1 revenue - typical pattern but 3% worse than last year",
                "revenue", "low", "Introduce Q1 promotions and loyalty incentives to offset seasonal slump", 0.83));
        list.add(insight("International revenue hit 15% of total - expansion into new markets gaining momentum",
                "revenue", "medium", "Evaluate top 3 international markets for dedicated local teams", 0.87));

        list.add(insight("Customer churn rate increasing in New Growers segment - improve onboarding",
                "customer", "critical", "Revamp onboarding flow with personalized mentorship program", 0.90));
        list.add(insight("Customer satisfaction score declined 5% - support response time increased",
                "customer", "high", "Hire additional support staff and implement chatbot for Tier 1 queries", 0.84));
        list.add(insight("Repeat purchase rate increased 8% - loyalty program driving retention",
                "customer", "medium", "Expand loyalty program benefits to include exclusive training content", 0.88));
        list.add(insight("High-value customer segment grew 15% - concentrate on white-glove service",
                "customer", "high", "Assign dedicated account managers for high-value customer accounts", 0.91));

        list.add(insight("Training completion rate dropped 8% - review curriculum difficulty",
                "training", "high", "Conduct curriculum audit and introduce adaptive learning paths", 0.86));
        list.add(insight("Advanced cultivation course has 95% completion rate - model for other courses",
                "training", "medium", "Apply advanced course instructional design to beginner and intermediate levels", 0.82));
        list.add(insight("Training revenue exceeded costs by 40% - training division becoming profitable",
                "training", "medium", "Develop new certification programs to further monetize training assets", 0.79));
        list.add(insight("Weekend batch attendance 20% higher than weekday - adjust batch scheduling",
                "training", "low", "Shift more batches to weekend slots and evaluate demand for evening batches", 0.75));

        list.add(insight("Oyster mushroom yield 15% above average - document best practices",
                "cultivation", "medium", "Document and disseminate oyster mushroom best practices across all facilities", 0.93));
        list.add(insight("Contamination rate above threshold in South facility - investigate sterilization",
                "cultivation", "critical", "Conduct sterilization audit at South facility and implement corrective actions", 0.89));
        list.add(insight("Cycle time for Shiitake reduced by 5 days - new substrate formula effective",
                "cultivation", "high", "Standardize new substrate formula across all Shiitake batches", 0.87));
        list.add(insight("Grower satisfaction score improved 12% after new support program launch",
                "cultivation", "medium", "Expand grower support program to include monthly site visits", 0.84));
        list.add(insight("Disease incidence rate spiked in West region - potential environmental factor",
                "cultivation", "critical", "Deploy disease prevention task force to West region farms immediately", 0.91));

        list.add(insight("Customers who complete training have 3x higher lifetime value - training drives retention",
                "cross-domain", "high", "Integrate training recommendations into customer onboarding flow", 0.95));
        list.add(insight("Growers using premium spawn have 22% higher yield - correlate purchases with cultivation success",
                "cross-domain", "medium", "Create bundled offering of premium spawn with cultivation consultation", 0.88));
        list.add(insight("Revenue from trained customers grew 35% faster than non-trained - education drives upsell",
                "cross-domain", "high", "Build automated training-to-upsell pipeline with targeted product recommendations", 0.90));
        list.add(insight("Regions with highest support ticket volume have lowest renewal rates - proactive outreach needed",
                "cross-domain", "critical", "Implement proactive outreach for regions with high support volume", 0.86));

        return list;
    }

    private BusinessInsight insight(String title, String category, String severity, String recommendation, double confidence) {
        return new BusinessInsight(
                UUID.randomUUID().toString(),
                title,
                "System-generated insight based on cross-domain data analysis",
                category,
                severity,
                recommendation,
                confidence,
                Map.of("generatedBy", "BusinessInsightsEngine"),
                List.of(),
                "critical".equals(severity) || "high".equals(severity),
                OffsetDateTime.now(),
                OffsetDateTime.now().plusDays(30)
        );
    }

    public List<BusinessInsight> generateInsights(String category, String period) {
        log.debug("Generating insights for category={}, period={}", category, period);
        return insightTemplates.stream()
                .filter(i -> i.category().equalsIgnoreCase(category))
                .map(i -> new BusinessInsight(
                        i.insightId(), i.title(), i.description(), i.category(), i.severity(),
                        i.recommendation(), i.confidenceScore(),
                        Map.of("category", category, "period", period, "generatedAt", OffsetDateTime.now().toString(),
                               "generatedBy", "BusinessInsightsEngine"),
                        i.relatedMetrics(), i.actionable(), OffsetDateTime.now(), i.expiresAt()))
                .collect(Collectors.toList());
    }

    public List<BusinessInsight> generateRevenueInsights(RevenueMetrics metrics) {
        log.debug("Generating revenue insights from metrics: {}", metrics.metricId());
        List<BusinessInsight> insights = new ArrayList<>();
        if (metrics.growthRate() > 10) {
            insights.add(new BusinessInsight(UUID.randomUUID().toString(),
                    "Revenue growing at " + String.format("%.1f", metrics.growthRate()) + "% - strong momentum",
                    "Revenue is growing significantly above target.", "revenue", "high",
                    "Maintain current strategy and explore adjacent market opportunities",
                    0.90, Map.of("growthRate", metrics.growthRate(), "totalRevenue", metrics.totalRevenue()),
                    List.of("revenue_growth", "total_revenue"), true, OffsetDateTime.now(), OffsetDateTime.now().plusDays(30)));
        }
        if (metrics.growthRate() < 0) {
            insights.add(new BusinessInsight(UUID.randomUUID().toString(),
                    "Revenue declining by " + String.format("%.1f", Math.abs(metrics.growthRate())) + "% - investigate causes",
                    "Negative revenue growth detected.", "revenue", "critical",
                    "Conduct root cause analysis and develop recovery plan",
                    0.88, Map.of("growthRate", metrics.growthRate(), "previousPeriodRevenue", metrics.previousPeriodRevenue()),
                    List.of("revenue_decline", "revenue_growth"), true, OffsetDateTime.now(), OffsetDateTime.now().plusDays(14)));
        }
        metrics.revenueByRegion().forEach((region, revenue) -> {
            if (revenue > metrics.totalRevenue() * 0.4) {
                insights.add(new BusinessInsight(UUID.randomUUID().toString(),
                        region + " region contributes " + String.format("%.0f", (revenue / metrics.totalRevenue()) * 100) + "% of revenue - high concentration risk",
                        "Revenue is heavily concentrated in a single region.", "revenue", "high",
                        "Develop regional diversification strategy to mitigate concentration risk",
                        0.85, Map.of("region", region, "revenue", revenue, "percentage", (revenue / metrics.totalRevenue()) * 100),
                        List.of("regional_concentration", "diversification"), true, OffsetDateTime.now(), OffsetDateTime.now().plusDays(30)));
            }
        });
        return insights;
    }

    public List<BusinessInsight> generateCustomerInsights(CustomerAnalytics analytics) {
        log.debug("Generating customer insights from analytics: {}", analytics.analyticsId());
        List<BusinessInsight> insights = new ArrayList<>();
        if (analytics.churnRate() > 10) {
            insights.add(new BusinessInsight(UUID.randomUUID().toString(),
                    "Customer churn rate at " + String.format("%.1f", analytics.churnRate()) + "% - above threshold",
                    "Churn rate exceeds acceptable threshold of 10%.", "customer", "critical",
                    "Launch customer retention campaign and analyze churn drivers",
                    0.91, Map.of("churnRate", analytics.churnRate(), "churnedCustomers", analytics.churnedCustomers()),
                    List.of("churn_rate", "customer_retention"), true, OffsetDateTime.now(), OffsetDateTime.now().plusDays(14)));
        }
        if (analytics.retentionRate() > 85) {
            insights.add(new BusinessInsight(UUID.randomUUID().toString(),
                    "Customer retention rate at " + String.format("%.1f", analytics.retentionRate()) + "% - excellent",
                    "Retention rate is well above industry average.", "customer", "medium",
                    "Document and replicate retention strategies across all customer segments",
                    0.89, Map.of("retentionRate", analytics.retentionRate()),
                    List.of("retention_rate"), false, OffsetDateTime.now(), OffsetDateTime.now().plusDays(30)));
        }
        if (analytics.averageSatisfactionScore() < 3.5) {
            insights.add(new BusinessInsight(UUID.randomUUID().toString(),
                    "Customer satisfaction score at " + String.format("%.1f", analytics.averageSatisfactionScore()) + "/5 - needs improvement",
                    "Satisfaction score is below the 3.5 threshold.", "customer", "high",
                    "Deploy satisfaction survey to identify pain points and implement fixes",
                    0.84, Map.of("satisfactionScore", analytics.averageSatisfactionScore()),
                    List.of("satisfaction", "csat"), true, OffsetDateTime.now(), OffsetDateTime.now().plusDays(30)));
        }
        return insights;
    }

    public List<BusinessInsight> generateTrainingInsights(TrainingAnalytics analytics) {
        log.debug("Generating training insights from analytics: {}", analytics.analyticsId());
        List<BusinessInsight> insights = new ArrayList<>();
        if (analytics.completionRate() < 70) {
            insights.add(new BusinessInsight(UUID.randomUUID().toString(),
                    "Training completion rate at " + String.format("%.1f", analytics.completionRate()) + "% - below target",
                    "Completion rate is below the 70% target.", "training", "high",
                    "Review course difficulty and introduce progress incentives",
                    0.87, Map.of("completionRate", analytics.completionRate(), "totalStudents", analytics.totalStudents()),
                    List.of("completion_rate", "training_effectiveness"), true, OffsetDateTime.now(), OffsetDateTime.now().plusDays(30)));
        }
        if (analytics.averageScore() > 85) {
            insights.add(new BusinessInsight(UUID.randomUUID().toString(),
                    "Average training score " + String.format("%.1f", analytics.averageScore()) + "% - students excelling",
                    "Students are performing well above average.", "training", "low",
                    "Consider introducing advanced certification levels",
                    0.82, Map.of("averageScore", analytics.averageScore()),
                    List.of("average_score"), false, OffsetDateTime.now(), OffsetDateTime.now().plusDays(30)));
        }
        if (analytics.trainingProfitMargin() > 30) {
            insights.add(new BusinessInsight(UUID.randomUUID().toString(),
                    "Training profit margin at " + String.format("%.1f", analytics.trainingProfitMargin()) + "% - highly profitable",
                    "Training division is highly profitable.", "training", "medium",
                    "Explore scaling training programs to new markets and audiences",
                    0.86, Map.of("profitMargin", analytics.trainingProfitMargin(), "revenue", analytics.revenueFromTraining()),
                    List.of("profitability", "training_revenue"), true, OffsetDateTime.now(), OffsetDateTime.now().plusDays(30)));
        }
        return insights;
    }

    public List<BusinessInsight> generateCultivationInsights(CultivationAnalytics analytics) {
        log.debug("Generating cultivation insights from analytics: {}", analytics.analyticsId());
        List<BusinessInsight> insights = new ArrayList<>();
        if (analytics.contaminationRate() > 5) {
            insights.add(new BusinessInsight(UUID.randomUUID().toString(),
                    "Contamination rate at " + String.format("%.1f", analytics.contaminationRate()) + "% - above threshold",
                    "Contamination exceeds the 5% threshold.", "cultivation", "critical",
                    "Conduct sterilization audit and review facility protocols",
                    0.93, Map.of("contaminationRate", analytics.contaminationRate()),
                    List.of("contamination", "quality_control"), true, OffsetDateTime.now(), OffsetDateTime.now().plusDays(7)));
        }
        analytics.yieldBySpecies().forEach((species, yield) -> {
            if (yield > analytics.averageYieldPerBatch() * 1.2) {
                insights.add(new BusinessInsight(UUID.randomUUID().toString(),
                        species + " yield " + String.format("%.0f", yield) + " kg - " + String.format("%.0f", ((yield / analytics.averageYieldPerBatch()) - 1) * 100) + "% above average",
                        species + " is outperforming other species significantly.", "cultivation", "medium",
                        "Document best practices for " + species + " and replicate across facilities",
                        0.91, Map.of("species", species, "yield", yield, "averageYield", analytics.averageYieldPerBatch()),
                        List.of("yield_optimization", species.toLowerCase() + "_yield"), true, OffsetDateTime.now(), OffsetDateTime.now().plusDays(30)));
            }
        });
        if (analytics.growerSatisfactionScore() < 3.5) {
            insights.add(new BusinessInsight(UUID.randomUUID().toString(),
                    "Grower satisfaction at " + String.format("%.1f", analytics.growerSatisfactionScore()) + "/5 - needs attention",
                    "Grower satisfaction is below the acceptable threshold.", "cultivation", "high",
                    "Survey growers to identify key pain points and address top issues",
                    0.85, Map.of("satisfactionScore", analytics.growerSatisfactionScore()),
                    List.of("grower_satisfaction"), true, OffsetDateTime.now(), OffsetDateTime.now().plusDays(30)));
        }
        return insights;
    }

    public List<BusinessInsight> generateCrossDomainInsights() {
        log.debug("Generating cross-domain insights");
        return insightTemplates.stream()
                .filter(i -> "cross-domain".equals(i.category()))
                .collect(Collectors.toList());
    }

    public List<BusinessInsight> getTopInsights(int limit) {
        return insightTemplates.stream()
                .sorted(Comparator.comparingDouble(BusinessInsight::confidenceScore).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }

    public List<BusinessInsight> getActionableInsights() {
        return insightTemplates.stream()
                .filter(BusinessInsight::actionable)
                .sorted(Comparator.comparingDouble(BusinessInsight::confidenceScore).reversed())
                .collect(Collectors.toList());
    }

    public Map<String, Object> generateDailyBriefing() {
        List<BusinessInsight> topInsights = getTopInsights(5);
        String priority = topInsights.stream()
                .anyMatch(i -> "critical".equals(i.severity())) ? "critical" :
                topInsights.stream().anyMatch(i -> "high".equals(i.severity())) ? "high" : "normal";
        List<String> recommendedActions = topInsights.stream()
                .map(BusinessInsight::recommendation)
                .collect(Collectors.toList());
        return Map.of(
                "briefingDate", OffsetDateTime.now().toString(),
                "totalInsights", insightTemplates.size(),
                "topInsights", topInsights,
                "priority", priority,
                "recommendedActions", recommendedActions,
                "generatedBy", "BusinessInsightsEngine"
        );
    }
}
