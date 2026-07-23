package com.sporekart.admin.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sporekart.admin.dto.*;
import com.sporekart.admin.service.AdminCopilotOrchestrator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AdminCopilotControllerTest {

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Mock
    private AdminCopilotOrchestrator orchestrator;

    @InjectMocks
    private AdminCopilotController controller;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void chatWithValidMessageShouldReturnOk() throws Exception {
        ChatRequest request = new ChatRequest("Show me the dashboard", null, null, null, null, null);
        ChatResponse response = new ChatResponse("sess-1", "Here is your dashboard", List.of(), Map.of(), false);

        when(orchestrator.processMessage(ArgumentMatchers.any())).thenReturn(CompletableFuture.completedFuture(response));

        mockMvc.perform(post("/api/v1/copilot/admin/chat")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.sessionId").value("sess-1"))
            .andExpect(jsonPath("$.message").value("Here is your dashboard"));
    }

    @Test
    void chatWithEmptyMessageShouldReturnBadRequest() throws Exception {
        ChatRequest request = new ChatRequest("", null, null, null, null, null);

        mockMvc.perform(post("/api/v1/copilot/admin/chat")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());
    }

    @Test
    void dashboardShouldReturnDashboard() throws Exception {
        DashboardResponse dashboardResponse = new DashboardResponse(null, List.of(), List.of(), OffsetDateTime.now());
        when(orchestrator.getDashboard()).thenReturn(dashboardResponse);

        mockMvc.perform(get("/api/v1/copilot/admin/dashboard"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.timestamp").isNotEmpty());
    }

    @Test
    void generateReportShouldReturnReport() throws Exception {
        ReportRequest request = new ReportRequest("Sales Report", "PDF", LocalDate.now().minusDays(7), LocalDate.now(), List.of("revenue", "orders"), "PDF");
        ReportResponse response = new ReportResponse("rep-1", "Sales Report", "/download/rep-1", OffsetDateTime.now(), "PDF", 1024);

        when(orchestrator.generateReport(ArgumentMatchers.any())).thenReturn(response);

        mockMvc.perform(post("/api/v1/copilot/admin/report")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.reportId").value("rep-1"))
            .andExpect(jsonPath("$.format").value("PDF"));
    }

    @Test
    void insightsShouldReturnInsights() throws Exception {
        InsightResponse response = new InsightResponse("Business is performing well", Map.of("growthRate", 12.5), List.of(), List.of(), OffsetDateTime.now());
        when(orchestrator.getInsights()).thenReturn(response);

        mockMvc.perform(get("/api/v1/copilot/admin/insights"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.summary").value("Business is performing well"));
    }

    @Test
    void forecastShouldReturnForecast() throws Exception {
        ForecastRequest request = new ForecastRequest("revenue", "monthly", 30, Map.of());
        ForecastResponse response = new ForecastResponse("revenue", null, "HIGH", List.of("Increase inventory"));

        when(orchestrator.getForecast(ArgumentMatchers.any())).thenReturn(response);

        mockMvc.perform(post("/api/v1/copilot/admin/forecast")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.metric").value("revenue"))
            .andExpect(jsonPath("$.confidenceLevel").value("HIGH"));
    }

    @Test
    void alertsShouldReturnAlerts() throws Exception {
        AlertResponse response = new AlertResponse(List.of(), 0, 0, 0);
        when(orchestrator.getAlerts()).thenReturn(response);

        mockMvc.perform(get("/api/v1/copilot/admin/alerts"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.totalCount").value(0));
    }

    @Test
    void healthShouldReturn200() throws Exception {
        mockMvc.perform(get("/api/v1/copilot/admin/health"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("UP"))
            .andExpect(jsonPath("$.service").value("admin-copilot-service"));
    }
}
