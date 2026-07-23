package com.sporekart.operations.copilot.engine;

import com.sporekart.operations.copilot.dto.ForecastRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class DemandPlanningEngineTest {

    @InjectMocks
    private DemandPlanningEngine demandPlanningEngine;

    @Test
    void testForecastDemand() {
        var request = new ForecastRequest("MUSH-001", "West", 30);
        var response = demandPlanningEngine.forecastDemand(request);
        assertNotNull(response);
        assertEquals("MUSH-001", response.productId());
        assertTrue(response.predictedDemand() > 0);
        assertTrue(response.confidence() > 0);
        assertNotNull(response.weeklyDemand());
        assertFalse(response.recommendations().isEmpty());
    }

    @Test
    void testForecastDemandWithDefaults() {
        var request = new ForecastRequest("MUSH-002", null, null);
        var response = demandPlanningEngine.forecastDemand(request);
        assertNotNull(response);
        assertTrue(response.predictedDemand() > 0);
    }

    @Test
    void testGenerateDetailedForecast() {
        var forecast = demandPlanningEngine.generateDetailedForecast("MUSH-001", "West");
        assertNotNull(forecast);
        assertNotNull(forecast.forecastId());
        assertNotNull(forecast.demandCurve());
        assertTrue(forecast.predictedDemand() > 0);
        assertNotNull(forecast.regionalDemand());
        assertFalse(forecast.regionalDemand().isEmpty());
        assertNotNull(forecast.festivalDemand());
    }

    @Test
    void testCalculateSeasonalFactor() {
        var factor = demandPlanningEngine.calculateSeasonalFactor("MUSH-001", "diwali");
        assertEquals(2.5, factor, 0.01);
    }

    @Test
    void testCalculateSeasonalFactorDefault() {
        var factor = demandPlanningEngine.calculateSeasonalFactor("MUSH-001", "unknown");
        assertEquals(1.0, factor, 0.01);
    }

    @Test
    void testAnalyzeDemandTrends() {
        var trends = demandPlanningEngine.analyzeDemandTrends("Spawn");
        assertNotNull(trends);
        assertTrue(trends.containsKey("growthRate"));
    }

    @Test
    void testPredictInventoryBuffer() {
        var buffer = demandPlanningEngine.predictInventoryBuffer(1000, 7, 95);
        assertTrue(buffer > 0);
    }
}
