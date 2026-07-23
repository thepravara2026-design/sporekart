package com.sporekart.executive.copilot.engine;

import com.sporekart.executive.copilot.dto.MarketRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MarketIntelligenceEngineTest {

    private MarketIntelligenceEngine engine;

    @BeforeEach
    void setUp() {
        engine = new MarketIntelligenceEngine();
    }

    @Test
    void analyzeMarket_shouldReturnValidResponse() {
        var request = new MarketRequest("mushroom_cultivation", "India");
        var response = engine.analyzeMarket(request);
        assertNotNull(response);
        assertNotNull(response.segment());
        assertTrue(response.marketSize() > 0);
        assertTrue(response.growthRate() > 0);
    }

    @Test
    void analyzeMarket_shouldHaveCompetitors() {
        var request = new MarketRequest("mushroom_cultivation", "India");
        var response = engine.analyzeMarket(request);
        assertFalse(response.competitors().isEmpty());
        assertEquals(4, response.competitors().size());
    }

    @Test
    void analyzeMarket_shouldHaveTrends() {
        var request = new MarketRequest("mushroom_cultivation", "India");
        var response = engine.analyzeMarket(request);
        assertFalse(response.trends().isEmpty());
    }

    @Test
    void analyzeMarket_shouldHaveOpportunities() {
        var request = new MarketRequest("mushroom_cultivation", "India");
        var response = engine.analyzeMarket(request);
        assertFalse(response.opportunities().isEmpty());
    }

    @Test
    void getDetailedIntelligence_shouldReturnValidIntelligence() {
        var intelligence = engine.getDetailedIntelligence("mushroom_cultivation");
        assertNotNull(intelligence);
        assertNotNull(intelligence.segment());
        assertTrue(intelligence.marketSize() > 0);
        assertNotNull(intelligence.competitors());
    }

    @Test
    void benchmarkCompetitors_shouldReturnValidBenchmark() {
        var benchmark = engine.benchmarkCompetitors("overall");
        assertNotNull(benchmark);
        assertNotNull(benchmark.get("metric"));
        assertNotNull(benchmark.get("sporekart"));
        assertNotNull(benchmark.get("topPerformer"));
    }

    @Test
    void benchmarkCompetitors_shouldHaveSporekartRank() {
        var benchmark = engine.benchmarkCompetitors("quality");
        var sporekart = (java.util.Map<String, Object>) benchmark.get("sporekart");
        assertEquals(85.0, sporekart.get("value"));
        assertEquals(2, sporekart.get("rank"));
    }

    @Test
    void getIndustryTrends_shouldReturnTrends() {
        var trends = engine.getIndustryTrends("mushroom_cultivation");
        assertNotNull(trends);
        assertFalse(trends.isEmpty());
        assertTrue(trends.size() >= 3);
    }

    @Test
    void getIndustryTrends_shouldContainRelevantTrends() {
        var trends = engine.getIndustryTrends("mushroom_cultivation");
        assertTrue(trends.stream().anyMatch(t -> t.contains("fermentation") || t.contains("farming")));
    }
}
