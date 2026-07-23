package com.sporekart.executive.copilot.engine;

import com.sporekart.executive.copilot.dto.ExecutiveQueryRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NaturalLanguageEngineTest {

    private NaturalLanguageEngine engine;

    @BeforeEach
    void setUp() {
        engine = new NaturalLanguageEngine();
    }

    @Test
    void processQuery_companyPerformance_shouldReturnValidResponse() {
        var request = new ExecutiveQueryRequest("How is the company doing?", "company_performance", null, null, null);
        var response = engine.processQuery(request);
        assertNotNull(response);
        assertNotNull(response.queryId());
        assertNotNull(response.executiveSummary());
        assertNotNull(response.insights());
        assertFalse(response.insights().isEmpty());
    }

    @Test
    void processQuery_companyPerformance_shouldHaveData() {
        var request = new ExecutiveQueryRequest("How is the company doing?", "company_performance", null, null, null);
        var response = engine.processQuery(request);
        assertTrue(response.data().containsKey("overallHealth"));
        assertEquals(82.5, response.data().get("overallHealth"));
        assertTrue(response.processingTimeMs() >= 0);
    }

    @Test
    void processQuery_biggestRisk_shouldReturnValidResponse() {
        var request = new ExecutiveQueryRequest("What is our biggest risk?", "biggest_risk", null, null, null);
        var response = engine.processQuery(request);
        assertNotNull(response);
        assertTrue(response.data().containsKey("topRisk"));
        assertEquals(55.0, response.data().get("riskScore"));
    }

    @Test
    void processQuery_weeklyFocus_shouldReturnValidResponse() {
        var request = new ExecutiveQueryRequest("What should we focus on this week?", "weekly_focus", null, null, null);
        var response = engine.processQuery(request);
        assertNotNull(response);
        assertTrue(response.data().containsKey("priority1"));
        assertNotNull(response.recommendations());
        assertFalse(response.recommendations().isEmpty());
    }

    @Test
    void processQuery_revenueDrop_shouldReturnValidResponse() {
        var request = new ExecutiveQueryRequest("Why did revenue drop?", "revenue_drop", null, null, null);
        var response = engine.processQuery(request);
        assertNotNull(response);
        assertTrue(response.data().containsKey("dropPct"));
        assertEquals(3.8, response.data().get("dropPct"));
    }

    @Test
    void processQuery_expansion_shouldReturnValidResponse() {
        var request = new ExecutiveQueryRequest("Should we expand?", "expansion", null, null, null);
        var response = engine.processQuery(request);
        assertNotNull(response);
        assertTrue(response.data().containsKey("recommendedMarket"));
        assertEquals(185.0, response.data().get("expectedROI"));
    }

    @Test
    void processQuery_training_shouldReturnValidResponse() {
        var request = new ExecutiveQueryRequest("Can we launch another training batch?", "training", null, null, null);
        var response = engine.processQuery(request);
        assertNotNull(response);
        assertTrue(response.data().containsKey("currentBatches"));
        assertEquals(6, response.data().get("currentBatches"));
    }

    @Test
    void processQuery_profitability_shouldReturnValidResponse() {
        var request = new ExecutiveQueryRequest("How is profitability?", "profitability", null, null, null);
        var response = engine.processQuery(request);
        assertNotNull(response);
        assertTrue(response.data().containsKey("netMargin"));
        assertEquals(24.0, response.data().get("netMargin"));
        assertEquals("IMPROVING", response.data().get("trend"));
    }

    @Test
    void processQuery_default_shouldReturnFallback() {
        var request = new ExecutiveQueryRequest("Some random query", "unknown_intent", null, null, null);
        var response = engine.processQuery(request);
        assertNotNull(response);
        assertNotNull(response.executiveSummary());
        assertTrue(response.executiveSummary().contains("Some random query"));
    }

    @Test
    void processQuery_howIsCompany_shouldWork() {
        var request = new ExecutiveQueryRequest("How is company?", "how_is_company", null, null, null);
        var response = engine.processQuery(request);
        assertNotNull(response);
        assertTrue(response.data().containsKey("overallHealth"));
    }

    @Test
    void processQuery_riskAnalysis_shouldWork() {
        var request = new ExecutiveQueryRequest("Analyze risks", "risk_analysis", null, null, null);
        var response = engine.processQuery(request);
        assertNotNull(response);
        assertTrue(response.data().containsKey("topRisk"));
    }

    @Test
    void analyzeQuery_shouldReturnDomainObject() {
        var query = engine.analyzeQuery("How is the company?", "company_performance");
        assertNotNull(query);
        assertNotNull(query.queryId());
        assertEquals("How is the company?", query.originalQuery());
        assertNotNull(query.insights());
    }
}
