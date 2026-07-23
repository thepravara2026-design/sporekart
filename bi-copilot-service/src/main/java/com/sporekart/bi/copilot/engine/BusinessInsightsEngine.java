package com.sporekart.bi.copilot.engine;

import com.sporekart.bi.copilot.domain.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class BusinessInsightsEngine {

    private static final Logger log = LoggerFactory.getLogger(BusinessInsightsEngine.class);

    private final List<BusinessInsight> insightRules = seedInsightRules();

    public List<BusinessInsight> generateInsights(ExecutiveSummary summary) {
        log.info("Generating insights from executive summary");
        List<BusinessInsight> all = new ArrayList<>();
        all.addAll(generateRevenueInsights(summary.revenue()));
        all.addAll(generateCustomerInsights(summary.customers()));
        all.addAll(generateProductInsights(summary.products()));
        all.addAll(generateInventoryInsights(summary.inventory()));
        all.addAll(generateTrainingInsights(summary.training()));
        all.addAll(generateCrossDomainInsights(summary));
        return all;
    }

    public List<BusinessInsight> generateRevenueInsights(RevenueAnalytics revenue) {
        List<BusinessInsight> insights = new ArrayList<>();

        if (revenue.revenueGrowth() > 10) {
            insights.add(insight("REV-INS-001", "Strong revenue growth momentum",
                    "Revenue grew " + revenue.revenueGrowth() + "%%, driven primarily by spawn and equipment categories.",
                    "revenue", "POSITIVE", 0.88,
                    "Spawn category contributes " + formatPct(revenue.byCategory().getOrDefault("Spawn", 0.0), revenue.grossRevenue())
                            + " of total revenue. Continued investment in production capacity is recommended.",
                    List.of("Increase spawn production capacity", "Expand equipment product line", "Explore new regional markets")));
        } else if (revenue.revenueGrowth() < 0) {
            insights.add(insight("REV-INS-002", "Revenue decline requires attention",
                    "Revenue declined " + Math.abs(revenue.revenueGrowth()) + "%% compared to previous period.",
                    "revenue", "NEGATIVE", 0.82,
                    "Previous period revenue was " + formatCurrency(revenue.previousPeriodRevenue()) +
                            ". Root cause analysis recommended to identify contributing factors.",
                    List.of("Conduct revenue decline root cause analysis", "Review pricing strategy", "Launch promotional campaigns")));
        }

        if (revenue.averageOrderValue() > 4000) {
            insights.add(insight("REV-INS-003", "Healthy average order value",
                    "Average order value of " + formatCurrency(revenue.averageOrderValue()) + " indicates strong customer spending.",
                    "revenue", "POSITIVE", 0.75,
                    "AOV above 4000 suggests customers are purchasing premium products and bundles.",
                    List.of("Test further upsell opportunities", "Create premium bundles to increase AOV")));
        }

        double refundPct = revenue.grossRevenue() > 0 ? (revenue.refundAmount() / revenue.grossRevenue()) * 100 : 0;
        if (refundPct > 2) {
            insights.add(insight("REV-INS-004", "Elevated refund rate",
                    "Refund rate of " + String.format("%.1f", refundPct) + "%% exceeds 2%% benchmark.",
                    "revenue", "NEGATIVE", 0.78,
                    "Refunds totaling " + formatCurrency(revenue.refundAmount()) + " impact net revenue.",
                    List.of("Investigate refund patterns by product", "Improve product quality and descriptions", "Review refund policy")));
        }

        return insights;
    }

    public List<BusinessInsight> generateCustomerInsights(CustomerAnalytics customers) {
        List<BusinessInsight> insights = new ArrayList<>();

        if (customers.retentionRate() > 0.7) {
            insights.add(insight("CUS-INS-001", "Strong customer retention",
                    "Customer retention rate of " + String.format("%.0f", customers.retentionRate() * 100) + "%% indicates healthy customer relationships.",
                    "customer", "POSITIVE", 0.85,
                    "Repeat purchase rate of " + String.format("%.0f", customers.repeatPurchaseRate() * 100) + "%% shows effective engagement.",
                    List.of("Maintain retention programs", "Develop loyalty rewards for top customers")));
        } else {
            insights.add(insight("CUS-INS-002", "Retention rate below target",
                    "Customer retention of " + String.format("%.0f", customers.retentionRate() * 100) + "%% needs improvement.",
                    "customer", "NEGATIVE", 0.80,
                    "Each 5%% improvement in retention can increase profitability by 25-95%%.",
                    List.of("Implement customer feedback program", "Develop targeted re-engagement campaigns")));
        }

        if (customers.customerLifetimeValue() > 8000) {
            insights.add(insight("CUS-INS-003", "High customer lifetime value",
                    "CLV of " + formatCurrency(customers.customerLifetimeValue()) + " indicates premium customer base.",
                    "customer", "POSITIVE", 0.82,
                    "High CLV customers should be prioritized for retention and upsell programs.",
                    List.of("Create VIP program for high CLV customers", "Develop referral incentives")));
        }

        double newCustomerPct = customers.totalCustomers() > 0
                ? (double) customers.newCustomers() / customers.totalCustomers() * 100 : 0;
        if (newCustomerPct > 5) {
            insights.add(insight("CUS-INS-004", "Healthy new customer acquisition",
                    "New customers represent " + String.format("%.1f", newCustomerPct) + "%% of total base.",
                    "customer", "POSITIVE", 0.76,
                    customers.newCustomers() + " new customers acquired this period indicates effective marketing.",
                    List.of("Optimize acquisition channels", "Develop onboarding sequence for new customers")));
        }

        double churnPct = customers.churnRate() * 100;
        if (churnPct > 10) {
            insights.add(insight("CUS-INS-005", "Churn rate exceeds healthy threshold",
                    "Churn rate of " + String.format("%.1f", churnPct) + "%% is above 10%% threshold.",
                    "customer", "NEGATIVE", 0.84,
                    customers.churnedCustomers() + " customers lost. Inactive base: " + customers.inactiveCustomers(),
                    List.of("Analyze churn reasons through exit surveys", "Launch win-back campaign", "Improve onboarding experience")));
        }

        return insights;
    }

    public List<BusinessInsight> generateProductInsights(ProductAnalytics products) {
        List<BusinessInsight> insights = new ArrayList<>();

        if (!products.topProducts().isEmpty()) {
            var top = products.topProducts().getFirst();
            insights.add(insight("PRD-INS-001", "Top product: " + top.name(),
                    top.name() + " generated " + formatCurrency(top.revenue()) + " with " + top.unitsSold() + " units sold.",
                    "product", "POSITIVE", 0.87,
                    "Growth rate of " + top.growth() + "%% and profit margin of " + String.format("%.0f", top.profitMargin() * 100) + "%%.",
                    List.of("Ensure adequate stock levels for top products", "Consider product line expansion", "Feature in marketing campaigns")));
        }

        if (!products.worstProducts().isEmpty()) {
            var worst = products.worstProducts().getFirst();
            insights.add(insight("PRD-INS-002", "Underperforming product: " + worst.name(),
                    worst.name() + " declined " + Math.abs(worst.growth()) + "%% with low margin.",
                    "product", "NEGATIVE", 0.72,
                    "Review pricing, positioning, or consider discontinuing this product.",
                    List.of("Evaluate product-market fit", "Consider bundling or discounting", "Assess discontinuation")));
        }

        if (!products.searchTrends().isEmpty()) {
            insights.add(insight("PRD-INS-003", "Emerging search trends identified",
                    "Top search trends: " + String.join(", ", products.searchTrends()),
                    "product", "POSITIVE", 0.70,
                    "Search trends indicate growing customer interest in specific products and categories.",
                    List.of("Increase inventory for trending products", "Create content around trending topics")));
        }

        if (products.conversionRate() > 0.5) {
            insights.add(insight("PRD-INS-004", "Strong conversion rate",
                    "Conversion rate of " + String.format("%.0f", products.conversionRate() * 100) + "%% exceeds industry average.",
                    "product", "POSITIVE", 0.78,
                    "High conversion indicates effective product pages and pricing.",
                    List.of("A/B test checkout improvements", "Analyze conversion funnel for further optimization")));
        }

        return insights;
    }

    public List<BusinessInsight> generateInventoryInsights(InventoryAnalytics inventory) {
        List<BusinessInsight> insights = new ArrayList<>();

        if (inventory.lowStockItems() > 0) {
            insights.add(insight("INV-INS-001", "Low stock items require attention",
                    inventory.lowStockItems() + " items are below reorder level and need immediate replenishment.",
                    "inventory", "NEGATIVE", 0.85,
                    "Risk of stockouts affecting " + inventory.lowStockItems() + " SKUs.",
                    List.of("Place replenishment orders immediately", "Review reorder points", "Set up automated reorder triggers")));
        }

        if (inventory.deadStockItems() > 20) {
            insights.add(insight("INV-INS-002", "Excess dead stock accumulating",
                    inventory.deadStockItems() + " items identified as dead stock, tying up working capital.",
                    "inventory", "NEGATIVE", 0.76,
                    "Dead stock increases holding costs and reduces warehouse efficiency.",
                    List.of("Run clearance sale on dead stock", "Donate for tax benefits", "Review purchasing patterns")));
        }

        if (inventory.turnoverRate() > 5) {
            insights.add(insight("INV-INS-003", "Healthy inventory turnover",
                    "Inventory turnover rate of " + String.format("%.1f", inventory.turnoverRate()) + "x indicates efficient stock management.",
                    "inventory", "POSITIVE", 0.80,
                    "Fast-moving inventory with strong demand signals.",
                    List.of("Maintain current inventory practices", "Monitor turnover trends for changes")));
        }

        if (!inventory.fastMoving().isEmpty()) {
            var fast = inventory.fastMoving().getFirst();
            insights.add(insight("INV-INS-004", "Fast mover: " + fast.name(),
                    fast.name() + " sold " + fast.soldLastMonth() + " units last month with turnover of "
                            + String.format("%.1f", fast.turnoverDays()) + " days.",
                    "inventory", "POSITIVE", 0.83,
                    "High demand product requiring consistent stock levels.",
                    List.of("Increase safety stock for fast movers", "Negotiate bulk supplier discounts")));
        }

        return insights;
    }

    public List<BusinessInsight> generateTrainingInsights(TrainingAnalytics training) {
        List<BusinessInsight> insights = new ArrayList<>();

        if (training.completionRate() > 0.8) {
            insights.add(insight("TRN-INS-001", "High course completion rate",
                    "Training completion rate of " + String.format("%.0f", training.completionRate() * 100) + "%% indicates effective programs.",
                    "training", "POSITIVE", 0.84,
                    training.certificationsIssued() + " certifications issued this period.",
                    List.of("Develop advanced course offerings", "Create certification pathways")));
        }

        if (training.averageScore() > 75) {
            insights.add(insight("TRN-INS-002", "Strong student performance",
                    "Average student score of " + String.format("%.1f", training.averageScore()) + "%% reflects quality instruction.",
                    "training", "POSITIVE", 0.79,
                    "Students are mastering course material effectively.",
                    List.of("Maintain curriculum quality", "Develop specialized advanced modules")));
        }

        if (training.totalStudents() > 200) {
            insights.add(insight("TRN-INS-003", "Growing training demand",
                    training.totalStudents() + " students enrolled across " + training.activeBatches() + " active batches.",
                    "training", "POSITIVE", 0.81,
                    "Training revenue contributes significantly to overall business.",
                    List.of("Expand training capacity", "Recruit additional trainers", "Develop online learning options")));
        }

        if (!training.trainerPerformance().isEmpty()) {
            var topTrainer = training.trainerPerformance().stream()
                    .max(Comparator.comparingDouble(TrainingAnalytics.TrainerPerformance::avgScore))
                    .orElse(null);
            if (topTrainer != null) {
                insights.add(insight("TRN-INS-004", "Top performer: " + topTrainer.name(),
                        topTrainer.name() + " achieves " + String.format("%.1f", topTrainer.avgScore())
                                + "%% avg score with " + topTrainer.students() + " students.",
                        "training", "POSITIVE", 0.86,
                        "High-performing trainer can mentor others and develop curriculum.",
                        List.of("Have top trainer develop training materials", "Implement trainer mentoring program")));
            }
        }

        return insights;
    }

    public List<BusinessInsight> generateCrossDomainInsights(ExecutiveSummary summary) {
        List<BusinessInsight> insights = new ArrayList<>();

        double healthScore = summary.healthScore().overall();
        if (healthScore >= 80) {
            insights.add(insight("XDM-INS-001", "Company health score is excellent",
                    "Overall health score of " + String.format("%.1f", healthScore) + " indicates strong business performance.",
                    "cross-domain", "POSITIVE", 0.90,
                    "All major dimensions are performing well. Focus on maintaining momentum and exploring growth opportunities.",
                    List.of("Maintain current strategies", "Explore expansion opportunities", "Invest in innovation")));
        } else if (healthScore < 50) {
            insights.add(insight("XDM-INS-002", "Company health score needs improvement",
                    "Overall health score of " + String.format("%.1f", healthScore) + " indicates critical areas need attention.",
                    "cross-domain", "NEGATIVE", 0.88,
                    "Multiple dimensions scoring below 50 require coordinated turnaround efforts.",
                    List.of("Conduct emergency strategy review", "Prioritize low-scoring dimensions", "Develop 90-day improvement plan")));
        }

        if (summary.revenue().revenueGrowth() > 10 && summary.customers().churnRate() < 0.1) {
            insights.add(insight("XDM-INS-003", "Revenue growth with low churn — ideal combination",
                    "Revenue growing " + summary.revenue().revenueGrowth() + "%% with churn at "
                            + String.format("%.1f", summary.customers().churnRate() * 100) + "%%.",
                    "cross-domain", "POSITIVE", 0.89,
                    "This combination indicates sustainable, quality growth.",
                    List.of("Double down on successful strategies", "Invest in scalable infrastructure")));
        }

        if (summary.training().completionRate() > 0.8 && summary.revenue().byTraining().values().stream()
                .mapToDouble(Double::doubleValue).sum() > 200000) {
            insights.add(insight("XDM-INS-004", "Training programs driving revenue growth",
                    "Training revenue of " + formatCurrency(summary.revenue().byTraining().values().stream()
                            .mapToDouble(Double::doubleValue).sum()) + " with high completion rates.",
                    "cross-domain", "POSITIVE", 0.83,
                    "Training programs are both effective and profitable.",
                    List.of("Expand training course catalog", "Develop corporate training programs")));
        }

        if (summary.inventory().lowStockItems() > 5 && summary.revenue().revenueGrowth() > 5) {
            insights.add(insight("XDM-INS-005", "Growth straining inventory — risk of stockouts",
                    "Revenue growth of " + summary.revenue().revenueGrowth() + "%% with " +
                            summary.inventory().lowStockItems() + " items below reorder level.",
                    "cross-domain", "NEGATIVE", 0.77,
                    "Growing demand may outpace inventory replenishment capabilities.",
                    List.of("Accelerate inventory procurement", "Implement demand-driven replenishment", "Review supplier lead times")));
        }

        return insights;
    }

    public List<BusinessInsight> getCriticalInsights() {
        return insightRules.stream()
                .filter(i -> "CRITICAL".equals(i.severity()) || "NEGATIVE".equals(i.severity()))
                .sorted(Comparator.comparingDouble(BusinessInsight::confidenceScore).reversed())
                .limit(10)
                .collect(Collectors.toList());
    }

    public List<BusinessInsight> generateDailyBriefing() {
        log.info("Generating daily briefing");
        List<BusinessInsight> all = insightRules;

        List<BusinessInsight> critical = all.stream()
                .filter(i -> "CRITICAL".equals(i.severity()) || "NEGATIVE".equals(i.severity()))
                .sorted(Comparator.comparingDouble(BusinessInsight::confidenceScore).reversed())
                .limit(5)
                .toList();

        List<BusinessInsight> important = all.stream()
                .filter(i -> !"CRITICAL".equals(i.severity()) && !"NEGATIVE".equals(i.severity()))
                .sorted(Comparator.comparingDouble(BusinessInsight::confidenceScore).reversed())
                .limit(5)
                .toList();

        return Stream.concat(critical.stream(), important.stream()).toList();
    }

    private List<BusinessInsight> seedInsightRules() {
        List<BusinessInsight> rules = new ArrayList<>();

        rules.add(insight("RULE-001", "Revenue growth above 10%% is a leading indicator of market expansion",
                "Track revenue growth rate against production capacity to identify scaling needs.",
                "revenue", "INFO", 0.80, "Businesses growing >10%% quarterly typically need to expand capacity within 2 quarters.",
                List.of("Monitor capacity vs demand ratio", "Plan capacity expansion 2 quarters ahead")));

        rules.add(insight("RULE-002", "Churn rate above 10%% signals structural issues",
                "High churn often correlates with product quality, pricing, or customer service issues.",
                "customer", "CRITICAL", 0.85, "Churn reduction of 5%% can increase profits by 25-95%% according to Harvard Business Review.",
                List.of("Analyze churn by segment and tenure", "Implement systematic exit interviews")));

        rules.add(insight("RULE-003", "AOV growth suggests successful upsell and cross-sell",
                "Monitor AOV trends to validate bundling and premium product strategies.",
                "revenue", "INFO", 0.70, "AOV growth of 10%%+ indicates effective product recommendation and bundling.",
                List.of("Continue bundling strategies", "Personalize product recommendations")));

        rules.add(insight("RULE-004", "Low stock items directly impact revenue",
                "Each stockout incident costs an average of 6.2K in lost sales.",
                "inventory", "HIGH", 0.83, "Inventory availability directly correlates with customer satisfaction and repeat purchases.",
                List.of("Set up automated reorder alerts", "Maintain buffer stock for top 20 SKUs")));

        rules.add(insight("RULE-005", "Training completion rate reflects program effectiveness",
                "Completion rates below 70%% typically indicate curriculum or delivery issues.",
                "training", "INFO", 0.75, "High completion rates correlate with higher NPS and referral rates.",
                List.of("Review curriculum for less than 70%% completion courses", "Collect student feedback")));

        rules.add(insight("RULE-006", "New customer acquisition cost should decrease with scale",
                "As brand awareness grows, CAC should decrease — monitor channel efficiency.",
                "customer", "INFO", 0.72, "Mature businesses typically see 20-30%% YoY decrease in CAC.",
                List.of("Track CAC by channel monthly", "Shift budget to highest ROI channels")));

        rules.add(insight("RULE-007", "Inventory turnover below 4x indicates working capital inefficiency",
                "Slow inventory turnover ties up cash and increases holding costs.",
                "inventory", "HIGH", 0.78, "Target turnover rate of 6-8x for perishable agricultural products.",
                List.of("Review slow-moving SKUs monthly", "Implement just-in-time for slow movers")));

        rules.add(insight("RULE-008", "Seasonal demand patterns require proactive inventory planning",
                "Monsoon season typically drives 40%% surge in substrate demand.",
                "inventory", "INFO", 0.80, "Proactive seasonal planning can prevent 95%% of stockout incidents.",
                List.of("Build seasonal demand forecasts", "Pre-order seasonal inventory 6-8 weeks ahead")));

        rules.add(insight("RULE-009", "Customer segment revenue concentration indicates risk",
                "If any single segment exceeds 50%% of revenue, diversify to reduce risk.",
                "customer", "HIGH", 0.74, "Over-reliance on one segment creates vulnerability to market shifts.",
                List.of("Track segment concentration quarterly", "Develop targeted campaigns for smaller segments")));

        rules.add(insight("RULE-010", "Product margin analysis should drive portfolio decisions",
                "Products with margins below 30%% should be reviewed for pricing or discontinuation.",
                "product", "INFO", 0.71, "Portfolio optimization can improve overall margins by 5-8%%.",
                List.of("Review product margins quarterly", "Phase out consistently low-margin products")));

        rules.add(insight("RULE-011", "Trainer performance directly impacts student outcomes",
                "Top-performing trainers achieve 15-20%% higher student scores than average.",
                "training", "INFO", 0.77, "Investing in trainer development improves overall program quality.",
                List.of("Implement trainer development program", "Share best practices across trainer team")));

        rules.add(insight("RULE-012", "Geographic revenue imbalance indicates market opportunity",
                "Regions below 20%% revenue contribution with 25%%+ customer base need attention.",
                "revenue", "INFO", 0.69, "Expanding underperforming regions can increase total revenue by 10-15%%.",
                List.of("Analyze regional market potential", "Develop region-specific marketing strategy")));

        rules.add(insight("RULE-013", "Customer lifetime value trends reflect brand health",
                "Rising CLV indicates strong brand loyalty and product-market fit.",
                "customer", "POSITIVE", 0.82, "CLV growth of 10%%+ YoY is a strong indicator of business health.",
                List.of("Track CLV by cohort monthly", "Focus retention efforts on high-CLV segments")));

        rules.add(insight("RULE-014", "Product search trends predict future demand",
                "Search trend growth of 50%%+ typically translates to 20-30%% demand increase within 60 days.",
                "product", "INFO", 0.73, "Leading indicator for inventory and production planning.",
                List.of("Review search trends weekly", "Align inventory with trending products")));

        rules.add(insight("RULE-015", "Refund rate above 3%% impacts profitability and trust",
                "High refund rates correlate with negative reviews and reduced repeat purchases.",
                "revenue", "HIGH", 0.79, "Each 1%% reduction in refund rate improves net margin by approximately 0.5%%.",
                List.of("Implement root cause analysis for refunds", "Improve product quality and descriptions")));

        rules.add(insight("RULE-016", "Dead stock ratio above 5%% indicates purchasing inefficiency",
                "Excess dead stock ties up capital that could be used for growth initiatives.",
                "inventory", "HIGH", 0.76, "Target dead stock ratio below 3%% through improved demand forecasting.",
                List.of("Review purchasing patterns quarterly", "Implement demand-driven purchasing")));

        rules.add(insight("RULE-017", "Cross-sell between products and training increases customer value",
                "Customers who buy both products and training have 40%% higher CLV.",
                "cross-domain", "POSITIVE", 0.84, "Training-to-product cross-sell is a high-impact growth lever.",
                List.of("Bundle training with starter kits", "Cross-promote training on product pages")));

        rules.add(insight("RULE-018", "Multi-product customers have significantly lower churn",
                "Customers with 3+ products have 60%% lower churn than single-product customers.",
                "cross-domain", "POSITIVE", 0.81, "Expanding product adoption is a key retention strategy.",
                List.of("Track product adoption metrics", "Create multi-product bundle offers")));

        rules.add(insight("RULE-019", "Seasonal training demand peaks require capacity planning",
                "Training enrollment increases 40%% during Q1 and Q3. Plan capacity accordingly.",
                "training", "INFO", 0.72, "Advanced capacity planning ensures no revenue loss due to capacity constraints.",
                List.of("Pre-schedule additional batches for peak seasons", "Hire seasonal trainers")));

        rules.add(insight("RULE-020", "Customer feedback response time impacts satisfaction",
                "Responses within 24 hours achieve 90%% customer satisfaction vs 60%% for 48+ hours.",
                "customer", "INFO", 0.68, "Fast response times are a competitive differentiator.",
                List.of("Implement automated response system", "Target < 4 hour response time")));

        rules.add(insight("RULE-021", "Regional expansion ROI is highest in adjacent markets",
                "Expanding to regions within 200km of existing distribution achieves 2x ROI vs distant markets.",
                "revenue", "INFO", 0.70, "Logistics efficiency drives profitability in regional expansion.",
                List.of("Prioritize adjacent markets for expansion", "Leverage existing distribution network")));

        rules.add(insight("RULE-022", "Subscription models improve revenue predictability",
                "Businesses with subscription models achieve 30%% higher valuation multiples.",
                "revenue", "POSITIVE", 0.78, "Recurring revenue reduces volatility and improves planning.",
                List.of("Develop subscription offerings for spawn/consumables", "Target 20%% revenue from subscriptions")));

        rules.add(insight("RULE-023", "B2B segment has highest growth potential",
                "B2B customers have 3x CLV and 50%% lower churn than B2C segments.",
                "customer", "POSITIVE", 0.79, "Commercial farms and distributors represent the highest-value segment.",
                List.of("Develop dedicated B2B sales team", "Create B2B-specific product bundles")));

        rules.add(insight("RULE-024", "Training certification programs create recurring revenue",
                "Certified customers have 80%% repeat certification rate and higher product spend.",
                "training", "POSITIVE", 0.76, "Certification creates customer lock-in and ongoing revenue streams.",
                List.of("Develop multi-level certification track", "Create recertification requirements")));

        rules.add(insight("RULE-025", "Early detection of negative trends enables proactive response",
                "Businesses that detect negative trends 30 days early reduce impact by 50%%.",
                "cross-domain", "INFO", 0.82, "Continuous monitoring with automated alerts is essential for agile response.",
                List.of("Set up automated trend monitoring", "Create threshold-based alerting system")));

        rules.add(insight("RULE-026", "Customer onboarding experience determines long-term retention",
                "Effective onboarding within first 7 days increases 90-day retention by 40%%.",
                "customer", "INFO", 0.74, "First-time buyer experience is critical for conversion to repeat customer.",
                List.of("Design structured 7-day onboarding sequence", "Provide educational content in onboarding")));

        rules.add(insight("RULE-027", "Employee training quality directly impacts customer experience",
                "Well-trained staff deliver 20%% higher customer satisfaction scores.",
                "training", "POSITIVE", 0.71, "Internal training investments compound through better customer interactions.",
                List.of("Develop internal training programs", "Measure training ROI through customer metrics")));

        rules.add(insight("RULE-028", "Dynamic pricing can improve margin during demand spikes",
                "Elasticity analysis shows 15%% price increase during peak demand reduces volume by only 5%%.",
                "revenue", "INFO", 0.66, "Strategic pricing during high-demand periods can improve margins significantly.",
                List.of("Implement seasonal pricing adjustments", "Test price elasticity for top products")));

        rules.add(insight("RULE-029", "Inventory accuracy directly impacts fulfillment reliability",
                "Inventory accuracy below 95%% leads to 15%%+ error rate in order fulfillment.",
                "inventory", "HIGH", 0.77, "Regular cycle counting and system accuracy are critical for operations.",
                List.of("Implement weekly cycle counting", "Conduct full inventory audit monthly")));

        rules.add(insight("RULE-030", "Customer referral programs have highest ROI acquisition channel",
                "Referral customers have 30%% higher CLV and 50%% lower churn than other channels.",
                "customer", "POSITIVE", 0.80, "Referral programs are the most cost-effective growth channel.",
                List.of("Launch structured referral program", "Offer dual-sided incentives")));

        rules.add(insight("RULE-031", "Product return rate above 5%% requires immediate investigation",
                "High return rates correlate with customer dissatisfaction and negative word-of-mouth.",
                "product", "HIGH", 0.75, "Returns above 5%% typically indicate systemic product or expectation issues.",
                List.of("Analyze return reasons by product", "Review product descriptions for accuracy")));

        rules.add(insight("RULE-032", "Cross-regional price consistency improves brand trust",
                "Price variation >10%% across regions leads to customer confusion and arbitrage issues.",
                "revenue", "INFO", 0.63, "Consistent pricing strategy supports brand integrity and customer trust.",
                List.of("Audit regional pricing quarterly", "Standardize pricing within 5%% variance")));

        rules.add(insight("RULE-033", "Training NPS strongly correlates with product purchase intent",
                "Students who rate training 9+ are 3x more likely to purchase products within 30 days.",
                "cross-domain", "POSITIVE", 0.83, "High-quality training is a powerful customer acquisition channel.",
                List.of("Track training-to-purchase conversion", "Optimize training for product discovery")));

        return rules;
    }

    private BusinessInsight insight(String id, String title, String description, String category,
                                     String severity, double confidence, String impact,
                                     List<String> actions) {
        return new BusinessInsight(id, title, description, category, severity, confidence,
                impact, actions, Map.of(), OffsetDateTime.now());
    }

    private String formatPct(double part, double total) {
        return total > 0 ? String.format("%.1f%%", part / total * 100) : "0%";
    }

    private String formatCurrency(double value) {
        return "₹" + String.format("%,.0f", value);
    }
}
