package com.sporekart.ai.decision.interfaces.rest;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sporekart.ai.decision.api.*;
import com.sporekart.ai.decision.domain.*;
import com.sporekart.ai.decision.infrastructure.kafka.DecisionKafkaEventPublisher;
import com.sporekart.ai.decision.infrastructure.monitoring.DecisionMonitoringService;
import com.sporekart.ai.decision.interfaces.rest.dto.DecisionRequestDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.time.OffsetDateTime;
import java.util.*;

@WebMvcTest(DecisionController.class)
class DecisionControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @MockitoBean private DecisionEngine engine;
    @MockitoBean private DecisionResolver resolver;
    @MockitoBean private DecisionExplanationService explanationService;
    @MockitoBean private DecisionMetricsService metricsService;
    @MockitoBean private DecisionKafkaEventPublisher eventPublisher;
    @MockitoBean private DecisionMonitoringService monitoringService;

    @Test
    void postEvaluateReturns200() throws Exception {
        UUID reqId = UUID.randomUUID();
        UUID resId = UUID.randomUUID();
        DecisionRequestDto dto = new DecisionRequestDto("mod", "ALLOW", Map.of(), Map.of(), "user1", List.of("admin"));

        DecisionRequest domainReq = new DecisionRequest(any(UUID.class), eq("mod"), eq("ALLOW"), anyMap(),
            anyMap(), eq("user1"), eq(List.of("admin")), eq(List.of()), eq(List.of()), eq(Map.of()), any(OffsetDateTime.class));
        DecisionResult result = new DecisionResult(resId, reqId, DecisionAction.ALLOW, DecisionStatus.ALLOWED,
            DecisionConfidence.HIGH, "summary", List.of(), List.of(), null, 50L, false, true, OffsetDateTime.now());

        when(engine.evaluate(any(DecisionRequest.class))).thenReturn(result);

        mockMvc.perform(post("/api/v1/decisions/evaluate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(resId.toString()))
            .andExpect(jsonPath("$.status").value("ALLOWED"));
    }

    @Test
    void getByIdReturns404ForUnknown() throws Exception {
        UUID id = UUID.randomUUID();
        when(resolver.findById(id)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/decisions/{id}", id))
            .andExpect(status().isNotFound());
    }

    @Test
    void getHealthReturnsUp() throws Exception {
        when(metricsService.getDetailedMetrics()).thenReturn(Map.of("total", 0L));

        mockMvc.perform(get("/api/v1/decisions/health"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("UP"))
            .andExpect(jsonPath("$.service").value("decision-engine"));
    }

    @Test
    void getStatisticsReturns200() throws Exception {
        DecisionStatistics stats = new DecisionStatistics(UUID.randomUUID(), 10L, 5L, 3L, 1L, 1L, 0L, 0.8, 100.0, OffsetDateTime.now());
        when(metricsService.getStatistics()).thenReturn(stats);
        when(metricsService.getDetailedMetrics()).thenReturn(Map.of("total", 10L));

        mockMvc.perform(get("/api/v1/decisions/statistics"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.totalDecisions").value(10));
    }
}
