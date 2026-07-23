package com.sporekart.bi.copilot.engine;

import com.sporekart.bi.copilot.domain.BusinessInsight;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BusinessInsightsEngineTest {

    @InjectMocks
    private BusinessInsightsEngine engine;

    @Test
    void generateRevenueInsightsReturnsRevenueInsights() {
        List<BusinessInsight> insights = engine.generateRevenueInsights("current");
        assertNotNull(insights);
        assertFalse(insights.isEmpty());
        insights.forEach(i -> assertEquals("revenue", i.category()));
    }

    @Test
    void generateCustomerInsightsReturnsCustomerInsights() {
        List<BusinessInsight> insights = engine.generateCustomerInsights("current");
        assertNotNull(insights);
        assertFalse(insights.isEmpty());
        insights.forEach(i -> assertEquals("customer", i.category()));
    }

    @Test
    void generateInventoryInsightsReturnsInventoryInsights() {
        List<BusinessInsight> insights = engine.generateInventoryInsights("current");
        assertNotNull(insights);
        assertFalse(insights.isEmpty());
        insights.forEach(i -> assertEquals("inventory", i.category()));
    }

    @Test
    void generateTrainingInsightsReturnsTrainingInsights() {
        List<BusinessInsight> insights = engine.generateTrainingInsights("current");
        assertNotNull(insights);
        assertFalse(insights.isEmpty());
        insights.forEach(i -> assertEquals("training", i.category()));
    }

    @Test
    void generateCrossDomainInsightsReturnsMultiDomainInsights() {
        List<BusinessInsight> insights = engine.generateCrossDomainInsights("current");
        assertNotNull(insights);
        assertFalse(insights.isEmpty());
        insights.forEach(i -> assertEquals("cross_domain", i.category()));
    }

    @Test
    void getCriticalInsightsReturnsHighSeverityInsights() {
        List<BusinessInsight> insights = engine.getCriticalInsights("current");
        assertNotNull(insights);
        assertFalse(insights.isEmpty());
        insights.forEach(i -> assertTrue(
                "CRITICAL".equalsIgnoreCase(i.severity())
                        || "HIGH".equalsIgnoreCase(i.severity())));
    }

    @Test
    void generateDailyBriefingReturnsBriefingInsights() {
        List<BusinessInsight> briefing = engine.generateDailyBriefing();
        assertNotNull(briefing);
        assertFalse(briefing.isEmpty());
    }

    @Test
    void generateRevenueInsightsIncludesActionItems() {
        List<BusinessInsight> insights = engine.generateRevenueInsights("current");
        insights.forEach(i -> {
            assertNotNull(i.title());
            assertNotNull(i.description());
            assertNotNull(i.actionItems());
        });
    }

    @Test
    void generateCrossDomainInsightsShowsCorrelations() {
        List<BusinessInsight> insights = engine.generateCrossDomainInsights("current");
        insights.forEach(i -> assertTrue(i.confidenceScore() > 0));
    }
}
