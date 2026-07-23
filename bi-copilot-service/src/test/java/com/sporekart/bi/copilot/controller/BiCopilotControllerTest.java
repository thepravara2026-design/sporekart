package com.sporekart.bi.copilot.controller;

import com.sporekart.bi.copilot.domain.*;
import com.sporekart.bi.copilot.dto.*;
import com.sporekart.bi.copilot.service.BiCopilotOrchestrator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BiCopilotController.class)
class BiCopilotControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BiCopilotOrchestrator orchestrator;

    @Test
    void getDashboardReturnsDashboardResponse() throws Exception {
        when(orchestrator.getExecutiveDashboard("daily"))
                .thenReturn(new DashboardResponse(
                        mockExecutiveSummary(), List.of(), null));

        mockMvc.perform(get("/api/v1/bi/dashboard")
                        .param("type", "daily"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.summary").exists());
    }

    @Test
    void getRevenueAnalyticsReturnsRevenueData() throws Exception {
        when(orchestrator.getRevenueAnalytics("current"))
                .thenReturn(mock(RevenueAnalytics.class));

        mockMvc.perform(get("/api/v1/bi/revenue")
                        .param("period", "current"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.period").exists());
    }

    @Test
    void getCustomerAnalyticsReturnsCustomerData() throws Exception {
        when(orchestrator.getCustomerAnalytics("current"))
                .thenReturn(mock(CustomerAnalytics.class));

        mockMvc.perform(get("/api/v1/bi/customers")
                        .param("period", "current"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.period").exists());
    }

    @Test
    void getProductAnalyticsReturnsProductData() throws Exception {
        when(orchestrator.getProductAnalytics("current"))
                .thenReturn(mock(ProductAnalytics.class));

        mockMvc.perform(get("/api/v1/bi/products")
                        .param("period", "current"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.period").exists());
    }

    @Test
    void getInventoryAnalyticsReturnsInventoryData() throws Exception {
        when(orchestrator.getInventoryAnalytics("current"))
                .thenReturn(mock(InventoryAnalytics.class));

        mockMvc.perform(get("/api/v1/bi/inventory")
                        .param("period", "current"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.period").exists());
    }

    @Test
    void getTrainingAnalyticsReturnsTrainingData() throws Exception {
        when(orchestrator.getTrainingAnalytics("current"))
                .thenReturn(mock(TrainingAnalytics.class));

        mockMvc.perform(get("/api/v1/bi/training")
                        .param("period", "current"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.period").exists());
    }

    @Test
    void getForecastReturnsForecastResponse() throws Exception {
        when(orchestrator.getForecast("revenue", "current", 6, "auto"))
                .thenReturn(mock(BusinessForecast.class));

        mockMvc.perform(get("/api/v1/bi/forecast")
                        .param("metric", "revenue")
                        .param("period", "current")
                        .param("horizon", "6")
                        .param("method", "auto"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.forecastId").exists());
    }

    @Test
    void getRecommendationsReturnsRecommendations() throws Exception {
        when(orchestrator.getRecommendations("revenue", "current"))
                .thenReturn(List.of(mock(DecisionRecommendation.class)));

        mockMvc.perform(get("/api/v1/bi/recommendations")
                        .param("focus", "revenue")
                        .param("period", "current"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.recommendations").exists());
    }

    @Test
    void getRisksReturnsRiskData() throws Exception {
        when(orchestrator.getRisks("current"))
                .thenReturn(List.of(mock(RiskAlert.class)));

        mockMvc.perform(get("/api/v1/bi/risks")
                        .param("period", "current"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.risks").exists());
    }

    @Test
    void answerQueryReturnsQueryResponse() throws Exception {
        when(orchestrator.answerQuery("test query", true, true))
                .thenReturn(new NaturalLanguageQueryResponse(
                        "revenue_query", "test explanation",
                        Map.of("revenue", 100000.0), null, List.of(), "suggested"));

        mockMvc.perform(post("/api/v1/bi/query")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "query": "test query",
                                    "generateVisualization": true,
                                    "includeExplanation": true
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.intent").value("revenue_query"));
    }

    @Test
    void getHealthScoreReturnsHealthScore() throws Exception {
        when(orchestrator.getHealthScore("current"))
                .thenReturn(mock(CompanyHealthScore.class));

        mockMvc.perform(get("/api/v1/bi/health-score")
                        .param("period", "current"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.overall").exists());
    }

    @Test
    void processChatReturnsChatResponse() throws Exception {
        when(orchestrator.processMessage(any(ChatRequest.class)))
                .thenReturn(new ChatResponse("sess-1", "response", List.of(), Map.of(), false));

        mockMvc.perform(post("/api/v1/bi/chat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "message": "show dashboard",
                                    "sessionId": "sess-1"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("response"));
    }

    private ExecutiveSummary mockExecutiveSummary() {
        return new ExecutiveSummary("sum-1", "daily", "DAILY",
                mock(CompanyHealthScore.class),
                mock(RevenueAnalytics.class),
                mock(CustomerAnalytics.class),
                mock(ProductAnalytics.class),
                mock(InventoryAnalytics.class),
                mock(TrainingAnalytics.class),
                List.of(), List.of(), List.of(), null);
    }
}
