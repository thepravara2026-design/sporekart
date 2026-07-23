package com.sporekart.marketing.copilot.service;

import com.sporekart.marketing.copilot.domain.*;
import com.sporekart.marketing.copilot.dto.*;
import com.sporekart.marketing.copilot.engine.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class MarketingOrchestrator {

    private static final Logger log = LoggerFactory.getLogger(MarketingOrchestrator.class);

    private final ContentEngine contentEngine;
    private final SEOEngine seoEngine;
    private final AEOGEOEngine aeogeoEngine;
    private final CampaignEngine campaignEngine;
    private final SocialMediaEngine socialMediaEngine;
    private final EmailEngine emailEngine;
    private final WhatsAppEngine whatsAppEngine;
    private final AnalyticsEngine analyticsEngine;
    private final BrandGovernanceEngine brandGovernanceEngine;
    private final GrowthEngine growthEngine;
    private final StrategyEngine strategyEngine;

    public MarketingOrchestrator(ContentEngine contentEngine, SEOEngine seoEngine, AEOGEOEngine aeogeoEngine,
                                  CampaignEngine campaignEngine, SocialMediaEngine socialMediaEngine,
                                  EmailEngine emailEngine, WhatsAppEngine whatsAppEngine,
                                  AnalyticsEngine analyticsEngine, BrandGovernanceEngine brandGovernanceEngine,
                                  GrowthEngine growthEngine, StrategyEngine strategyEngine) {
        this.contentEngine = contentEngine;
        this.seoEngine = seoEngine;
        this.aeogeoEngine = aeogeoEngine;
        this.campaignEngine = campaignEngine;
        this.socialMediaEngine = socialMediaEngine;
        this.emailEngine = emailEngine;
        this.whatsAppEngine = whatsAppEngine;
        this.analyticsEngine = analyticsEngine;
        this.brandGovernanceEngine = brandGovernanceEngine;
        this.growthEngine = growthEngine;
        this.strategyEngine = strategyEngine;
    }

    public MarketingQueryResponse routeQuery(MarketingQueryRequest request) {
        var startTime = System.currentTimeMillis();
        var queryId = UUID.randomUUID().toString();
        log.info("Routing marketing query: intent={} query={}", request.intent(), request.query());

        Map<String, Object> data;
        List<String> recommendations;
        String summary;

        switch (request.intent().toLowerCase()) {
            case "content_generation":
            case "content":
                var contentReq = new ContentGenerationRequest("blog_article", request.query(), request.audience(),
                    "professional", "en_IN", List.of(), null, request.campaignId(), null);
                var contentResp = contentEngine.generateContent(contentReq);
                data = Map.of("content", contentResp);
                recommendations = contentResp.seoRecommendations();
                summary = "Generated " + contentResp.contentType() + " content for: " + request.query();
                break;

            case "campaign_planning":
            case "campaign":
                var campaignReq = new CampaignRequest("Campaign: " + request.query(), "launch",
                    "Brand awareness for " + request.query(), request.audience(), null, null, 50000.0,
                    List.of("email", "social", "paid_ads"), null);
                var campaignResp = campaignEngine.planCampaign(campaignReq);
                data = Map.of("campaign", campaignResp);
                recommendations = campaignResp.recommendations();
                summary = "Planned campaign: " + campaignResp.name();
                break;

            case "seo_analysis":
            case "seo":
                var seoReq = new SEORequest("https://sporekart.com/" + request.query().toLowerCase().replace(" ", "-"),
                    request.query(), "en_IN", "article", null);
                var seoResp = seoEngine.analyzeSEO(seoReq);
                data = Map.of("seo", seoResp);
                recommendations = seoResp.suggestions();
                summary = "SEO analysis for keyword: " + request.query();
                break;

            case "aeo_geo":
            case "aeogeo":
                var aeogeoContent = "Content about " + request.query();
                var aeogeoResp = aeogeoEngine.analyzeAEOGEO(new AEOGEORequest(request.query(), aeogeoContent, "en_IN"));
                data = Map.of("aeogeo", aeogeoResp);
                recommendations = aeogeoResp.optimizationTips();
                summary = "AEO/GEO analysis for query: " + request.query();
                break;

            case "social_media":
            case "social":
                var socialResp = socialMediaEngine.generatePost(
                    new SocialMediaRequest("instagram", request.query(), "IMAGE", "professional",
                        List.of(), null, request.campaignId()));
                data = Map.of("socialPost", socialResp);
                recommendations = List.of("Best posting times: " + String.join(", ", socialResp.bestPostingTimes()));
                summary = "Generated social media post for: " + request.query();
                break;

            case "email":
                var emailResp = emailEngine.generateEmail(
                    new EmailRequest(request.query(), "promotional", request.audience(), "professional", "Shop Now", request.campaignId()));
                data = Map.of("email", emailResp);
                recommendations = emailResp.recommendations();
                summary = "Generated email campaign: " + request.query();
                break;

            case "whatsapp":
                var whatsappResp = whatsAppEngine.generateMessage(
                    new WhatsAppRequest("text", request.query(), request.audience(), request.campaignId(), "Learn More"));
                data = Map.of("whatsapp", whatsappResp);
                recommendations = List.of("Use personalized templates for better engagement");
                summary = "Generated WhatsApp message for: " + request.query();
                break;

            case "analytics":
            case "analyze":
                var analyticsResp = analyticsEngine.analyzeCampaign(
                    new AnalyticsRequest(request.campaignId() != null ? request.campaignId() : "default",
                        null, null));
                data = Map.of("analytics", analyticsResp);
                recommendations = analyticsResp.recommendations();
                summary = "Analytics for campaign: " + (request.campaignId() != null ? request.campaignId() : "default");
                break;

            case "brand_check":
            case "brand":
                var brandResp = brandGovernanceEngine.checkContent(
                    new BrandCheckRequest(request.query(), "professional", "article"));
                data = Map.of("brandCheck", brandResp);
                recommendations = brandResp.suggestions();
                summary = "Brand consistency check completed. Score: " + brandResp.consistencyScore();
                break;

            case "growth":
            case "recommendations":
                var growthResp = growthEngine.generateRecommendations(
                    request.audience() != null ? request.audience() : "general", "all");
                data = Map.of("growth", growthResp);
                recommendations = growthResp.recommendations().stream()
                    .map(r -> r.title() + " (Impact: " + r.projectedImpact() + "%)")
                    .toList();
                summary = growthResp.summary();
                break;

            case "strategy":
            case "brief":
                var briefReq = new MarketingBriefRequest(request.query(), request.audience() != null ? request.audience() : "general",
                    null, List.of("email", "social", "content"), 50000.0, "4 weeks");
                var briefResp = strategyEngine.generateBrief(briefReq);
                data = Map.of("brief", briefResp);
                recommendations = briefResp.competitorSuggestions();
                summary = "Generated marketing brief: " + briefResp.title();
                break;

            default:
                data = Map.of("message", "Unknown intent: " + request.intent());
                recommendations = List.of("Available intents: content, campaign, seo, aeogeo, social, email, whatsapp, analytics, brand, growth, strategy");
                summary = "Unrecognized marketing intent: " + request.intent();
        }

        var processingTime = System.currentTimeMillis() - startTime;
        return new MarketingQueryResponse(queryId, request.intent(), summary, data, recommendations, processingTime);
    }

    public ContentGenerationResponse generateContent(ContentGenerationRequest request) {
        log.info("Orchestrator: generating content for topic: {}", request.topic());
        var response = contentEngine.generateContent(request);
        if (request.keywords() != null && !request.keywords().isEmpty()) {
            contentEngine.optimizeSEO(response.body(), request.keywords());
        }
        return response;
    }

    public CampaignResponse planCampaign(CampaignRequest request) {
        log.info("Orchestrator: planning campaign: {}", request.name());
        return campaignEngine.planCampaign(request);
    }

    public SEOResponse analyzeSEO(SEORequest request) {
        log.info("Orchestrator: analyzing SEO for: {}", request.targetKeyword());
        return seoEngine.analyzeSEO(request);
    }

    public AEOGEOResponse analyzeAEOGEO(AEOGEORequest request) {
        log.info("Orchestrator: analyzing AEO/GEO for: {}", request.query());
        var result = aeogeoEngine.analyzeAEOGEO(request);
        return result;
    }

    public SocialMediaResponse generateSocialPost(SocialMediaRequest request) {
        log.info("Orchestrator: generating social post for: {} on {}", request.topic(), request.platform());
        return socialMediaEngine.generatePost(request);
    }

    public EmailResponse generateEmail(EmailRequest request) {
        log.info("Orchestrator: generating email: {}", request.subject());
        return emailEngine.generateEmail(request);
    }

    public WhatsAppResponse generateWhatsApp(WhatsAppRequest request) {
        log.info("Orchestrator: generating WhatsApp message");
        return whatsAppEngine.generateMessage(request);
    }

    public AnalyticsResponse analyzeCampaign(AnalyticsRequest request) {
        log.info("Orchestrator: analyzing campaign: {}", request.campaignId());
        return analyticsEngine.analyzeCampaign(request);
    }

    public BrandCheckResponse checkBrand(BrandCheckRequest request) {
        log.info("Orchestrator: checking brand consistency");
        return brandGovernanceEngine.checkContent(request);
    }

    public GrowthRecommendationResponse getGrowthRecommendations(String audience, String segment) {
        log.info("Orchestrator: getting growth recommendations for: {}", audience);
        return growthEngine.generateRecommendations(audience, segment);
    }

    public MarketingBriefResponse generateBrief(MarketingBriefRequest request) {
        log.info("Orchestrator: generating marketing brief");
        return strategyEngine.generateBrief(request);
    }
}
