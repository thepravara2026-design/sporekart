package com.sporekart.ai.approval.interfaces.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sporekart.ai.approval.api.*;
import com.sporekart.ai.approval.domain.*;
import com.sporekart.ai.approval.infrastructure.kafka.ApprovalKafkaEventPublisher;
import com.sporekart.ai.approval.infrastructure.monitoring.ApprovalMonitoringService;
import com.sporekart.ai.approval.interfaces.rest.dto.*;
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

@WebMvcTest(ApprovalController.class)
class ApprovalControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @MockitoBean private ApprovalEngine engine;
    @MockitoBean private ApprovalMetricsService metricsService;
    @MockitoBean private ApprovalHistoryService historyService;
    @MockitoBean private ApprovalKafkaEventPublisher eventPublisher;
    @MockitoBean private ApprovalMonitoringService monitoringService;

    @Test
    void postSubmitReturns201() throws Exception {
        var dto = new ApprovalRequestDto("content", "publish", Map.of(), Map.of(),
            "user1", List.of("admin"), "reason", "normal");
        var result = new ApprovalRequest(UUID.randomUUID(), "content", "publish",
            Map.of(), Map.of(), "user1", List.of("admin"),
            "reason", "normal", null, Map.of(), null,
            ApprovalStatus.PENDING, OffsetDateTime.now());

        when(engine.submit(any())).thenReturn(result);

        mockMvc.perform(post("/api/v1/approvals")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(result.id().toString()));
    }

    @Test
    void getByIdReturns200() throws Exception {
        var id = UUID.randomUUID();
        var approval = new ApprovalRequest(id, "content", "publish",
            Map.of(), Map.of(), "user1", List.of("admin"),
            "reason", "normal", null, Map.of(), null,
            ApprovalStatus.PENDING, OffsetDateTime.now());

        when(engine.getStatus(id)).thenReturn(approval);

        mockMvc.perform(get("/api/v1/approvals/{id}", id))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(id.toString()));
    }

    @Test
    void postApproveReturns200() throws Exception {
        var id = UUID.randomUUID();
        var action = new ApprovalActionDto("reviewer1", "approved");
        var result = new ApprovalRequest(id, "content", "publish",
            Map.of(), Map.of(), "user1", List.of("admin"),
            "reason", "normal", null, Map.of(), null,
            ApprovalStatus.APPROVED, OffsetDateTime.now());

        when(engine.approve(eq(id), eq("reviewer1"), eq("approved"))).thenReturn(result);

        mockMvc.perform(post("/api/v1/approvals/{id}/approve", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(action)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("APPROVED"));
    }

    @Test
    void postRejectReturns200() throws Exception {
        var id = UUID.randomUUID();
        var action = new ApprovalActionDto("reviewer1", "rejected");
        var result = new ApprovalRequest(id, "content", "publish",
            Map.of(), Map.of(), "user1", List.of("admin"),
            "reason", "normal", null, Map.of(), null,
            ApprovalStatus.REJECTED, OffsetDateTime.now());

        when(engine.reject(eq(id), eq("reviewer1"), eq("rejected"))).thenReturn(result);

        mockMvc.perform(post("/api/v1/approvals/{id}/reject", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(action)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("REJECTED"));
    }

    @Test
    void getPendingReturns200() throws Exception {
        var approval = new ApprovalRequest(UUID.randomUUID(), "content", "publish",
            Map.of(), Map.of(), "user1", List.of("admin"),
            "reason", "normal", null, Map.of(), null,
            ApprovalStatus.PENDING, OffsetDateTime.now());

        when(engine.getPendingApprovals("user1")).thenReturn(List.of(approval));

        mockMvc.perform(get("/api/v1/approvals/pending")
                .param("userId", "user1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.total").value(1));
    }

    @Test
    void getHistoryReturns200() throws Exception {
        var requestId = UUID.randomUUID();
        var history = new ApprovalHistory(UUID.randomUUID(), requestId, UUID.randomUUID(),
            ApprovalDecision.APPROVE, "approved", Map.of(), OffsetDateTime.now());

        when(engine.getHistory(requestId)).thenReturn(List.of(history));

        mockMvc.perform(get("/api/v1/approvals/history")
                .param("requestId", requestId.toString()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.total").value(1));
    }

    @Test
    void getStatisticsReturns200() throws Exception {
        when(metricsService.getTotalRequests()).thenReturn(10L);
        when(metricsService.getPendingCount()).thenReturn(3L);
        when(metricsService.getApprovedCount()).thenReturn(5L);
        when(metricsService.getRejectedCount()).thenReturn(2L);
        when(metricsService.getAverageReviewTimeMs()).thenReturn(100.0);
        when(metricsService.getStatistics()).thenReturn(Map.of("totalRequests", 10L));

        mockMvc.perform(get("/api/v1/approvals/statistics"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.totalRequests").value(10))
            .andExpect(jsonPath("$.approved").value(5));
    }
}
