package com.sporekart.executive.copilot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sporekart.executive.copilot.dto.*;
import com.sporekart.executive.copilot.service.ExecutiveOrchestrator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ExecutiveCopilotController.class)
@WithMockUser
class ExecutiveCopilotControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @MockBean private ExecutiveOrchestrator orchestrator;

    @Test
    void chat_shouldReturnOk() throws Exception {
        var request = new ExecutiveQueryRequest("How is the company?", "company_performance", null, null, null);
        var response = new ExecutiveQueryResponse("q1", "company_performance", "Summary", Map.of(), List.of(), List.of(), 100L);
        when(orchestrator.processQuery(any())).thenReturn(response);

        mockMvc.perform(post("/api/v1/copilot/executive/chat").with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.queryId").value("q1"));
    }

    @Test
    void chat_withInvalidRequest_shouldReturnBadRequest() throws Exception {
        mockMvc.perform(post("/api/v1/copilot/executive/chat").with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
            .andExpect(status().isBadRequest());
    }

    @Test
    void stream_shouldReturnOk() throws Exception {
        var request = new ExecutiveQueryRequest("How is the company?", "company_performance", null, null, null);
        var response = new ExecutiveQueryResponse("q2", "company_performance", "Stream summary", Map.of(), List.of(), List.of(), 50L);
        when(orchestrator.processQuery(any())).thenReturn(response);

        mockMvc.perform(post("/api/v1/copilot/executive/stream").with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void dashboard_shouldReturnOk() throws Exception {
        var dashboard = new DashboardResponse("Summary",
            new DashboardResponse.CompanyHealth(82.5, 2.5, "LOW", Map.of()),
            Map.of("revenue", "Rs. 1.25Cr"),
            List.of(), new DashboardResponse.RiskSummary(42.5, "MODERATE", 0, 2),
            List.of(), List.of());
        when(orchestrator.getDashboard()).thenReturn(dashboard);

        mockMvc.perform(get("/api/v1/copilot/executive/dashboard"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.health.overallScore").value(82.5));
    }

    @Test
    void dashboard_today_shouldReturnOk() throws Exception {
        when(orchestrator.getTodaySummary()).thenReturn(Map.of("ordersToday", 85));
        mockMvc.perform(get("/api/v1/copilot/executive/dashboard/today"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void dashboard_weekly_shouldReturnOk() throws Exception {
        when(orchestrator.getWeeklyReport()).thenReturn(Map.of("week", "Week 30"));
        mockMvc.perform(get("/api/v1/copilot/executive/dashboard/weekly"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void dashboard_monthly_shouldReturnOk() throws Exception {
        when(orchestrator.getMonthlyReport()).thenReturn(Map.of("month", "July"));
        mockMvc.perform(get("/api/v1/copilot/executive/dashboard/monthly"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void report_shouldReturnOk() throws Exception {
        var request = new ReportRequest("quarterly", "Q2");
        var response = new ReportResponse("r1", "Report", "Q2", "Summary", Map.of(), Map.of(), List.of(), List.of(), Map.of(), "Outlook");
        when(orchestrator.generateReport(any())).thenReturn(response);

        mockMvc.perform(post("/api/v1/copilot/executive/report").with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void forecast_shouldReturnOk() throws Exception {
        var request = new ForecastRequest("revenue", "Q3", 6);
        var response = new ForecastResponse("revenue", 6, List.of(1.0), List.of(0.8), List.of(1.2), 87.5, Map.of(), List.of());
        when(orchestrator.forecast(any())).thenReturn(response);

        mockMvc.perform(post("/api/v1/copilot/executive/forecast").with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void risk_shouldReturnOk() throws Exception {
        var request = new RiskRequest("all", "all");
        var response = new RiskResponse(42.5, "MEDIUM", List.of(), List.of(), List.of(), List.of());
        when(orchestrator.assessRisks(any())).thenReturn(response);

        mockMvc.perform(post("/api/v1/copilot/executive/risk").with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void health_shouldReturnOk() throws Exception {
        var health = new HealthResponse(82.5, 80.0, 2.5, "LOW", List.of(), "Summary");
        when(orchestrator.getHealth()).thenReturn(health);

        mockMvc.perform(get("/api/v1/copilot/executive/health"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.overallScore").value(82.5));
    }

    @Test
    void financial_shouldReturnOk() throws Exception {
        var request = new FinancialRequest("revenue", "Q2");
        var response = new FinancialResponse(Map.of("totalRevenue", 12500000.0), List.of(),
            new FinancialResponse.RevenueDetail(12500000.0, 13.6, Map.of(), 850.0, 4500.0));
        when(orchestrator.analyzeFinancials(anyString(), anyString())).thenReturn(response);

        mockMvc.perform(post("/api/v1/copilot/executive/financial").with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void decision_shouldReturnOk() throws Exception {
        var request = new DecisionRequest("Expand", "expansion", "growth", 100.0, "6m");
        var response = new DecisionResponse("Expand South", 185.0, 92.0, 0.85, "MEDIUM", "6m", List.of(), List.of());
        when(orchestrator.getStrategicDecision(any())).thenReturn(response);

        mockMvc.perform(post("/api/v1/copilot/executive/decision").with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void performance_shouldReturnOk() throws Exception {
        var request = new PerformanceRequest("Training", "Q2");
        var response = new PerformanceResponse(List.of(), Map.of("avg", 85.0));
        when(orchestrator.analyzePerformance(any())).thenReturn(response);

        mockMvc.perform(post("/api/v1/copilot/executive/performance").with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void market_shouldReturnOk() throws Exception {
        var request = new MarketRequest("mushroom", "India");
        var response = new MarketResponse("mushroom", 500000000.0, 18.5, List.of(), List.of(), List.of());
        when(orchestrator.analyzeMarket(any())).thenReturn(response);

        mockMvc.perform(post("/api/v1/copilot/executive/market").with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void riskMatrix_shouldReturnOk() throws Exception {
        var matrix = new com.sporekart.executive.copilot.domain.RiskMatrix(List.of(), List.of(), List.of(), List.of(), 42.5, "MEDIUM");
        when(orchestrator.getRiskMatrix()).thenReturn(matrix);

        mockMvc.perform(get("/api/v1/copilot/executive/risk/matrix"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void topRisks_shouldReturnOk() throws Exception {
        when(orchestrator.getTopRisks(5)).thenReturn(Map.of("count", 5));
        mockMvc.perform(get("/api/v1/copilot/executive/risk/top?count=5"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void revenueDeclineRisk_shouldReturnOk() throws Exception {
        when(orchestrator.detectRevenueDeclineRisk()).thenReturn(Map.of("riskDetected", false));
        mockMvc.perform(get("/api/v1/copilot/executive/risk/revenue-decline"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void dimensionHealth_shouldReturnOk() throws Exception {
        var health = new com.sporekart.executive.copilot.domain.CompanyHealth(82.5, 80.0, 2.5, Map.of(), "LOW", "Good");
        when(orchestrator.getDimensionHealth("revenue")).thenReturn(health);

        mockMvc.perform(get("/api/v1/copilot/executive/health/dimension/revenue"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void sustainability_shouldReturnOk() throws Exception {
        var sust = new com.sporekart.executive.copilot.domain.BusinessSustainability(82.0, 65.0, 40.0, 55.0, 45.0, "LOW", "Good");
        when(orchestrator.assessSustainability()).thenReturn(sust);

        mockMvc.perform(get("/api/v1/copilot/executive/health/sustainability"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void cashFlow_shouldReturnOk() throws Exception {
        var cf = new com.sporekart.executive.copilot.domain.CashFlowIndicators(2500000.0, -500000.0, -200000.0, 1800000.0, 350000.0, 12.0, "HEALTHY");
        when(orchestrator.analyzeCashFlow()).thenReturn(cf);

        mockMvc.perform(get("/api/v1/copilot/executive/health/cashflow"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void revenueBreakdown_shouldReturnOk() throws Exception {
        var breakdown = new com.sporekart.executive.copilot.domain.RevenueBreakdown(12500000.0, 11000000.0, 13.6, Map.of(), Map.of(), Map.of(), 850.0, 4500.0);
        when(orchestrator.getRevenueBreakdown()).thenReturn(breakdown);

        mockMvc.perform(get("/api/v1/copilot/executive/financial/revenue-breakdown"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void profitAnalysis_shouldReturnOk() throws Exception {
        var analysis = new com.sporekart.executive.copilot.domain.ProfitAnalysis(5000000.0, 40.0, 3000000.0, 24.0, 6500000.0, 3800000.0, 30.4);
        when(orchestrator.getProfitAnalysis()).thenReturn(analysis);

        mockMvc.perform(get("/api/v1/copilot/executive/financial/profit-analysis"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void financialTrends_shouldReturnOk() throws Exception {
        when(orchestrator.getFinancialTrends("quarterly")).thenReturn(Map.of("growth", 13.6));
        mockMvc.perform(get("/api/v1/copilot/executive/financial/trends?period=quarterly"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void forecastRevenue_shouldReturnOk() throws Exception {
        when(orchestrator.forecastRevenue(12)).thenReturn(Map.of("revenue", 100.0));
        mockMvc.perform(get("/api/v1/copilot/executive/forecast/revenue?months=12"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void forecastOrders_shouldReturnOk() throws Exception {
        when(orchestrator.forecastOrders(6)).thenReturn(Map.of("orders", 50.0));
        mockMvc.perform(get("/api/v1/copilot/executive/forecast/orders?months=6"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void simulateDecision_shouldReturnOk() throws Exception {
        when(orchestrator.simulateDecisionImpact("Expand", 1000000.0)).thenReturn(Map.of("roi", 150.0));
        mockMvc.perform(get("/api/v1/copilot/executive/decision/simulate")
                .param("decision", "Expand")
                .param("investment", "1000000.0"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void benchmark_shouldReturnOk() throws Exception {
        when(orchestrator.benchmarkCompetitors("overall")).thenReturn(Map.of("rank", 2));
        mockMvc.perform(get("/api/v1/copilot/executive/market/benchmark?metric=overall"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void industryTrends_shouldReturnOk() throws Exception {
        when(orchestrator.getIndustryTrends("mushroom")).thenReturn(List.of("Trend 1", "Trend 2"));
        mockMvc.perform(get("/api/v1/copilot/executive/market/trends?sector=mushroom"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void departmentDetail_shouldReturnOk() throws Exception {
        var detail = new com.sporekart.executive.copilot.domain.DepartmentPerformance("Training", 92.0, 4.5, List.of(), List.of(), List.of(), Map.of());
        when(orchestrator.getDepartmentDetail("Training")).thenReturn(detail);

        mockMvc.perform(get("/api/v1/copilot/executive/performance/department/Training"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void crossDepartmentalMetrics_shouldReturnOk() throws Exception {
        var metrics = List.of(new com.sporekart.executive.copilot.domain.PerformanceMetric("Revenue", "Financial", 13.6, 12.0, 20.0, "%", "UP", "GREEN"));
        when(orchestrator.getCrossDepartmentalMetrics()).thenReturn(metrics);

        mockMvc.perform(get("/api/v1/copilot/executive/performance/cross-departmental"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true));
    }
}
