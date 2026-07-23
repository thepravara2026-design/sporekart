package com.sporekart.marketing.copilot.engine;

import com.sporekart.marketing.copilot.domain.GrowthRecommendation;
import com.sporekart.marketing.copilot.domain.GrowthRecommendation.GrowthArea;
import com.sporekart.marketing.copilot.dto.GrowthRecommendationResponse;
import com.sporekart.marketing.copilot.dto.GrowthRecommendationResponse.GrowthItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class GrowthEngine {

    private static final Logger log = LoggerFactory.getLogger(GrowthEngine.class);

    public GrowthRecommendationResponse generateRecommendations(String audience, String segment) {
        log.info("Generating growth recommendations for audience: {} segment: {}", audience, segment);
        var items = new ArrayList<GrowthItem>();
        items.add(new GrowthItem("Expand Digital Presence",
            "Increase organic reach through SEO-optimized content and social media engagement",
            "CUSTOMER_ACQUISITION", 85.0, 0.92, "3-6 months"));
        items.add(new GrowthItem("Loyalty Program Implementation",
            "Launch a tiered loyalty program to increase customer retention and lifetime value",
            "CUSTOMER_RETENTION", 75.0, 0.88, "2-3 months"));
        items.add(new GrowthItem("Market Expansion Strategy",
            "Enter new geographic markets with localized marketing campaigns",
            "MARKET_EXPANSION", 65.0, 0.75, "6-12 months"));
        items.add(new GrowthItem("Content Marketing Scale",
            "Scale content production with AI-assisted creation and distribution across channels",
            "CONTENT_STRATEGY", 90.0, 0.95, "1-2 months"));
        items.add(new GrowthItem("Channel Diversification",
            "Expand into emerging platforms (Threads, WhatsApp Business) for broader reach",
            "CHANNEL_EXPANSION", 70.0, 0.82, "2-4 months"));

        return new GrowthRecommendationResponse(items, "Priority recommendations based on " + audience + " analysis showing highest impact in content strategy and digital presence");
    }

    public GrowthRecommendation createCustomRecommendation(String title, String description, GrowthArea area,
                                                           double impact, double confidence, List<String> actions) {
        log.debug("Creating custom recommendation: {}", title);
        return new GrowthRecommendation(UUID.randomUUID().toString(), title, description, area, impact,
            confidence, "3-6 months", actions, "Marketing team time allocation");
    }

    public List<GrowthRecommendation> prioritizeRecommendations(List<GrowthRecommendation> recs) {
        log.debug("Prioritizing {} recommendations", recs != null ? recs.size() : 0);
        if (recs == null) return List.of();
        return recs.stream()
            .sorted((a, b) -> Double.compare(b.projectedImpact() * b.confidence(), a.projectedImpact() * a.confidence()))
            .toList();
    }

    public double calculateProjectedROI(double investment, double expectedReturn) {
        if (investment <= 0) return 0.0;
        return Math.round(((expectedReturn - investment) / investment) * 1000.0) / 10.0;
    }

    public List<String> suggestExperiments(String growthArea) {
        log.info("Suggesting experiments for growth area: {}", growthArea);
        return switch (growthArea.toUpperCase()) {
            case "CUSTOMER_ACQUISITION" -> List.of(
                "A/B test different ad copy variations on social media",
                "Test referral program with 10% discount incentive",
                "Run influencer partnership pilot with 5 micro-influencers"
            );
            case "CUSTOMER_RETENTION" -> List.of(
                "Test personalized email sequences for inactive users",
                "Implement loyalty points pilot for top 10% customers",
                "A/B test re-engagement push notification timing"
            );
            case "CONTENT_STRATEGY" -> List.of(
                "Compare blog post vs video tutorial engagement rates",
                "Test AI-generated content performance vs human-written",
                "Measure impact of daily vs weekly posting frequency"
            );
            default -> List.of("Run channel attribution analysis", "Test cross-channel messaging consistency");
        };
    }
}
