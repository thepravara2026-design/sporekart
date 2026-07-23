package com.sporekart.marketing.copilot.service;

import com.sporekart.marketing.copilot.dto.*;
import com.sporekart.marketing.copilot.engine.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MarketingOrchestratorTest {

    @Mock private ContentEngine contentEngine;
    @Mock private SEOEngine seoEngine;
    @Mock private AEOGEOEngine aeogeoEngine;
    @Mock private CampaignEngine campaignEngine;
    @Mock private SocialMediaEngine socialMediaEngine;
    @Mock private EmailEngine emailEngine;
    @Mock private WhatsAppEngine whatsAppEngine;
    @Mock private AnalyticsEngine analyticsEngine;
    @Mock private BrandGovernanceEngine brandGovernanceEngine;
    @Mock private GrowthEngine growthEngine;
    @Mock private StrategyEngine strategyEngine;

    private MarketingOrchestrator orchestrator;

    @BeforeEach
    void setUp() {
        orchestrator = new MarketingOrchestrator(contentEngine, seoEngine, aeogeoEngine, campaignEngine,
            socialMediaEngine, emailEngine, whatsAppEngine, analyticsEngine, brandGovernanceEngine,
            growthEngine, strategyEngine);
    }

    @Test
    void testRouteQueryContentIntent() {
        when(contentEngine.generateContent(any())).thenReturn(
            new ContentGenerationResponse("c1", "Title", "Body", "blog_article", "DRAFT", java.util.List.of(), java.time.LocalDateTime.now()));
        var request = new MarketingQueryRequest("Mushroom guide", "content", "home-growers", null);
        var response = orchestrator.routeQuery(request);
        assertNotNull(response);
        assertEquals("content", response.intent());
        assertNotNull(response.data());
        assertTrue(response.data().containsKey("content"));
    }

    @Test
    void testRouteQueryCampaignIntent() {
        when(campaignEngine.planCampaign(any())).thenReturn(
            new CampaignResponse("camp-1", "Campaign", "launch", "PLANNING", "Test", null, null, 0, java.util.List.of(), java.util.List.of(), java.util.List.of(), java.time.LocalDateTime.now()));
        var request = new MarketingQueryRequest("Spring campaign", "campaign", "all", null);
        var response = orchestrator.routeQuery(request);
        assertNotNull(response);
        assertEquals("campaign", response.intent());
    }

    @Test
    void testRouteQuerySEOIntent() {
        when(seoEngine.analyzeSEO(any())).thenReturn(
            new SEOResponse("url", "keyword", 1000, 50.0, 5, java.util.List.of("Suggestion"),
                new SEOResponse.SEOChecklistResponse(80.0, java.util.List.of(), java.util.List.of(), java.util.List.of()), 300, "en_IN"));
        var request = new MarketingQueryRequest("organic mushrooms", "seo", null, null);
        var response = orchestrator.routeQuery(request);
        assertNotNull(response);
        assertEquals("seo", response.intent());
    }

    @Test
    void testRouteQueryAEOGEOIntent() {
        when(aeogeoEngine.analyzeAEOGEO(any())).thenReturn(
            new AEOGEOResponse("query", 80.0, 75.0, java.util.List.of(), java.util.List.of(), "snippet", java.util.List.of()));
        var request = new MarketingQueryRequest("how to grow mushrooms", "aeogeo", null, null);
        var response = orchestrator.routeQuery(request);
        assertNotNull(response);
        assertEquals("aeogeo", response.intent());
    }

    @Test
    void testRouteQuerySocialIntent() {
        when(socialMediaEngine.generatePost(any())).thenReturn(
            new SocialMediaResponse("p1", "instagram", "Content", java.util.List.of("#tag"), "IMAGE", "CTA", java.time.LocalDateTime.now(), java.util.List.of("10 AM")));
        var request = new MarketingQueryRequest("Mushroom tips", "social", null, null);
        var response = orchestrator.routeQuery(request);
        assertNotNull(response);
        assertEquals("social", response.intent());
    }

    @Test
    void testRouteQueryEmailIntent() {
        when(emailEngine.generateEmail(any())).thenReturn(
            new EmailResponse("e1", "Subject", "Preheader", "<html></html>", "Text", "promotional", java.util.List.of()));
        var request = new MarketingQueryRequest("Newsletter", "email", "all", null);
        var response = orchestrator.routeQuery(request);
        assertNotNull(response);
        assertEquals("email", response.intent());
    }

    @Test
    void testRouteQueryWhatsAppIntent() {
        when(whatsAppEngine.generateMessage(any())).thenReturn(
            new WhatsAppResponse("w1", "template", "Body", "text", "all"));
        var request = new MarketingQueryRequest("Offer update", "whatsapp", "active_buyers", null);
        var response = orchestrator.routeQuery(request);
        assertNotNull(response);
        assertEquals("whatsapp", response.intent());
    }

    @Test
    void testRouteQueryAnalyticsIntent() {
        when(analyticsEngine.analyzeCampaign(any())).thenReturn(
            new AnalyticsResponse("camp-1", java.util.Map.of(), java.util.Map.of(), java.util.List.of(), java.util.List.of()));
        var request = new MarketingQueryRequest("Campaign performance", "analytics", null, "camp-1");
        var response = orchestrator.routeQuery(request);
        assertNotNull(response);
        assertEquals("analytics", response.intent());
    }

    @Test
    void testRouteQueryBrandIntent() {
        when(brandGovernanceEngine.checkContent(any())).thenReturn(
            new BrandCheckResponse(85.0, java.util.List.of(), java.util.List.of(), true));
        var request = new MarketingQueryRequest("Check this content", "brand", null, null);
        var response = orchestrator.routeQuery(request);
        assertNotNull(response);
        assertEquals("brand", response.intent());
    }

    @Test
    void testRouteQueryGrowthIntent() {
        when(growthEngine.generateRecommendations(any(), any())).thenReturn(
            new GrowthRecommendationResponse(java.util.List.of(), "Summary"));
        var request = new MarketingQueryRequest("Growth ideas", "growth", "home-growers", null);
        var response = orchestrator.routeQuery(request);
        assertNotNull(response);
        assertEquals("growth", response.intent());
    }

    @Test
    void testRouteQueryStrategyIntent() {
        when(strategyEngine.generateBrief(any())).thenReturn(
            new MarketingBriefResponse("b1", "Title", "Objective", "Audience", "Message", java.util.List.of(), java.util.List.of(), "4 weeks", java.util.List.of(), "Metrics"));
        var request = new MarketingQueryRequest("Strategy plan", "strategy", "commercial-growers", null);
        var response = orchestrator.routeQuery(request);
        assertNotNull(response);
        assertEquals("strategy", response.intent());
    }

    @Test
    void testRouteQueryDefaultIntent() {
        var request = new MarketingQueryRequest("Something", "unknown_intent", null, null);
        var response = orchestrator.routeQuery(request);
        assertNotNull(response);
        assertEquals("unknown_intent", response.intent());
        assertNotNull(response.recommendations());
    }

    @Test
    void testGenerateContent() {
        when(contentEngine.generateContent(any())).thenReturn(
            new ContentGenerationResponse("c1", "Title", "Body", "article", "DRAFT", java.util.List.of(), java.time.LocalDateTime.now()));
        var request = new ContentGenerationRequest("article", "Topic", null, null, null, null, null, null, null);
        var response = orchestrator.generateContent(request);
        assertNotNull(response);
        assertEquals("c1", response.contentId());
    }

    @Test
    void testPlanCampaign() {
        when(campaignEngine.planCampaign(any())).thenReturn(
            new CampaignResponse("c1", "Name", "launch", "PLANNING", "Obj", null, null, 0, java.util.List.of(), java.util.List.of(), java.util.List.of(), java.time.LocalDateTime.now()));
        var request = new CampaignRequest("Name", "launch", "Obj", null, null, null, 0, null, null);
        var response = orchestrator.planCampaign(request);
        assertNotNull(response);
    }

    @Test
    void testAnalyzeSEO() {
        when(seoEngine.analyzeSEO(any())).thenReturn(
            new SEOResponse("url", "kw", 100, 50.0, 5, java.util.List.of(),
                new SEOResponse.SEOChecklistResponse(80.0, java.util.List.of(), java.util.List.of(), java.util.List.of()), 100, "en"));
        var request = new SEORequest("url", "kw", "en", "article", null);
        var response = orchestrator.analyzeSEO(request);
        assertNotNull(response);
    }

    @Test
    void testAnalyzeAEOGEO() {
        when(aeogeoEngine.analyzeAEOGEO(any())).thenReturn(
            new AEOGEOResponse("q", 80.0, 70.0, java.util.List.of(), java.util.List.of(), "snippet", java.util.List.of()));
        var request = new AEOGEORequest("q", "content", "en");
        var response = orchestrator.analyzeAEOGEO(request);
        assertNotNull(response);
    }

    @Test
    void testGenerateSocialPost() {
        when(socialMediaEngine.generatePost(any())).thenReturn(
            new SocialMediaResponse("p1", "ig", "Content", java.util.List.of(), "IMAGE", null, java.time.LocalDateTime.now(), java.util.List.of()));
        var request = new SocialMediaRequest("ig", "topic", "IMAGE", null, null, null, null);
        var response = orchestrator.generateSocialPost(request);
        assertNotNull(response);
    }

    @Test
    void testGenerateEmail() {
        when(emailEngine.generateEmail(any())).thenReturn(
            new EmailResponse("e1", "Subj", "Pre", "<html></html>", "Text", "promo", java.util.List.of()));
        var request = new EmailRequest("Subj", "promo", null, null, null, null);
        var response = orchestrator.generateEmail(request);
        assertNotNull(response);
    }

    @Test
    void testGenerateWhatsApp() {
        when(whatsAppEngine.generateMessage(any())).thenReturn(
            new WhatsAppResponse("w1", "tpl", "Body", "text", "all"));
        var request = new WhatsAppRequest("text", "Body", null, null, null);
        var response = orchestrator.generateWhatsApp(request);
        assertNotNull(response);
    }

    @Test
    void testAnalyzeCampaign() {
        when(analyticsEngine.analyzeCampaign(any())).thenReturn(
            new AnalyticsResponse("c1", java.util.Map.of(), java.util.Map.of(), java.util.List.of(), java.util.List.of()));
        var request = new AnalyticsRequest("c1", null, null);
        var response = orchestrator.analyzeCampaign(request);
        assertNotNull(response);
    }

    @Test
    void testCheckBrand() {
        when(brandGovernanceEngine.checkContent(any())).thenReturn(
            new BrandCheckResponse(90.0, java.util.List.of(), java.util.List.of(), true));
        var request = new BrandCheckRequest("Content", null, null);
        var response = orchestrator.checkBrand(request);
        assertNotNull(response);
    }

    @Test
    void testGetGrowthRecommendations() {
        when(growthEngine.generateRecommendations(any(), any())).thenReturn(
            new GrowthRecommendationResponse(java.util.List.of(), "Summary"));
        var response = orchestrator.getGrowthRecommendations("home-growers", "beginners");
        assertNotNull(response);
    }

    @Test
    void testGenerateBrief() {
        when(strategyEngine.generateBrief(any())).thenReturn(
            new MarketingBriefResponse("b1", "Title", "Obj", "Aud", "Msg", java.util.List.of(), java.util.List.of(), "4w", java.util.List.of(), "Metrics"));
        var request = new MarketingBriefRequest("Obj", "Aud", null, null, 0, null);
        var response = orchestrator.generateBrief(request);
        assertNotNull(response);
    }
}
