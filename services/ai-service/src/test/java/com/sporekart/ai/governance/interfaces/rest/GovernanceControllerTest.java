package com.sporekart.ai.governance.interfaces.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sporekart.ai.governance.api.*;
import com.sporekart.ai.governance.domain.*;
import com.sporekart.ai.governance.infrastructure.kafka.GovernanceKafkaEventPublisher;
import com.sporekart.ai.governance.infrastructure.monitoring.GovernanceMonitoringService;
import com.sporekart.ai.governance.interfaces.rest.dto.GovernanceRequestDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetDateTime;
import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GovernanceController.class)
class GovernanceControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @MockitoBean private GovernanceEngine engine;
    @MockitoBean private GovernanceManager manager;
    @MockitoBean private GovernanceHealthService healthService;
    @MockitoBean private GovernanceRegistryService registryService;
    @MockitoBean private GovernanceKafkaEventPublisher eventPublisher;
    @MockitoBean private GovernanceMonitoringService monitoringService;

    @Test
    void testIndex() throws Exception {
        mockMvc.perform(get("/api/v1/governance"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.service").value("Enterprise AI Governance"));
    }

    @Test
    void testGetStatus() throws Exception {
        when(healthService.getStatus()).thenReturn(Map.of("operational", true, "mode", "DEVELOPMENT"));
        when(healthService.getMetrics()).thenReturn(Map.of("totalPolicies", 0L));

        mockMvc.perform(get("/api/v1/governance/status"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.operational").value(true))
            .andExpect(jsonPath("$.mode").value("DEVELOPMENT"));
    }

    @Test
    void testGetConfiguration() throws Exception {
        when(manager.getAllConfigurations()).thenReturn(List.of());
        mockMvc.perform(get("/api/v1/governance/configuration"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray());
    }

    @Test
    void testValidate() throws Exception {
        GovernanceRequestDto dto = new GovernanceRequestDto(
            "content", "generate", Map.of(), "user1", List.of("admin"));
        GovernanceResponse response = new GovernanceResponse(
            UUID.randomUUID(), UUID.randomUUID(), GovernanceDecision.ALLOW,
            List.of(), Map.of(), 10L, OffsetDateTime.now());

        when(engine.validate(any())).thenReturn(response);

        mockMvc.perform(post("/api/v1/governance/validate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.decision").value("ALLOW"));
    }

    @Test
    void testReload() throws Exception {
        mockMvc.perform(post("/api/v1/governance/reload"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void testHealth() throws Exception {
        when(healthService.checkHealth()).thenReturn(Map.of(
            "status", "UP", "service", "governance",
            "timestamp", System.currentTimeMillis(),
            "details", Map.of()));

        mockMvc.perform(get("/api/v1/governance/health"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("UP"))
            .andExpect(jsonPath("$.service").value("governance"));
    }
}
