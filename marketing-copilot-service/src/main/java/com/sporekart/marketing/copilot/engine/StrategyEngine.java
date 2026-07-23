package com.sporekart.marketing.copilot.engine;

import com.sporekart.marketing.copilot.domain.MarketingBrief;
import com.sporekart.marketing.copilot.domain.MarketingPersona;
import com.sporekart.marketing.copilot.dto.MarketingBriefRequest;
import com.sporekart.marketing.copilot.dto.MarketingBriefResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class StrategyEngine {

    private static final Logger log = LoggerFactory.getLogger(StrategyEngine.class);

    public MarketingBriefResponse generateBrief(MarketingBriefRequest request) {
        log.info("Generating marketing brief for objective: {}", request.objective());
        var briefId = UUID.randomUUID().toString();
        var title = generateTitle(request);
        var deliverables = generateDeliverables(request);
        var competitorSuggestions = generateCompetitorSuggestions(request);
        var successMetrics = generateSuccessMetrics(request);
        return new MarketingBriefResponse(briefId, title, request.objective(), request.targetAudience(),
            request.keyMessage() != null ? request.keyMessage() : "Default key message for " + request.objective(),
            request.channels() != null ? request.channels() : List.of("email", "social", "content"),
            deliverables, request.timeline() != null ? request.timeline() : "4 weeks",
            competitorSuggestions, successMetrics);
    }

    public MarketingBrief createBrief(MarketingBriefRequest request) {
        log.debug("Creating brief record");
        return new MarketingBrief(UUID.randomUUID().toString(), "Marketing Brief: " + request.objective(),
            request.objective(), request.targetAudience(), "Key message for " + request.objective(),
            request.channels() != null ? request.channels() : List.of("email", "social"),
            List.of("Content calendar", "Campaign assets", "Performance report"),
            request.budget(), request.timeline(), "SporeKart Brand Guidelines",
            List.of("Competitor A", "Competitor B"), "ROAS, CTR, Conversion Rate, Engagement");
    }

    public List<MarketingPersona> generatePersonas(String segment) {
        log.info("Generating personas for segment: {}", segment);
        if ("home-growers".equalsIgnoreCase(segment)) {
            return List.of(
                new MarketingPersona("p1", "Beginner Bob", segment, "New to mushroom cultivation, seeking easy solutions",
                    List.of("Contamination issues", "Low yield", "Limited space"),
                    List.of("Grow first harvest", "Learn best practices", "Join community"),
                    List.of("YouTube", "Blog", "Email"), "Simple and encouraging", "Price, ease of use"),
                new MarketingPersona("p2", "Hobbyist Helen", segment, "Experienced home grower looking to expand",
                    List.of("Scaling up", "Advanced techniques", "Quality improvement"),
                    List.of("Increase yield", "Try exotic varieties", "Optimize setup"),
                    List.of("Blog", "Instagram", "Forum"), "Detailed and technical", "Quality, innovation")
            );
        } else if ("commercial-growers".equalsIgnoreCase(segment)) {
            return List.of(
                new MarketingPersona("p3", "Farmer Frank", segment, "Commercial mushroom farm owner",
                    List.of("Operational efficiency", "Cost reduction", "Market access"),
                    List.of("Scale production", "Reduce costs", "Expand distribution"),
                    List.of("LinkedIn", "Email", "Trade shows"), "Professional and data-driven", "ROI, reliability, scale")
            );
        }
        return List.of();
    }

    public List<String> analyzeCompetitors(String industry) {
        log.info("Analyzing competitors in industry: {}", industry);
        return List.of(
            "Competitor A: Strong in content marketing, weak in social media",
            "Competitor B: Dominant in paid search, lacks organic presence",
            "Competitor C: Emerging player with innovative product positioning",
            "Opportunity: Underserved niche in sustainable mushroom products",
            "Opportunity: Growing demand for educational content in " + industry
        );
    }

    private String generateTitle(MarketingBriefRequest request) {
        return "Marketing Strategy: " + request.objective() + " for " + request.targetAudience();
    }

    private List<String> generateDeliverables(MarketingBriefRequest request) {
        var deliverables = new ArrayList<String>();
        deliverables.add("Creative brief document");
        deliverables.add("Content calendar (4 weeks)");
        deliverables.add("Campaign performance dashboard");
        deliverables.add("A/B testing framework");
        if (request.channels() != null) {
            for (var channel : request.channels()) {
                deliverables.add(channel.substring(0, 1).toUpperCase() + channel.substring(1) + " assets");
            }
        }
        return deliverables;
    }

    private List<String> generateCompetitorSuggestions(MarketingBriefRequest request) {
        return List.of(
            "Monitor competitor campaigns targeting " + request.targetAudience(),
            "Differentiate messaging around SporeKart's unique value proposition",
            "Identify gaps in competitor content strategy to exploit"
        );
    }

    private String generateSuccessMetrics(MarketingBriefRequest request) {
        return "Primary: ROAS > 4.0, CTR > 3%, Conversion Rate > 4%. Secondary: Brand mentions, Engagement rate, Audience growth";
    }
}
