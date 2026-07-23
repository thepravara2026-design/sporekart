package com.sporekart.marketing.copilot.engine;

import com.sporekart.marketing.copilot.dto.AnalyticsRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AnalyticsEngineTest {

    @InjectMocks
    private AnalyticsEngine analyticsEngine;

    @Test
    void testAnalyzeCampaign() {
        var request = new AnalyticsRequest("camp-123", "2026-01-01", "2026-03-31");
        var response = analyticsEngine.analyzeCampaign(request);
        assertNotNull(response);
        assertEquals("camp-123", response.campaignId());
        assertNotNull(response.metrics());
        assertTrue(response.metrics().containsKey("totalImpressions"));
        assertNotNull(response.channelBreakdown());
        assertFalse(response.insights().isEmpty());
        assertFalse(response.recommendations().isEmpty());
    }

    @Test
    void testGetAggregatedAnalytics() {
        var analytics = analyticsEngine.getAggregatedAnalytics(List.of("camp-1", "camp-2"));
        assertNotNull(analytics);
        assertTrue(analytics.totalRevenue() > 0);
        assertTrue(analytics.overallROAS() > 0);
        assertNotNull(analytics.channelPerformance());
        assertFalse(analytics.channelPerformance().isEmpty());
    }

    @Test
    void testGetAggregatedAnalyticsEmpty() {
        var analytics = analyticsEngine.getAggregatedAnalytics(List.of());
        assertNotNull(analytics);
    }

    @Test
    void testCalculateROAS() {
        var roas = analyticsEngine.calculateROAS(100000.0, 20000.0);
        assertEquals(5.0, roas, 0.01);
    }

    @Test
    void testCalculateROASWithZeroSpend() {
        var roas = analyticsEngine.calculateROAS(100000.0, 0.0);
        assertEquals(0.0, roas, 0.01);
    }

    @Test
    void testCalculateCAC() {
        var cac = analyticsEngine.calculateCAC(50000.0, 100);
        assertEquals(500.0, cac, 0.01);
    }

    @Test
    void testCalculateCACWithZeroCustomers() {
        var cac = analyticsEngine.calculateCAC(50000.0, 0);
        assertEquals(0.0, cac, 0.01);
    }

    @Test
    void testGenerateDashboardData() {
        var data = analyticsEngine.generateDashboardData("camp-123");
        assertNotNull(data);
        assertTrue(data.containsKey("campaignId"));
        assertTrue(data.containsKey("metrics"));
        assertTrue(data.containsKey("roasTrend"));
    }
}
