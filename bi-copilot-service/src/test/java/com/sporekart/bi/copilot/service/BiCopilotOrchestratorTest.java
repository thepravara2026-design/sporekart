package com.sporekart.bi.copilot.service;

import com.sporekart.bi.copilot.domain.*;
import com.sporekart.bi.copilot.dto.*;
import com.sporekart.bi.copilot.engine.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BiCopilotOrchestratorTest {

    @Mock private RevenueAnalyticsEngine revenueEngine;
    @Mock private CustomerAnalyticsEngine customerEngine;
    @Mock private ProductAnalyticsEngine productEngine;
    @Mock private InventoryAnalyticsEngine inventoryEngine;
    @Mock private TrainingAnalyticsEngine trainingEngine;
    @Mock private ExecutiveDashboardEngine dashboardEngine;
    @Mock private ForecastingEngine forecastingEngine;
    @Mock private DecisionSupportEngine decisionEngine;
    @Mock private RiskDetectionEngine riskEngine;
    @Mock private NaturalLanguageAnalyticsEngine nlEngine;
    @Mock private VisualizationEngine vizEngine;
    @Mock private BusinessInsightsEngine insightsEngine;

    @InjectMocks
    private BiCopilotOrchestrator orchestrator;

    @Test
    void processMessageWithDashboardIntentRoutesToDashboardEngine() {
        when(dashboardEngine.generateDailySnapshot()).thenReturn(mock(ExecutiveSummary.class));
        when(dashboardEngine.getDashboardVisualizations()).thenReturn(List.of(mock(VisualizationConfig.class)));

        ChatResponse response = orchestrator.processMessage(
                new ChatRequest("show dashboard", null, null, null, null));

        assertNotNull(response);
        verify(dashboardEngine).generateDailySnapshot();
    }

    @Test
    void processMessageWithRevenueIntentRoutesToRevenueEngine() {
        when(revenueEngine.getRevenueSummary("current"))
                .thenReturn(mock(RevenueAnalytics.class));

        ChatResponse response = orchestrator.processMessage(
                new ChatRequest("show revenue", null, null, null, "revenue"));

        assertNotNull(response);
        verify(revenueEngine).getRevenueSummary("current");
    }

    @Test
    void processMessageWithForecastIntentRoutesToForecastingEngine() {
        when(forecastingEngine.autoForecast(anyString(), anyString(), anyInt()))
                .thenReturn(mock(BusinessForecast.class));

        ChatResponse response = orchestrator.processMessage(
                new ChatRequest("predict sales", null, null, null, "forecast"));

        assertNotNull(response);
        verify(forecastingEngine).autoForecast(anyString(), anyString(), anyInt());
    }

    @Test
    void processMessageWithRecommendationIntentRoutesToDecisionEngine() {
        when(decisionEngine.generateRecommendations(anyString(), anyString()))
                .thenReturn(List.of(mock(DecisionRecommendation.class)));

        ChatResponse response = orchestrator.processMessage(
                new ChatRequest("what should I do", null, null, null, "recommendations"));

        assertNotNull(response);
        verify(decisionEngine).generateRecommendations(anyString(), anyString());
    }

    @Test
    void processMessageWithRiskIntentRoutesToRiskEngine() {
        when(riskEngine.detectRisks(anyString()))
                .thenReturn(List.of(mock(RiskAlert.class)));

        ChatResponse response = orchestrator.processMessage(
                new ChatRequest("what are the risks", null, null, null, "risks"));

        assertNotNull(response);
        verify(riskEngine).detectRisks(anyString());
    }

    @Test
    void processMessageWithQueryIntentRoutesToNLEngine() {
        when(nlEngine.answerQuery(anyString(), anyBoolean(), anyBoolean()))
                .thenReturn(mock(NaturalLanguageQueryResponse.class));

        ChatResponse response = orchestrator.processMessage(
                new ChatRequest("show revenue of spawn products", null, null, null, null));

        assertNotNull(response);
        verify(nlEngine).answerQuery(anyString(), anyBoolean(), anyBoolean());
    }

    @Test
    void processMessageWithHealthScoreIntentRoutesToDashboardEngine() {
        when(dashboardEngine.calculateCompanyHealthScore(anyString()))
                .thenReturn(mock(CompanyHealthScore.class));

        ChatResponse response = orchestrator.processMessage(
                new ChatRequest("health score", null, null, null, "health"));

        assertNotNull(response);
        verify(dashboardEngine).calculateCompanyHealthScore(anyString());
    }

    @Test
    void getExecutiveDashboardReturnsDashboardResponse() {
        when(dashboardEngine.generateDailySnapshot()).thenReturn(mock(ExecutiveSummary.class));
        when(dashboardEngine.getDashboardVisualizations())
                .thenReturn(List.of(mock(VisualizationConfig.class)));

        DashboardResponse response = orchestrator.getExecutiveDashboard("daily");

        assertNotNull(response);
        assertNotNull(response.summary());
        assertNotNull(response.visualizations());
        verify(dashboardEngine).generateDailySnapshot();
    }

    @Test
    void getRevenueAnalyticsDelegatesToRevenueEngine() {
        when(revenueEngine.getRevenueSummary("current"))
                .thenReturn(mock(RevenueAnalytics.class));

        RevenueAnalytics result = orchestrator.getRevenueAnalytics("current");

        assertNotNull(result);
        verify(revenueEngine).getRevenueSummary("current");
    }

    @Test
    void getForecastDelegatesToForecastingEngine() {
        when(forecastingEngine.autoForecast("revenue", "current", 6))
                .thenReturn(mock(BusinessForecast.class));

        BusinessForecast result = orchestrator.getForecast("revenue", "current", 6, "auto");

        assertNotNull(result);
        verify(forecastingEngine).autoForecast("revenue", "current", 6);
    }

    @Test
    void getRecommendationsDelegatesToDecisionEngine() {
        when(decisionEngine.generateRecommendations("revenue", "current"))
                .thenReturn(List.of(mock(DecisionRecommendation.class)));

        List<DecisionRecommendation> recs = orchestrator.getRecommendations("revenue", "current");

        assertNotNull(recs);
        assertFalse(recs.isEmpty());
        verify(decisionEngine).generateRecommendations("revenue", "current");
    }

    @Test
    void getRisksDelegatesToRiskEngine() {
        when(riskEngine.detectRisks("current"))
                .thenReturn(List.of(mock(RiskAlert.class)));

        List<RiskAlert> risks = orchestrator.getRisks("current");

        assertNotNull(risks);
        assertFalse(risks.isEmpty());
        verify(riskEngine).detectRisks("current");
    }

    @Test
    void answerQueryDelegatesToNLEngine() {
        when(nlEngine.answerQuery("test query", true, true))
                .thenReturn(mock(NaturalLanguageQueryResponse.class));

        NaturalLanguageQueryResponse response = orchestrator.answerQuery("test query", true, true);

        assertNotNull(response);
        verify(nlEngine).answerQuery("test query", true, true);
    }

    @Test
    void getHealthScoreDelegatesToDashboardEngine() {
        when(dashboardEngine.calculateCompanyHealthScore("current"))
                .thenReturn(mock(CompanyHealthScore.class));

        CompanyHealthScore score = orchestrator.getHealthScore("current");

        assertNotNull(score);
        verify(dashboardEngine).calculateCompanyHealthScore("current");
    }
}
