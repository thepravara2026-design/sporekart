package com.sporekart.marketing.copilot.controller;

import com.sporekart.marketing.copilot.dto.*;
import com.sporekart.marketing.copilot.service.MarketingOrchestrator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MarketingCopilotControllerTest {

    @Mock
    private MarketingOrchestrator orchestrator;

    @InjectMocks
    private MarketingCopilotController controller;

    @Test
    void testHandleQuery() {
        when(orchestrator.routeQuery(any())).thenReturn(
            new MarketingQueryResponse("q1", "content", "Summary", Map.of(), List.of(), 100L));
        var request = new MarketingQueryRequest("Test", "content", null, null);
        var response = controller.handleQuery(request);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().success());
    }

    @Test
    void testGenerateContent() {
        when(orchestrator.generateContent(any())).thenReturn(
            new ContentGenerationResponse("c1", "Title", "Body", "article", "DRAFT", List.of(), LocalDateTime.now()));
        var request = new ContentGenerationRequest("article", "Topic", null, null, null, null, null, null, null);
        var response = controller.generateContent(request);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().success());
    }

    @Test
    void testPlanCampaign() {
        when(orchestrator.planCampaign(any())).thenReturn(
            new CampaignResponse("c1", "Name", "launch", "PLANNING", "Obj", null, null, 0, List.of(), List.of(), List.of(), LocalDateTime.now()));
        var request = new CampaignRequest("Name", "launch", "Obj", null, null, null, 0, null, null);
        var response = controller.planCampaign(request);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testAnalyzeSEO() {
        when(orchestrator.analyzeSEO(any())).thenReturn(
            new SEOResponse("url", "kw", 100, 50.0, 5, List.of(),
                new SEOResponse.SEOChecklistResponse(80.0, List.of(), List.of(), List.of()), 100, "en"));
        var request = new SEORequest("url", "kw", "en", "article", null);
        var response = controller.analyzeSEO(request);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testAnalyzeAEOGEO() {
        when(orchestrator.analyzeAEOGEO(any())).thenReturn(
            new AEOGEOResponse("q", 80.0, 70.0, List.of(), List.of(), "snippet", List.of()));
        var request = new AEOGEORequest("q", "content", "en");
        var response = controller.analyzeAEOGEO(request);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testGenerateSocialPost() {
        when(orchestrator.generateSocialPost(any())).thenReturn(
            new SocialMediaResponse("p1", "ig", "Content", List.of(), "IMAGE", null, LocalDateTime.now(), List.of()));
        var request = new SocialMediaRequest("ig", "topic", "IMAGE", null, null, null, null);
        var response = controller.generateSocialPost(request);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testGenerateEmail() {
        when(orchestrator.generateEmail(any())).thenReturn(
            new EmailResponse("e1", "Subj", "Pre", "<html>", "Text", "promo", List.of()));
        var request = new EmailRequest("Subj", "promo", null, null, null, null);
        var response = controller.generateEmail(request);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testGenerateWhatsApp() {
        when(orchestrator.generateWhatsApp(any())).thenReturn(
            new WhatsAppResponse("w1", "tpl", "Body", "text", "all"));
        var request = new WhatsAppRequest("text", "Body", null, null, null);
        var response = controller.generateWhatsApp(request);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testAnalyzeCampaign() {
        when(orchestrator.analyzeCampaign(any())).thenReturn(
            new AnalyticsResponse("c1", Map.of(), Map.of(), List.of(), List.of()));
        var request = new AnalyticsRequest("c1", null, null);
        var response = controller.analyzeCampaign(request);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testCheckBrand() {
        when(orchestrator.checkBrand(any())).thenReturn(
            new BrandCheckResponse(90.0, List.of(), List.of(), true));
        var request = new BrandCheckRequest("Content", null, null);
        var response = controller.checkBrand(request);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testGetGrowthRecommendations() {
        when(orchestrator.getGrowthRecommendations(any(), any())).thenReturn(
            new GrowthRecommendationResponse(List.of(), "Summary"));
        var response = controller.getGrowthRecommendations("home-growers", "beginners");
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testGenerateBrief() {
        when(orchestrator.generateBrief(any())).thenReturn(
            new MarketingBriefResponse("b1", "Title", "Obj", "Aud", "Msg", List.of(), List.of(), "4w", List.of(), "Metrics"));
        var request = new MarketingBriefRequest("Obj", "Aud", null, null, 0, null);
        var response = controller.generateBrief(request);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testHealth() {
        var response = controller.health();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().success());
    }
}
