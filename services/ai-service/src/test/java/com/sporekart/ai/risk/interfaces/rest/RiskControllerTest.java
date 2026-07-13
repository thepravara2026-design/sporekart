package com.sporekart.ai.risk.interfaces.rest;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import com.sporekart.ai.risk.api.RiskConfigurationService;
import com.sporekart.ai.risk.api.RiskEngine;
import com.sporekart.ai.risk.api.RiskMetricsService;
import com.sporekart.ai.risk.domain.*;
import com.sporekart.ai.risk.infrastructure.kafka.RiskKafkaEventPublisher;
import com.sporekart.ai.risk.infrastructure.monitoring.RiskMonitoringService;
import com.sporekart.ai.risk.infrastructure.persistence.*;
import com.sporekart.ai.risk.interfaces.rest.dto.RiskAssessResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.*;

@WebMvcTest(RiskController.class)
class RiskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RiskEngine riskEngine;
    @MockitoBean
    private RiskMetricsService riskMetricsService;
    @MockitoBean
    private RiskHistoryRepository riskHistoryRepository;
    @MockitoBean
    private RiskConfigurationService riskConfigurationService;
    @MockitoBean
    private TrustScoreRepository trustScoreRepository;
    @MockitoBean
    private ConfidenceScoreRepository confidenceScoreRepository;
    @MockitoBean
    private RiskKafkaEventPublisher riskKafkaEventPublisher;
    @MockitoBean
    private RiskMonitoringService riskMonitoringService;

    @Test
    void assessShouldReturn200() throws Exception {
        when(riskMonitoringService.checkHealth()).thenReturn(Map.of("status", "UP", "service", "risk", "timestamp", System.currentTimeMillis(), "details", Map.of()));
        doNothing().when(riskMonitoringService).recordRequest();
        doNothing().when(riskMonitoringService).recordAssessment(anyLong());
        doNothing().when(riskMonitoringService).recordRiskLevel(anyString());
        doNothing().when(riskKafkaEventPublisher).publishAssessmentCompleted(anyString(), anyString(), anyLong());

        mockMvc.perform(post("/api/v1/risk/assess")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"module\":\"prompt\",\"action\":\"generate\",\"context\":{}}"))
                .andExpect(status().isOk());
    }

    @Test
    void getHistoryShouldReturn200() throws Exception {
        when(riskHistoryRepository.findAll()).thenReturn(List.of());
        mockMvc.perform(get("/api/v1/risk/history"))
                .andExpect(status().isOk());
    }

    @Test
    void getStatisticsShouldReturn200() throws Exception {
        when(riskMetricsService.getStatistics()).thenReturn(Map.of(
            "totalAssessments", 5L,
            "averageRiskScore", 0.0,
            "riskDistribution", Map.<String, Long>of(),
            "trustTrends", Map.<String, Double>of(),
            "confidenceTrends", Map.<String, Double>of(),
            "recommendationCounts", Map.<String, Long>of()
        ));
        mockMvc.perform(get("/api/v1/risk/statistics"))
                .andExpect(status().isOk());
    }

    @Test
    void getThresholdsShouldReturn200() throws Exception {
        when(riskConfigurationService.getThresholds()).thenReturn(List.of());
        mockMvc.perform(get("/api/v1/risk/thresholds"))
                .andExpect(status().isOk());
    }

    @Test
    void recalculateShouldReturn200() throws Exception {
        doNothing().when(riskMonitoringService).recordRequest();
        doNothing().when(riskMonitoringService).recordRecalculation(anyLong());
        doNothing().when(riskKafkaEventPublisher).publishRiskRecalculated(anyString(), anyString(), anyLong());

        mockMvc.perform(post("/api/v1/risk/recalculate?assessmentId={id}", UUID.randomUUID()))
                .andExpect(status().isOk());
    }

    @Test
    void getTrustShouldReturn200() throws Exception {
        var assessmentId = UUID.randomUUID();
        var trustAssessment = new TrustAssessment(UUID.randomUUID(), assessmentId, 85.0, Map.of(TrustFactor.PROVIDER_RELIABILITY, 85.0), Map.of(TrustFactor.PROVIDER_RELIABILITY, "ok"), java.time.Instant.now());

        when(trustScoreRepository.findByAssessmentId(assessmentId)).thenReturn(trustAssessment);

        mockMvc.perform(get("/api/v1/risk/trust?assessmentId={id}", assessmentId))
                .andExpect(status().isOk());
    }

    @Test
    void getTrustHistoryShouldReturn200() throws Exception {
        when(trustScoreRepository.findAll()).thenReturn(List.of());
        mockMvc.perform(get("/api/v1/risk/trust/history"))
                .andExpect(status().isOk());
    }

    @Test
    void getConfidenceShouldReturn200() throws Exception {
        var assessmentId = UUID.randomUUID();
        var confidenceScore = new ConfidenceScore(UUID.randomUUID(), assessmentId, 75.0, Map.of(ConfidenceFactor.KNOWLEDGE_MATCH, 75.0), "explanation", java.time.Instant.now());

        when(confidenceScoreRepository.findByAssessmentId(assessmentId)).thenReturn(confidenceScore);

        mockMvc.perform(get("/api/v1/risk/confidence?assessmentId={id}", assessmentId))
                .andExpect(status().isOk());
    }

    @Test
    void getConfidenceHistoryShouldReturn200() throws Exception {
        when(confidenceScoreRepository.findAll()).thenReturn(List.of());
        mockMvc.perform(get("/api/v1/risk/confidence/history"))
                .andExpect(status().isOk());
    }

    @Test
    void getHealthShouldReturn200() throws Exception {
        when(riskMonitoringService.checkHealth()).thenReturn(Map.of(
            "status", "UP",
            "service", "risk",
            "timestamp", System.currentTimeMillis(),
            "details", Map.of()
        ));
        mockMvc.perform(get("/api/v1/risk/health"))
                .andExpect(status().isOk());
    }
}
