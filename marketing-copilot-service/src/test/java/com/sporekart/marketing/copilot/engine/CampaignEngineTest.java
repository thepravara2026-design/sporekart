package com.sporekart.marketing.copilot.engine;

import com.sporekart.marketing.copilot.domain.Campaign;
import com.sporekart.marketing.copilot.dto.CampaignRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CampaignEngineTest {

    @InjectMocks
    private CampaignEngine campaignEngine;

    @Test
    void testPlanCampaign() {
        var request = new CampaignRequest("Summer Sale", "launch", "Increase sales", "home-growers",
            LocalDate.now(), LocalDate.now().plusDays(30), 50000.0, List.of("email", "social", "paid_ads"), null);
        var response = campaignEngine.planCampaign(request);
        assertNotNull(response);
        assertEquals("Summer Sale", response.name());
        assertEquals("PLANNING", response.status());
        assertFalse(response.recommendations().isEmpty());
    }

    @Test
    void testCreateCampaignRecord() {
        var request = new CampaignRequest("Test Campaign", "launch", "Testing", "general",
            LocalDate.now(), LocalDate.now().plusDays(7), 10000.0, null, null);
        var campaign = campaignEngine.createCampaignRecord(request);
        assertNotNull(campaign);
        assertEquals("Test Campaign", campaign.name());
        assertEquals(Campaign.CampaignStatus.PLANNING, campaign.status());
    }

    @Test
    void testGenerateRecommendations() {
        var request = new CampaignRequest("Launch", "launch", "Product launch", "all",
            null, null, 100000.0, List.of("email"), null);
        var recs = campaignEngine.generateRecommendations(request);
        assertFalse(recs.isEmpty());
        assertTrue(recs.stream().anyMatch(r -> r.contains("A/B")));
    }

    @Test
    void testGenerateRecommendationsForSeasonal() {
        var request = new CampaignRequest("Seasonal", "seasonal", "Holiday", "all",
            null, null, 50000.0, List.of("email"), null);
        var recs = campaignEngine.generateRecommendations(request);
        assertTrue(recs.stream().anyMatch(r -> r.contains("urgency")));
    }

    @Test
    void testOptimizeBudget() {
        var campaign = new Campaign("c1", "Test", Campaign.CampaignType.LAUNCH, Campaign.CampaignStatus.PLANNING,
            "Test", "all", null, null, 100000.0, List.of(), List.of(), List.of(), null, "system");
        var budget = campaignEngine.optimizeBudget(campaign, 100000.0);
        assertEquals(5, budget.size());
    }

    @Test
    void testSimulatePerformance() {
        var campaign = new Campaign("c1", "Test", Campaign.CampaignType.LAUNCH, Campaign.CampaignStatus.ACTIVE,
            "Test", "all", null, null, 50000.0, List.of(), List.of(), List.of(), null, "system");
        var metric = campaignEngine.simulatePerformance(campaign);
        assertNotNull(metric);
        assertTrue(metric.impressions() > 0);
        assertTrue(metric.revenue() > 0);
    }
}
