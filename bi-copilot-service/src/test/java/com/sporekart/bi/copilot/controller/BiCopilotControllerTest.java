package com.sporekart.bi.copilot.controller;

import com.sporekart.bi.copilot.dto.*;
import com.sporekart.bi.copilot.service.BiCopilotOrchestrator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BiCopilotController.class)
class BiCopilotControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BiCopilotOrchestrator orchestrator;

    @Test
    void processMessage_shouldReturnOk() throws Exception {
        when(orchestrator.processMessage(any(ChatRequest.class))).thenReturn(new ChatResponse("s1", "reply", List.of(), Map.of(), false));
        mockMvc.perform(post("/api/v1/bi-copilot/chat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"message\":\"hello\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("reply"));
    }

    @Test
    void processChat_shouldReturnOk() throws Exception {
        when(orchestrator.processChat(any(ChatRequest.class))).thenReturn(new ChatResponse("s2", "chat reply", List.of(), Map.of(), true));
        mockMvc.perform(post("/api/v1/bi-copilot/chat/stream")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"message\":\"hi\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("chat reply"));
    }

    @Test
    void getDashboard_shouldReturnOk() throws Exception {
        when(orchestrator.getDashboard(eq("default"))).thenReturn(new DashboardResponse("d1", "Default", List.of(), Map.of(), List.of(), null));
        mockMvc.perform(get("/api/v1/bi-copilot/dashboards/default"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dashboardId").value("d1"));
    }

    @Test
    void queryData_shouldReturnOk() throws Exception {
        when(orchestrator.queryData(any(BiQueryRequest.class))).thenReturn(new BiQueryResponse(List.of(), 0, 1, 10, 5L, "explanation"));
        mockMvc.perform(post("/api/v1/bi-copilot/query")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"query\":\"revenue\",\"page\":1,\"size\":10}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page").value(1));
    }

    @Test
    void generateReport_shouldReturnOk() throws Exception {
        when(orchestrator.generateReport(any(ReportRequest.class))).thenReturn(new ReportResponse("r1", "Report", "completed", "pdf", "/download", Map.of(), null));
        mockMvc.perform(post("/api/v1/bi-copilot/reports")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"reportType\":\"revenue\",\"format\":\"pdf\",\"metrics\":[],\"dimensions\":[],\"recipients\":[]}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reportId").value("r1"));
    }

    @Test
    void getInsights_shouldReturnOk() throws Exception {
        when(orchestrator.getInsights(eq("revenue"), eq("2025-06"))).thenReturn(new InsightsResponse(List.of(), 0, "revenue", "2025-06"));
        mockMvc.perform(get("/api/v1/bi-copilot/insights?category=revenue&period=2025-06"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.category").value("revenue"));
    }

    @Test
    void getTrends_shouldReturnOk() throws Exception {
        when(orchestrator.getTrends(eq("revenue"), eq(12))).thenReturn(new TrendsResponse(List.of(), "revenue", "2025", 12, Map.of()));
        mockMvc.perform(get("/api/v1/bi-copilot/trends?metric=revenue&months=12"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.metric").value("revenue"));
    }

    @Test
    void getAnomalies_shouldReturnOk() throws Exception {
        when(orchestrator.getAnomalies(eq("2025-06"))).thenReturn(new AnomaliesResponse(List.of(), 0, 0, 0, "2025-06"));
        mockMvc.perform(get("/api/v1/bi-copilot/anomalies?period=2025-06"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.period").value("2025-06"));
    }

    @Test
    void forecast_shouldReturnOk() throws Exception {
        when(orchestrator.forecast(any(ForecastRequest.class))).thenReturn(new ForecastResponse("f1", "revenue", "seasonal", List.of(), 0.95, "Good"));
        mockMvc.perform(post("/api/v1/bi-copilot/forecast")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"metric\":\"revenue\",\"method\":\"seasonal\",\"horizon\":6,\"parameters\":{}}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.forecastId").value("f1"));
    }

    @Test
    void getCrossCopilotMetrics_shouldReturnOk() throws Exception {
        when(orchestrator.getCrossCopilotMetrics()).thenReturn(new CrossCopilotMetricsResponse(List.of(), null, Map.of()));
        mockMvc.perform(get("/api/v1/bi-copilot/cross-copilot/metrics"))
                .andExpect(status().isOk());
    }

    @Test
    void getCustomerSegments_shouldReturnOk() throws Exception {
        when(orchestrator.getCustomerSegments()).thenReturn(new CustomerSegmentsResponse(List.of(), 0, 0.0, null));
        mockMvc.perform(get("/api/v1/bi-copilot/customers/segments"))
                .andExpect(status().isOk());
    }

    @Test
    void getScheduledReports_shouldReturnOk() throws Exception {
        when(orchestrator.getScheduledReports()).thenReturn(List.of());
        mockMvc.perform(get("/api/v1/bi-copilot/reports/scheduled"))
                .andExpect(status().isOk());
    }

    @Test
    void getUnifiedHealth_shouldReturnOk() throws Exception {
        when(orchestrator.getUnifiedHealth()).thenReturn(Map.of("status", "healthy"));
        mockMvc.perform(get("/api/v1/bi-copilot/health/unified"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("healthy"));
    }
}
