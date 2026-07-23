package com.sporekart.bi.copilot.engine;

import com.sporekart.bi.copilot.domain.CustomerSegment;
import com.sporekart.bi.copilot.domain.TrendDataPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class CustomerSegmentationEngine {

    private static final Logger log = LoggerFactory.getLogger(CustomerSegmentationEngine.class);

    private final List<CustomerSegment> segments;

    public CustomerSegmentationEngine() {
        this.segments = seedSegments();
        log.info("Initialized CustomerSegmentationEngine with {} segments", segments.size());
    }

    private List<CustomerSegment> seedSegments() {
        return List.of(
                new CustomerSegment("SEG_HIGH_VALUE", "High-Value Growers",
                        "Established growers with consistent high-volume purchases and premium product adoption",
                        250, 1250000, 5000.0, 3.2,
                        List.of("3+ years active", "Avg order > Rs.5000", "Multiple species purchased",
                                "Training program graduates", "Premium spawn users"),
                        List.of("Dedicated account manager", "Early access to new products",
                                "Exclusive training invites", "Volume discount tiers")),

                new CustomerSegment("SEG_NEW_GROWER", "New Growers",
                        "Customers who joined within the last 6 months, exploring mushroom cultivation",
                        850, 340000, 400.0, 18.5,
                        List.of("< 6 months active", "Single species purchases", "High support ticket volume",
                                "Low repeat purchase rate", "Price sensitive"),
                        List.of("Structured onboarding sequence", "Beginner training bundle",
                                "Starter kit discount", "Community mentorship pairing")),

                new CustomerSegment("SEG_HOBBYIST", "Hobbyist Cultivators",
                        "Small-scale growers cultivating for personal consumption and local sharing",
                        1200, 480000, 400.0, 8.1,
                        List.of("Small order sizes", "Seasonal purchasing pattern",
                                "Interest in exotic species", "Active in community forums",
                                "Low certification interest"),
                        List.of("Community engagement programs", "Exotic species subscriptions",
                                "Project-based learning content", "Peer mentoring opportunities")),

                new CustomerSegment("SEG_COMMERCIAL", "Commercial Farms",
                        "Large-scale commercial mushroom producers with regular bulk orders",
                        80, 1800000, 22500.0, 1.8,
                        List.of("Bulk order quantities", "Regular weekly/monthly orders",
                                "Multiple facility operations", "Technical consultation users",
                                "Long-term contracts preferred"),
                        List.of("Dedicated relationship manager", "Bulk pricing agreements",
                                "On-site technical support", "Custom spawn formulation")),

                new CustomerSegment("SEG_DISTRIBUTOR", "Distributors & Resellers",
                        "Businesses that resell products to end consumers",
                        120, 960000, 8000.0, 5.4,
                        List.of("Large order values", "Branding/white-label interest",
                                "Logistics support needed", "Marketing collateral requests",
                                "Seasonal bulk buying"),
                        List.of("Reseller portal access", "Marketing material kit",
                                "Logistics partnership program", "Trade show support")),

                new CustomerSegment("SEG_AT_RISK", "At-Risk Customers",
                        "Customers showing declining engagement and likely to churn",
                        340, 204000, 600.0, 45.0,
                        List.of("No purchase in 90+ days", "Decreasing order frequency",
                                "Opened support tickets unresolved", "Competitor trial detected",
                                "Reduced platform login activity"),
                        List.of("Win-back campaign with special offer", "Personalized outreach call",
                                "Satisfaction survey with incentive", "Product recommendation refresh"))
        );
    }

    public List<CustomerSegment> segmentCustomers() {
        log.debug("Returning all customer segments");
        return segments;
    }

    public CustomerSegment getSegmentById(String segmentId) {
        log.debug("Looking up segment: {}", segmentId);
        return segments.stream()
                .filter(s -> s.segmentId().equals(segmentId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Segment not found: " + segmentId));
    }

    public List<String> getSegmentRecommendations(String segmentId) {
        CustomerSegment segment = getSegmentById(segmentId);
        log.debug("Returning recommendations for segment: {}", segmentId);
        return segment.recommendedStrategies();
    }

    public List<TrendDataPoint> analyzeSegmentTrends(String segmentId, int months) {
        log.debug("Analyzing trends for segment {} over {} months", segmentId, months);
        CustomerSegment segment = getSegmentById(segmentId);
        List<TrendDataPoint> trends = new ArrayList<>();
        Random rng = new Random(segmentId.hashCode());
        for (int i = months; i >= 0; i--) {
            OffsetDateTime ts = OffsetDateTime.now().minusMonths(i);
            double value = segment.customerCount() * (1 + rng.nextGaussian() * 0.05);
            double movingAvg = value * (1 + rng.nextGaussian() * 0.02);
            trends.add(new TrendDataPoint(
                    UUID.randomUUID().toString(), segment.name() + "_customer_count",
                    ts.getMonth().name() + " " + ts.getYear(), value, movingAvg,
                    1.0, value * 1.02, value * 0.05,
                    value > movingAvg ? "up" : "down",
                    ((value - movingAvg) / movingAvg) * 100, ts
            ));
        }
        return trends;
    }

    public Map<String, Double> getSegmentRevenueContribution() {
        log.debug("Calculating segment revenue contribution");
        double totalRevenue = segments.stream().mapToDouble(CustomerSegment::totalRevenue).sum();
        Map<String, Double> contribution = new LinkedHashMap<>();
        segments.forEach(s -> contribution.put(s.name(),
                totalRevenue > 0 ? Math.round((s.totalRevenue() / totalRevenue) * 100.0 * 10.0) / 10.0 : 0));
        return contribution;
    }

    public List<CustomerSegment> getHighValueSegments() {
        log.debug("Identifying high-value segments");
        return segments.stream()
                .filter(s -> s.averageRevenue() > 2000)
                .sorted(Comparator.comparingDouble(CustomerSegment::averageRevenue).reversed())
                .collect(Collectors.toList());
    }

    public List<CustomerSegment> getAtRiskSegments() {
        log.debug("Identifying at-risk segments");
        return segments.stream()
                .filter(s -> s.churnRate() > 10)
                .sorted(Comparator.comparingDouble(CustomerSegment::churnRate).reversed())
                .collect(Collectors.toList());
    }

    public Map<String, Object> recommendMarketingStrategy(String segmentId) {
        CustomerSegment segment = getSegmentById(segmentId);
        log.debug("Recommending marketing strategy for segment: {}", segmentId);
        String segmentType = segment.segmentId();
        Map<String, Object> strategy = new LinkedHashMap<>();
        switch (segmentType) {
            case "SEG_HIGH_VALUE":
                strategy.put("channels", List.of("Email (personalized)", "Phone (account manager)", "Exclusive events"));
                strategy.put("messaging", "Loyalty recognition, early access, VIP benefits, community leadership");
                strategy.put("offers", List.of("Exclusive product previews", "Referral rewards", "Annual growth review"));
                break;
            case "SEG_NEW_GROWER":
                strategy.put("channels", List.of("Email (automated drip)", "In-app notifications", "WhatsApp", "SMS"));
                strategy.put("messaging", "Getting started guide, success stories, milestone celebrations, educational tips");
                strategy.put("offers", List.of("First-order discount", "Starter kit bundle", "Free beginner webinar"));
                break;
            case "SEG_HOBBYIST":
                strategy.put("channels", List.of("Social media", "Community forum", "Email newsletter", "YouTube"));
                strategy.put("messaging", "Community highlights, exotic species spotlights, DIY projects, member stories");
                strategy.put("offers", List.of("Species of the month club", "Community challenge rewards", "Workshop discounts"));
                break;
            case "SEG_COMMERCIAL":
                strategy.put("channels", List.of("Phone (relationship manager)", "Email (quarterly business review)", "In-person meetings"));
                strategy.put("messaging", "ROI analysis, bulk efficiency, technical innovation, industry insights");
                strategy.put("offers", List.of("Volume-based pricing", "Custom substrate formulation", "On-site consultation"));
                break;
            case "SEG_DISTRIBUTOR":
                strategy.put("channels", List.of("Email (partner newsletter)", "Trade shows", "Partner portal", "Webinars"));
                strategy.put("messaging", "Margin optimization, co-branding opportunities, market trends, logistics updates");
                strategy.put("offers", List.of("Co-marketing funds", "Sample kits", "Early payment discounts"));
                break;
            case "SEG_AT_RISK":
                strategy.put("channels", List.of("Email (re-engagement)", "Phone (personal outreach)", "SMS", "Direct mail"));
                strategy.put("messaging", "We miss you, new product highlights, exclusive comeback offer, customer feedback");
                strategy.put("offers", List.of("Comeback discount (30% off)", "Free consultation session", "Priority support access"));
                break;
            default:
                strategy.put("channels", List.of("Email", "Social media"));
                strategy.put("messaging", "General brand awareness and product education");
                strategy.put("offers", List.of("Standard promotions"));
        }
        strategy.put("segmentId", segment.segmentId());
        strategy.put("segmentName", segment.name());
        return strategy;
    }
}
