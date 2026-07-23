package com.sporekart.bi.copilot.engine;

import com.sporekart.bi.copilot.domain.CompanyHealthScore;
import com.sporekart.bi.copilot.domain.ExecutiveSummary;
import com.sporekart.bi.copilot.domain.VisualizationConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ExecutiveDashboardEngineTest {

    @InjectMocks
    private ExecutiveDashboardEngine engine;

    @Test
    void generateDailySnapshotReturnsExecutiveSummary() {
        ExecutiveSummary result = engine.generateDailySnapshot();
        assertNotNull(result);
        assertEquals("DAILY", result.type());
    }

    @Test
    void generateWeeklySummaryReturnsWeeklyReport() {
        ExecutiveSummary result = engine.generateWeeklySummary();
        assertNotNull(result);
        assertEquals("WEEKLY", result.type());
        assertNotNull(result.revenue());
        assertNotNull(result.customers());
    }

    @Test
    void generateMonthlyReportReturnsMonthlyReport() {
        ExecutiveSummary result = engine.generateMonthlyReport();
        assertNotNull(result);
        assertEquals("MONTHLY", result.type());
    }

    @Test
    void generateQuarterlyReviewReturnsQuarterlyReview() {
        ExecutiveSummary result = engine.generateQuarterlyReview();
        assertNotNull(result);
        assertEquals("QUARTERLY", result.type());
        assertNotNull(result.healthScore());
    }

    @Test
    void calculateCompanyHealthScoreReturnsValidScore() {
        CompanyHealthScore score = engine.calculateCompanyHealthScore("current");
        assertNotNull(score);
        assertTrue(score.overall() >= 0);
        assertTrue(score.overall() <= 100);
        assertTrue(score.revenueScore() >= 0);
        assertTrue(score.customerScore() >= 0);
        assertTrue(score.trainingScore() >= 0);
        assertTrue(score.inventoryScore() >= 0);
    }

    @Test
    void getDashboardVisualizationsReturnsVisualizations() {
        List<VisualizationConfig> vizs = engine.getDashboardVisualizations();
        assertNotNull(vizs);
        assertFalse(vizs.isEmpty());
    }

    @Test
    void generateDailySnapshotIncludesRecommendations() {
        ExecutiveSummary result = engine.generateDailySnapshot();
        assertNotNull(result.recommendations());
    }

    @Test
    void generateDailySnapshotIncludesRisks() {
        ExecutiveSummary result = engine.generateDailySnapshot();
        assertNotNull(result.risks());
    }

    @Test
    void generateDailySnapshotIncludesInsights() {
        ExecutiveSummary result = engine.generateDailySnapshot();
        assertNotNull(result.insights());
    }
}
