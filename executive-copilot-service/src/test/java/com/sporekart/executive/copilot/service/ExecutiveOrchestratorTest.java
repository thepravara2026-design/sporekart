package com.sporekart.executive.copilot.service;

import com.sporekart.executive.copilot.domain.*;
import com.sporekart.executive.copilot.dto.*;
import com.sporekart.executive.copilot.engine.*;
import com.sporekart.executive.copilot.infrastructure.monitoring.ExecutiveMetricsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExecutiveOrchestratorTest {

    @Mock private CompanyHealthEngine companyHealthEngine;
    @Mock private FinancialIntelligenceEngine financialIntelligenceEngine;
    @Mock private BusinessForecastingEngine businessForecastingEngine;
    @Mock private StrategicDecisionEngine strategicDecisionEngine;
    @Mock private RiskIntelligenceEngine riskIntelligenceEngine;
    @Mock private MarketIntelligenceEngine marketIntelligenceEngine;
    @Mock private BoardReportEngine boardReportEngine;
    @Mock private ExecutiveDashboardEngine executiveDashboardEngine;
    @Mock private NaturalLanguageEngine naturalLanguageEngine;
    @Mock private PerformanceAnalyticsEngine performanceAnalyticsEngine;
    @Mock private ExecutiveMetricsService metricsService;

    private ExecutiveOrchestrator orchestrator;

    @BeforeEach
    void setUp() {
        orchestrator = new ExecutiveOrchestrator(
            companyHealthEngine, financialIntelligenceEngine, businessForecastingEngine,
            strategicDecisionEngine, riskIntelligenceEngine, marketIntelligenceEngine,
            boardReportEngine, executiveDashboardEngine, naturalLanguageEngine,
            performanceAnalyticsEngine, metricsService
        );
    }

    @Test
    void processQuery_shouldDelegateToNLEngine() {
        var request = new ExecutiveQueryRequest("How is the company?", "company_performance", null, null, null);
        var expected = new ExecutiveQueryResponse("q1", "company_performance", "Summary", Map.of(), List.of(), List.of(), 100L);
        when(naturalLanguageEngine.processQuery(request)).thenReturn(expected);

        var result = orchestrator.processQuery(request);

        assertEquals(expected, result);
        verify(metricsService).recordQueryLatency(anyLong());
        verify(metricsService).recordPromptUsage();
    }

    @Test
    void getDashboard_shouldDelegateToDashboardEngine() {
        var dashboard = new DashboardResponse("Summary",
            new DashboardResponse.CompanyHealth(82.5, 2.5, "LOW", Map.of()),
            Map.of(), List.of(), new DashboardResponse.RiskSummary(42.5, "MODERATE", 0, 2),
            List.of(), List.of());
        when(executiveDashboardEngine.getDashboard()).thenReturn(dashboard);

        var result = orchestrator.getDashboard();

        assertEquals(dashboard, result);
        verify(metricsService).recordDashboardUsage("executive");
    }

    @Test
    void getTodaySummary_shouldDelegate() {
        when(executiveDashboardEngine.getTodaySummary()).thenReturn(Map.of("ordersToday", 85));
        var result = orchestrator.getTodaySummary();
        assertEquals(85, result.get("ordersToday"));
        verify(metricsService).recordDashboardUsage("today");
    }

    @Test
    void weeklyAndMonthlyReports_shouldDelegate() {
        when(executiveDashboardEngine.getWeeklyReport()).thenReturn(Map.of("week", "Week 30"));
        when(executiveDashboardEngine.getMonthlyReport()).thenReturn(Map.of("month", "July 2026"));

        assertEquals("Week 30", orchestrator.getWeeklyReport().get("week"));
        assertEquals("July 2026", orchestrator.getMonthlyReport().get("month"));

        verify(metricsService).recordDashboardUsage("weekly");
        verify(metricsService).recordDashboardUsage("monthly");
    }

    @Test
    void generateReport_shouldDelegate() {
        var request = new ReportRequest("quarterly", "Q2");
        var response = new ReportResponse("r1", "Report", "Q2", "Summary", Map.of(), Map.of(), List.of(), List.of(), Map.of(), "Outlook");
        when(boardReportEngine.generateReport(request)).thenReturn(response);

        var result = orchestrator.generateReport(request);
        assertEquals(response, result);
        verify(metricsService).recordPromptUsage();
    }

    @Test
    void generateBoardReport_shouldDelegate() {
        var report = mock(BoardReport.class);
        when(boardReportEngine.generateBoardReport("Q2")).thenReturn(report);
        assertEquals(report, orchestrator.generateBoardReport("Q2"));
    }

    @Test
    void forecast_shouldDelegate() {
        var request = new ForecastRequest("revenue", "Q3", 6);
        var response = new ForecastResponse("revenue", 6, List.of(1.0), List.of(0.8), List.of(1.2), 87.5, Map.of(), List.of());
        when(businessForecastingEngine.forecast(request)).thenReturn(response);

        var result = orchestrator.forecast(request);
        assertEquals(response, result);
        verify(metricsService).recordForecastExecution(anyLong());
    }

    @Test
    void generateDetailedForecast_shouldDelegate() {
        var forecast = mock(BusinessForecast.class);
        when(businessForecastingEngine.generateDetailedForecast("revenue", 12)).thenReturn(forecast);
        assertEquals(forecast, orchestrator.generateDetailedForecast("revenue", 12));
    }

    @Test
    void forecastRevenueAndOrders_shouldDelegate() {
        when(businessForecastingEngine.forecastRevenue(12)).thenReturn(Map.of("revenue", 100.0));
        when(businessForecastingEngine.forecastOrders(6)).thenReturn(Map.of("orders", 50.0));

        assertEquals(100.0, orchestrator.forecastRevenue(12).get("revenue"));
        assertEquals(50.0, orchestrator.forecastOrders(6).get("orders"));
    }

    @Test
    void assessRisks_shouldDelegate() {
        var request = new RiskRequest("all", "all");
        var response = new RiskResponse(42.5, "MEDIUM", List.of(), List.of(), List.of(), List.of());
        when(riskIntelligenceEngine.assessRisks(request)).thenReturn(response);

        var result = orchestrator.assessRisks(request);
        assertEquals(response, result);
    }

    @Test
    void getRiskMatrix_shouldDelegate() {
        var matrix = mock(RiskMatrix.class);
        when(riskIntelligenceEngine.getRiskMatrix()).thenReturn(matrix);
        assertEquals(matrix, orchestrator.getRiskMatrix());
    }

    @Test
    void getTopRisks_and_RevenueDecline_shouldDelegate() {
        when(riskIntelligenceEngine.getTopRisks(5)).thenReturn(Map.of("count", 5));
        when(riskIntelligenceEngine.detectRevenueDeclineRisk()).thenReturn(Map.of("riskDetected", false));

        assertEquals(5, orchestrator.getTopRisks(5).get("count"));
        assertFalse((Boolean) orchestrator.detectRevenueDeclineRisk().get("riskDetected"));
    }

    @Test
    void getHealth_shouldDelegate() {
        var health = new HealthResponse(82.5, 80.0, 2.5, "LOW", List.of(), "Summary");
        when(companyHealthEngine.getHealthReport()).thenReturn(health);

        var result = orchestrator.getHealth();
        assertEquals(health, result);
    }

    @Test
    void calculateOverallHealth_shouldDelegate() {
        var health = mock(CompanyHealth.class);
        when(companyHealthEngine.calculateOverallHealth()).thenReturn(health);
        assertEquals(health, orchestrator.calculateOverallHealth());
    }

    @Test
    void getDimensionHealth_and_Sustainability_and_CashFlow_shouldDelegate() {
        var health = mock(CompanyHealth.class);
        var sustainability = mock(BusinessSustainability.class);
        var cashFlow = mock(CashFlowIndicators.class);

        when(companyHealthEngine.getDimensionHealth("revenue")).thenReturn(health);
        when(companyHealthEngine.assessSustainability()).thenReturn(sustainability);
        when(companyHealthEngine.analyzeCashFlow()).thenReturn(cashFlow);

        assertEquals(health, orchestrator.getDimensionHealth("revenue"));
        assertEquals(sustainability, orchestrator.assessSustainability());
        assertEquals(cashFlow, orchestrator.analyzeCashFlow());
    }

    @Test
    void analyzeFinancials_shouldDelegate() {
        var response = mock(FinancialResponse.class);
        when(financialIntelligenceEngine.analyzeFinancials("revenue", "Q2")).thenReturn(response);
        assertEquals(response, orchestrator.analyzeFinancials("revenue", "Q2"));
    }

    @Test
    void getRevenueBreakdown_and_ProfitAnalysis_and_ROI_and_Trends_shouldDelegate() {
        var breakdown = mock(RevenueBreakdown.class);
        var analysis = mock(ProfitAnalysis.class);

        when(financialIntelligenceEngine.getRevenueBreakdown()).thenReturn(breakdown);
        when(financialIntelligenceEngine.getProfitAnalysis()).thenReturn(analysis);
        when(financialIntelligenceEngine.calculateROI(100.0, 200.0)).thenReturn(100.0);
        when(financialIntelligenceEngine.getFinancialTrends("Q2")).thenReturn(Map.of());

        assertEquals(breakdown, orchestrator.getRevenueBreakdown());
        assertEquals(analysis, orchestrator.getProfitAnalysis());
        assertEquals(100.0, orchestrator.calculateROI(100.0, 200.0));
        assertNotNull(orchestrator.getFinancialTrends("Q2"));
    }

    @Test
    void getStrategicDecision_shouldDelegate() {
        var request = new DecisionRequest("Expand", "expansion", "growth", 100.0, "6m");
        var response = mock(DecisionResponse.class);
        when(strategicDecisionEngine.getRecommendation(request)).thenReturn(response);
        assertEquals(response, orchestrator.getStrategicDecision(request));
    }

    @Test
    void createRecommendation_and_Prioritize_and_Simulate_shouldDelegate() {
        var rec = mock(StrategicRecommendation.class);
        when(strategicDecisionEngine.createRecommendation(anyString(), anyString(), any(), anyDouble(), anyDouble(), anyDouble()))
            .thenReturn(rec);
        when(strategicDecisionEngine.prioritizeRecommendations(anyList())).thenReturn(List.of(rec));
        when(strategicDecisionEngine.simulateDecisionImpact(anyString(), anyDouble())).thenReturn(Map.of());

        assertNotNull(orchestrator.createRecommendation("T", "D", StrategicRecommendation.RecommendationCategory.EXPANSION, 1, 2, 3));
        assertFalse(orchestrator.prioritizeRecommendations(List.of()).isEmpty());
        assertNotNull(orchestrator.simulateDecisionImpact("test", 100.0));
    }

    @Test
    void analyzePerformance_shouldDelegate() {
        var request = new PerformanceRequest("Training", "Q2");
        var response = mock(PerformanceResponse.class);
        when(performanceAnalyticsEngine.analyzePerformance(request)).thenReturn(response);
        assertEquals(response, orchestrator.analyzePerformance(request));
    }

    @Test
    void getDepartmentDetail_and_CrossDeptMetrics_shouldDelegate() {
        var detail = mock(DepartmentPerformance.class);
        when(performanceAnalyticsEngine.getDepartmentDetail("Training")).thenReturn(detail);

        assertEquals(detail, orchestrator.getDepartmentDetail("Training"));
        assertNotNull(orchestrator.getCrossDepartmentalMetrics());
    }

    @Test
    void analyzeMarket_shouldDelegate() {
        var request = new MarketRequest("mushroom", "India");
        var response = mock(MarketResponse.class);
        when(marketIntelligenceEngine.analyzeMarket(request)).thenReturn(response);
        assertEquals(response, orchestrator.analyzeMarket(request));
    }

    @Test
    void getDetailedIntelligence_and_Benchmark_and_Trends_shouldDelegate() {
        var intelligence = mock(MarketIntelligence.class);
        when(marketIntelligenceEngine.getDetailedIntelligence("mushroom")).thenReturn(intelligence);
        when(marketIntelligenceEngine.benchmarkCompetitors("quality")).thenReturn(Map.of());

        assertEquals(intelligence, orchestrator.getDetailedIntelligence("mushroom"));
        assertNotNull(orchestrator.benchmarkCompetitors("quality"));
        assertNotNull(orchestrator.getIndustryTrends("mushroom"));
    }
}
