package com.sporekart.marketing.copilot.controller;

import com.sporekart.marketing.copilot.dto.*;
import com.sporekart.marketing.copilot.service.MarketingOrchestrator;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/copilot/marketing")
public class MarketingCopilotController {

    private static final Logger log = LoggerFactory.getLogger(MarketingCopilotController.class);

    private final MarketingOrchestrator orchestrator;

    public MarketingCopilotController(MarketingOrchestrator orchestrator) {
        this.orchestrator = orchestrator;
    }

    @PostMapping("/query")
    public ResponseEntity<MarketingCopilotResponse<MarketingQueryResponse>> handleQuery(
            @Valid @RequestBody MarketingQueryRequest request) {
        log.info("REST: marketing query intent={}", request.intent());
        var response = orchestrator.routeQuery(request);
        return ResponseEntity.ok(MarketingCopilotResponse.success(response));
    }

    @PostMapping("/content/generate")
    public ResponseEntity<MarketingCopilotResponse<ContentGenerationResponse>> generateContent(
            @Valid @RequestBody ContentGenerationRequest request) {
        log.info("REST: generate content topic={}", request.topic());
        var response = orchestrator.generateContent(request);
        return ResponseEntity.ok(MarketingCopilotResponse.success(response));
    }

    @PostMapping("/campaign/plan")
    public ResponseEntity<MarketingCopilotResponse<CampaignResponse>> planCampaign(
            @Valid @RequestBody CampaignRequest request) {
        log.info("REST: plan campaign name={}", request.name());
        var response = orchestrator.planCampaign(request);
        return ResponseEntity.ok(MarketingCopilotResponse.success(response));
    }

    @PostMapping("/seo/analyze")
    public ResponseEntity<MarketingCopilotResponse<SEOResponse>> analyzeSEO(
            @Valid @RequestBody SEORequest request) {
        log.info("REST: analyze SEO keyword={}", request.targetKeyword());
        var response = orchestrator.analyzeSEO(request);
        return ResponseEntity.ok(MarketingCopilotResponse.success(response));
    }

    @PostMapping("/aeogeo/analyze")
    public ResponseEntity<MarketingCopilotResponse<AEOGEOResponse>> analyzeAEOGEO(
            @Valid @RequestBody AEOGEORequest request) {
        log.info("REST: analyze AEO/GEO query={}", request.query());
        var response = orchestrator.analyzeAEOGEO(request);
        return ResponseEntity.ok(MarketingCopilotResponse.success(response));
    }

    @PostMapping("/social/generate")
    public ResponseEntity<MarketingCopilotResponse<SocialMediaResponse>> generateSocialPost(
            @Valid @RequestBody SocialMediaRequest request) {
        log.info("REST: generate social post platform={} topic={}", request.platform(), request.topic());
        var response = orchestrator.generateSocialPost(request);
        return ResponseEntity.ok(MarketingCopilotResponse.success(response));
    }

    @PostMapping("/email/generate")
    public ResponseEntity<MarketingCopilotResponse<EmailResponse>> generateEmail(
            @Valid @RequestBody EmailRequest request) {
        log.info("REST: generate email subject={}", request.subject());
        var response = orchestrator.generateEmail(request);
        return ResponseEntity.ok(MarketingCopilotResponse.success(response));
    }

    @PostMapping("/whatsapp/generate")
    public ResponseEntity<MarketingCopilotResponse<WhatsAppResponse>> generateWhatsApp(
            @Valid @RequestBody WhatsAppRequest request) {
        log.info("REST: generate WhatsApp message");
        var response = orchestrator.generateWhatsApp(request);
        return ResponseEntity.ok(MarketingCopilotResponse.success(response));
    }

    @PostMapping("/analytics/campaign")
    public ResponseEntity<MarketingCopilotResponse<AnalyticsResponse>> analyzeCampaign(
            @Valid @RequestBody AnalyticsRequest request) {
        log.info("REST: analyze campaign id={}", request.campaignId());
        var response = orchestrator.analyzeCampaign(request);
        return ResponseEntity.ok(MarketingCopilotResponse.success(response));
    }

    @PostMapping("/brand/check")
    public ResponseEntity<MarketingCopilotResponse<BrandCheckResponse>> checkBrand(
            @Valid @RequestBody BrandCheckRequest request) {
        log.info("REST: check brand consistency");
        var response = orchestrator.checkBrand(request);
        return ResponseEntity.ok(MarketingCopilotResponse.success(response));
    }

    @PostMapping("/growth/recommendations")
    public ResponseEntity<MarketingCopilotResponse<GrowthRecommendationResponse>> getGrowthRecommendations(
            @RequestParam(defaultValue = "general") String audience,
            @RequestParam(defaultValue = "all") String segment) {
        log.info("REST: growth recommendations audience={} segment={}", audience, segment);
        var response = orchestrator.getGrowthRecommendations(audience, segment);
        return ResponseEntity.ok(MarketingCopilotResponse.success(response));
    }

    @PostMapping("/strategy/brief")
    public ResponseEntity<MarketingCopilotResponse<MarketingBriefResponse>> generateBrief(
            @Valid @RequestBody MarketingBriefRequest request) {
        log.info("REST: generate marketing brief");
        var response = orchestrator.generateBrief(request);
        return ResponseEntity.ok(MarketingCopilotResponse.success(response));
    }

    @GetMapping("/health")
    public ResponseEntity<MarketingCopilotResponse<String>> health() {
        return ResponseEntity.ok(MarketingCopilotResponse.success("Marketing Copilot is operational"));
    }
}
