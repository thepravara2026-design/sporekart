package com.sporekart.ai.compliance.interfaces.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sporekart.ai.compliance.api.*;
import com.sporekart.ai.compliance.domain.*;
import com.sporekart.ai.compliance.infrastructure.kafka.ComplianceKafkaEventPublisher;
import com.sporekart.ai.compliance.infrastructure.monitoring.ComplianceMonitoringService;
import com.sporekart.ai.compliance.infrastructure.persistence.ComplianceExceptionRepository;
import com.sporekart.ai.compliance.infrastructure.persistence.ComplianceExceptionEntity;
import com.sporekart.ai.compliance.infrastructure.persistence.ComplianceViolationRepository;
import com.sporekart.ai.compliance.interfaces.rest.dto.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ComplianceController.class)
class ComplianceControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @MockitoBean private ComplianceEngine complianceEngine;
    @MockitoBean private ComplianceRegistry complianceRegistry;
    @MockitoBean private ComplianceReportingService complianceReportingService;
    @MockitoBean private ComplianceViolationRepository complianceViolationRepository;
    @MockitoBean private ComplianceExceptionRepository complianceExceptionRepository;
    @MockitoBean private ComplianceMetricsService complianceMetricsService;
    @MockitoBean private ComplianceKafkaEventPublisher complianceKafkaEventPublisher;
    @MockitoBean private ComplianceMonitoringService complianceMonitoringService;
    @MockitoBean private ComplianceHealthService complianceHealthService;

    @Test
    void testValidateReturns200() throws Exception {
        ComplianceValidateRequest request = new ComplianceValidateRequest(
            "content", "generate", Map.of(), "user1", List.of("admin")
        );

        when(complianceEngine.validate(any())).thenReturn(new com.sporekart.ai.compliance.engine.ComplianceResult(
            true, ComplianceStatus.PASSED, List.of(), List.of(),
            UUID.randomUUID(), null, Map.of()
        ));

        mockMvc.perform(post("/api/v1/compliance/validate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.compliant").value(true));
    }

    @Test
    void testGetFrameworksReturns200() throws Exception {
        when(complianceRegistry.findAllFrameworks()).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/compliance/frameworks"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray());
    }

    @Test
    void testGetRulesReturns200() throws Exception {
        when(complianceRegistry.findActiveRules()).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/compliance/rules"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray());
    }

    @Test
    void testGetRulesByFrameworkReturns200() throws Exception {
        UUID frameworkId = UUID.randomUUID();
        when(complianceRegistry.findRulesByFramework(frameworkId)).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/compliance/rules")
                .param("frameworkId", frameworkId.toString()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray());
    }

    @Test
    void testGetReportsReturns200() throws Exception {
        when(complianceRegistry.findAllFrameworks()).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/compliance/reports"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray());
    }

    @Test
    void testGetViolationsReturns200() throws Exception {
        when(complianceViolationRepository.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/compliance/violations"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray());
    }

    @Test
    void testGetExceptionsReturns200() throws Exception {
        when(complianceExceptionRepository.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/compliance/exceptions"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray());
    }

    @Test
    void testCreateExceptionReturns201() throws Exception {
        ExceptionRequest request = new ExceptionRequest(
            UUID.randomUUID().toString(), "reason", "justification", "requester"
        );

        ComplianceExceptionEntity entity = new ComplianceExceptionEntity();
        entity.setId(UUID.randomUUID());
        entity.setRuleId(UUID.fromString(request.ruleId()));
        entity.setReason(request.reason());
        entity.setJustification(request.justification());
        entity.setStatus("REQUESTED");

        when(complianceExceptionRepository.save(any())).thenReturn(entity);

        mockMvc.perform(post("/api/v1/compliance/exceptions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.reason").value("reason"));

        verify(complianceMonitoringService).recordExceptionCreated();
    }

    @Test
    void testGetStatisticsReturns200() throws Exception {
        when(complianceMetricsService.getTotalValidations()).thenReturn(10L);
        when(complianceMetricsService.getPassCount()).thenReturn(7L);
        when(complianceMetricsService.getFailureCount()).thenReturn(3L);
        when(complianceMetricsService.getViolationCount()).thenReturn(5L);
        when(complianceMetricsService.getPassRate()).thenReturn(0.7);
        when(complianceMetricsService.getAverageAssessmentLatencyMs()).thenReturn(150.0);
        when(complianceMetricsService.getStatistics()).thenReturn(Map.of());

        mockMvc.perform(get("/api/v1/compliance/statistics"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.totalValidations").value(10))
            .andExpect(jsonPath("$.passCount").value(7))
            .andExpect(jsonPath("$.failureCount").value(3));
    }

    @Test
    void testGetHealthReturns200() throws Exception {
        when(complianceHealthService.getHealthDetails()).thenReturn(Map.of(
            "status", "UP", "service", "compliance", "timestamp", Instant.now().toString()
        ));

        mockMvc.perform(get("/api/v1/compliance/health"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("UP"))
            .andExpect(jsonPath("$.service").value("compliance"));
    }
}
