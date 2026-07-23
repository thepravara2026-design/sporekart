package com.sporekart.bi.copilot.service;

import com.sporekart.bi.copilot.dto.*;
import com.sporekart.bi.copilot.engine.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BiCopilotOrchestratorTest {

    @Mock private RevenueAnalyticsEngine revenueAnalyticsEngine;
    @Mock private CustomerAnalyticsEngine customerAnalyticsEngine;
    @Mock private TrainingAnalyticsEngine trainingAnalyticsEngine;
    @Mock private CultivationAnalyticsEngine cultivationAnalyticsEngine;
    @Mock private CrossCopilotIntelligenceEngine crossCopilotIntelligenceEngine;
    @Mock private TrendDetectionEngine trendDetectionEngine;
    @Mock private AnomalyDetectionEngine anomalyDetectionEngine;
    @Mock private BusinessInsightsEngine businessInsightsEngine;
    @Mock private ForecastingEngine forecastingEngine;
    @Mock private CustomerSegmentationEngine customerSegmentationEngine;
    @Mock private ReportingEngine reportingEngine;
    @Mock private DashboardEngine dashboardEngine;

    private BiCopilotOrchestrator orchestrator;

    @BeforeEach
    void setUp() {
        orchestrator = new BiCopilotOrchestrator(
                revenueAnalyticsEngine, customerAnalyticsEngine, trainingAnalyticsEngine,
                cultivationAnalyticsEngine, crossCopilotIntelligenceEngine, trendDetectionEngine,
                anomalyDetectionEngine, businessInsightsEngine, forecastingEngine,
                customerSegmentationEngine, reportingEngine, dashboardEngine);
    }

    @Test
    void processMessage_shouldReturnResponse() {
        ChatRequest request = new ChatRequest("show revenue", "s1", null, null, null, null, null, null, null);
        when(revenueAnalyticsEngine.getRevenueSummary(any())).thenReturn(null);
        ChatResponse response = orchestrator.processMessage(request);
        assertNotNull(response);
        assertNotNull(response.message());
    }

    @Test
    void processChat_shouldReturnChatResponse() {
        ChatRequest request = new ChatRequest("hello", "s1", null, null, null, null, null, null, null);
        ChatResponse response = orchestrator.processChat(request);
        assertNotNull(response);
    }

    @Test
    void getDashboard_shouldReturnDashboard() {
        when(dashboardEngine.getDefaultDashboard()).thenReturn(
                new DashboardResponse("d1", "Default", List.of(), Map.of(), List.of(), null));
        DashboardResponse response = orchestrator.getDashboard("default");
        assertNotNull(response);
        assertEquals("d1", response.dashboardId());
    }

    @Test
    void queryData_shouldReturnQueryResult() {
        BiQueryRequest request = new BiQueryRequest("revenue", "revenue", Map.of(),
                List.of(), List.of(), null, null, 1, 10);
        BiQueryResponse response = orchestrator.queryData(request);
        assertNotNull(response);
    }

    @Test
    void generateReport_shouldReturnResponse() {
        ReportRequest request = new ReportRequest("revenue", "pdf", null,
                List.of(), List.of(), Map.of(), List.of());
        when(reportingEngine.generateRevenueReport(anyString(), anyMap())).thenReturn(Map.of("reportId", "r1"));
        ReportResponse response = orchestrator.generateReport(request);
        assertNotNull(response);
    }

    @Test
    void getInsights_shouldReturnInsights() {
        when(businessInsightsEngine.generateInsights(anyString(), anyString())).thenReturn(List.of());
        InsightsResponse response = orchestrator.getInsights("revenue", "2025-06");
        assertNotNull(response);
        assertEquals("revenue", response.category());
    }

    @Test
    void getTrends_shouldReturnTrends() {
        when(trendDetectionEngine.getTopTrends(anyInt())).thenReturn(List.of());
        TrendsResponse response = orchestrator.getTrends("revenue", 12);
        assertNotNull(response);
        assertEquals("revenue", response.metric());
    }

    @Test
    void getAnomalies_shouldReturnAnomalies() {
        when(anomalyDetectionEngine.getActiveAnomalies()).thenReturn(List.of());
        AnomaliesResponse response = orchestrator.getAnomalies("2025-06");
        assertNotNull(response);
    }

    @Test
    void forecast_shouldReturnForecast() {
        ForecastRequest request = new ForecastRequest("revenue", "seasonal", 3, Map.of());
        when(forecastingEngine.forecastRevenue(anyInt())).thenReturn(null);
        ForecastResponse response = orchestrator.forecast(request);
        assertNotNull(response);
    }

    @Test
    void getCrossCopilotMetrics_shouldReturnMetrics() {
        when(crossCopilotIntelligenceEngine.getCrossCopilotMetrics()).thenReturn(List.of());
        CrossCopilotMetricsResponse response = orchestrator.getCrossCopilotMetrics();
        assertNotNull(response);
    }

    @Test
    void getCustomerSegments_shouldReturnSegments() {
        when(customerSegmentationEngine.segmentCustomers()).thenReturn(List.of());
        CustomerSegmentsResponse response = orchestrator.getCustomerSegments();
        assertNotNull(response);
    }

    @Test
    void getScheduledReports_shouldReturnList() {
        when(reportingEngine.getScheduledReports()).thenReturn(List.of());
        List<?> reports = orchestrator.getScheduledReports();
        assertNotNull(reports);
        assertTrue(reports.isEmpty());
    }

    @Test
    void getUnifiedHealth_shouldReturnHealth() {
        when(crossCopilotIntelligenceEngine.getUnifiedBusinessHealth()).thenReturn(Map.of("status", "healthy"));
        Map<String, Object> health = orchestrator.getUnifiedHealth();
        assertEquals("healthy", health.get("status"));
    }
}
