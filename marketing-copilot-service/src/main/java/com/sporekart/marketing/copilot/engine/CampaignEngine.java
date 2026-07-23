package com.sporekart.marketing.copilot.engine;

import com.sporekart.marketing.copilot.domain.*;
import com.sporekart.marketing.copilot.dto.CampaignRequest;
import com.sporekart.marketing.copilot.dto.CampaignResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class CampaignEngine {

    private static final Logger log = LoggerFactory.getLogger(CampaignEngine.class);

    public CampaignResponse planCampaign(CampaignRequest request) {
        log.info("Planning campaign: {} of type: {}", request.name(), request.type());
        var campaignId = UUID.randomUUID().toString();
        var channels = request.channels() != null ? request.channels() : defaultChannels(request.type());
        var contentIds = new ArrayList<String>();
        var recommendations = generateRecommendations(request);
        return new CampaignResponse(campaignId, request.name(), request.type(), "PLANNING",
            request.objective(), request.startDate(), request.endDate(), request.budget(),
            channels, contentIds, recommendations, LocalDateTime.now());
    }

    public Campaign createCampaignRecord(CampaignRequest request) {
        log.debug("Creating campaign record: {}", request.name());
        var channels = request.channels() != null ? request.channels() : defaultChannels(request.type());
        return new Campaign(UUID.randomUUID().toString(), request.name(),
            Campaign.CampaignType.valueOf(request.type().toUpperCase()), Campaign.CampaignStatus.PLANNING,
            request.objective(), request.targetAudience(), request.startDate(), request.endDate(),
            request.budget(), channels, List.of(), List.of(), LocalDateTime.now(), "system");
    }

    public List<String> generateRecommendations(CampaignRequest request) {
        log.debug("Generating recommendations for campaign: {}", request.name());
        var recs = new ArrayList<String>();
        recs.add("Start with A/B testing on ad creatives during first week");
        recs.add("Allocate 60% budget to " + (request.channels() != null && !request.channels().isEmpty() ? request.channels().get(0) : "primary channel"));
        recs.add("Set up conversion tracking before launch");
        recs.add("Create audience retargeting pools from Day 1");
        if ("launch".equalsIgnoreCase(request.type())) {
            recs.add("Build pre-launch email sequence for warm leads");
            recs.add("Leverage influencer partnerships for initial buzz");
        } else if ("seasonal".equalsIgnoreCase(request.type())) {
            recs.add("Create urgency with limited-time offers");
            recs.add("Optimize landing pages for seasonal keywords");
        }
        return recs;
    }

    public List<String> optimizeBudget(Campaign campaign, double totalBudget) {
        log.info("Optimizing budget for campaign: {} total: {}", campaign.name(), totalBudget);
        return List.of(
            "Content marketing: " + (totalBudget * 0.30),
            "Paid advertising: " + (totalBudget * 0.35),
            "Email marketing: " + (totalBudget * 0.15),
            "Social media: " + (totalBudget * 0.15),
            "Contingency: " + (totalBudget * 0.05)
        );
    }

    public CampaignMetric simulatePerformance(Campaign campaign) {
        log.debug("Simulating performance for campaign: {}", campaign.name());
        var today = LocalDate.now();
        return new CampaignMetric(today, 50000, 2500, 125, 5000.0, 25000.0, 5.0, 5.0, 5.0);
    }

    private List<String> defaultChannels(String type) {
        return switch (type.toLowerCase()) {
            case "launch" -> List.of("email", "social", "paid_ads", "influencer");
            case "seasonal" -> List.of("email", "social", "paid_ads");
            case "brand_awareness" -> List.of("social", "content", "pr", "influencer");
            case "lead_generation" -> List.of("email", "paid_ads", "webinar");
            default -> List.of("email", "social");
        };
    }
}
