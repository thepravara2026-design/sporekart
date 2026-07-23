package com.sporekart.admin.engine;

import com.sporekart.admin.domain.BusinessInsight;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BusinessInsightsEngineTest {

    private BusinessInsightsEngine engine;

    @BeforeEach
    void setUp() {
        engine = new BusinessInsightsEngine();
    }

    @Test
    void generateDailySummaryShouldReturnInsights() {
        BusinessInsight insight = engine.generateDailySummary();

        assertNotNull(insight);
        assertNotNull(insight.summary());
        assertFalse(insight.summary().isBlank());
        assertNotNull(insight.keyMetrics());
        assertFalse(insight.keyMetrics().isEmpty());
        assertNotNull(insight.trends());
        assertNotNull(insight.recommendations());
    }

    @Test
    void getSalesInsightsShouldIdentifyTrends() {
        List<BusinessInsight> insights = engine.getSalesInsights();

        assertNotNull(insights);
        assertFalse(insights.isEmpty());
        for (BusinessInsight insight : insights) {
            assertNotNull(insight.summary());
            assertNotNull(insight.keyMetrics());
            assertTrue(insight.keyMetrics().containsKey("currentValue"));
            assertTrue(insight.keyMetrics().containsKey("previousValue"));
            assertTrue(insight.keyMetrics().containsKey("changePercentage"));
        }
    }

    @Test
    void getInventoryInsightsShouldFlagIssues() {
        List<BusinessInsight> insights = engine.getInventoryInsights();

        assertNotNull(insights);
        assertFalse(insights.isEmpty());
        boolean hasLowStockWarning = insights.stream()
            .anyMatch(i -> i.summary().toLowerCase().contains("low") || i.summary().toLowerCase().contains("stock"));
        assertTrue(hasLowStockWarning);
    }

    @Test
    void getRiskIndicatorsShouldContainWarnings() {
        List<BusinessInsight> risks = engine.getRiskIndicators();

        assertNotNull(risks);
        assertFalse(risks.isEmpty());
        for (BusinessInsight risk : risks) {
            assertNotNull(risk.risks());
            assertFalse(risk.risks().isEmpty());
            assertTrue(risk.risks().stream().anyMatch(r -> !r.isBlank()));
        }
    }

    @Test
    void getActionRecommendationsAreActionable() {
        List<String> recommendations = engine.getActionRecommendations();

        assertNotNull(recommendations);
        assertFalse(recommendations.isEmpty());
        for (String rec : recommendations) {
            assertNotNull(rec);
            assertFalse(rec.isBlank());
            assertTrue(rec.length() > 10);
        }
    }

    @Test
    void insightsHaveProperMetricComparisons() {
        List<BusinessInsight> insights = engine.getSalesInsights();

        for (BusinessInsight insight : insights) {
            Map<String, Object> metrics = insight.keyMetrics();
            double current = ((Number) metrics.get("currentValue")).doubleValue();
            double previous = ((Number) metrics.get("previousValue")).doubleValue();
            double change = ((Number) metrics.get("changePercentage")).doubleValue();

            if (previous > 0) {
                double expectedChange = ((current - previous) / previous) * 100;
                assertEquals(expectedChange, change, 0.01);
            }
        }
    }
}
