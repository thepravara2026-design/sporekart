package com.sporekart.prompt.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sporekart.prompt.domain.PromptCategory;
import com.sporekart.prompt.domain.PromptScope;
import com.sporekart.prompt.domain.PromptStatus;
import com.sporekart.prompt.dto.request.*;
import com.sporekart.prompt.dto.response.*;
import com.sporekart.prompt.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class PromptControllerTest {

    @Mock private PromptService promptService;
    @Mock private PromptVersionService versionService;
    @Mock private PromptApprovalService approvalService;
    @Mock private PreviewEngine previewEngine;
    @Mock private PromptPlaygroundService playgroundService;
    @Mock private PromptComparisonService comparisonService;
    @Mock private PromptMetricsService metricsService;
    @Mock private PromptAuditService auditService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @ControllerAdvice
    static class TestExceptionHandler {
        @ExceptionHandler(IllegalArgumentException.class)
        @ResponseBody
        public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @BeforeEach
    void setUp() {
        var controller = new PromptController(promptService, versionService, approvalService,
                previewEngine, playgroundService, comparisonService, metricsService, auditService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new TestExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void shouldCreateTemplate() throws Exception {
        var response = PromptTemplateResponse.builder()
                .id(UUID.randomUUID()).name("Test").slug("test-slug")
                .category(PromptCategory.COMMERCE).scope(PromptScope.GLOBAL)
                .status(PromptStatus.DRAFT).owner(UUID.randomUUID())
                .createdBy(UUID.randomUUID()).createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now()).isActive(true).tags(List.of())
                .build();

        when(promptService.createTemplate(any())).thenReturn(response);

        var request = new CreatePromptRequest("Test", "test-slug", "desc",
                PromptCategory.COMMERCE, PromptScope.GLOBAL, UUID.randomUUID(), UUID.randomUUID(), null);

        mockMvc.perform(post("/api/v1/prompts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Test"));
    }

    @Test
    void shouldListTemplates() throws Exception {
        when(promptService.listTemplates()).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/prompts"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldGetTemplate() throws Exception {
        var id = UUID.randomUUID();
        var response = PromptTemplateResponse.builder()
                .id(id).name("Test").slug("test")
                .category(PromptCategory.COMMERCE).scope(PromptScope.GLOBAL)
                .status(PromptStatus.DRAFT).owner(UUID.randomUUID())
                .createdBy(UUID.randomUUID()).createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now()).isActive(true).tags(List.of())
                .build();

        when(promptService.getTemplate(id)).thenReturn(Optional.of(response));

        mockMvc.perform(get("/api/v1/prompts/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()));
    }

    @Test
    void shouldReturn404WhenTemplateNotFound() throws Exception {
        var id = UUID.randomUUID();
        when(promptService.getTemplate(id)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/prompts/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldUpdateTemplate() throws Exception {
        var id = UUID.randomUUID();
        var response = PromptTemplateResponse.builder()
                .id(id).name("Updated").slug("updated")
                .category(PromptCategory.ANALYTICS).scope(PromptScope.GLOBAL)
                .status(PromptStatus.DRAFT).owner(UUID.randomUUID())
                .createdBy(UUID.randomUUID()).createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now()).isActive(true).tags(List.of())
                .build();

        when(promptService.updateTemplate(any(), any())).thenReturn(response);

        var request = new UpdatePromptRequest("Updated", null, null, PromptCategory.ANALYTICS,
                null, null, UUID.randomUUID(), null);

        mockMvc.perform(put("/api/v1/prompts/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated"));
    }

    @Test
    void shouldDeleteTemplate() throws Exception {
        var id = UUID.randomUUID();
        mockMvc.perform(delete("/api/v1/prompts/{id}", id)
                        .param("performedBy", UUID.randomUUID().toString()))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldArchiveTemplate() throws Exception {
        var id = UUID.randomUUID();
        mockMvc.perform(post("/api/v1/prompts/{id}/archive", id)
                        .param("performedBy", UUID.randomUUID().toString()))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldCreateVersion() throws Exception {
        var templateId = UUID.randomUUID();
        var response = PromptVersionResponse.builder()
                .id(UUID.randomUUID()).templateId(templateId).version(1)
                .promptBody("Test body").createdAt(OffsetDateTime.now())
                .createdBy(UUID.randomUUID()).build();

        when(versionService.createVersion(any(), any())).thenReturn(response);

        var request = new CreateVersionRequest("Test body", null, null, null,
                null, null, null, null, null, null, null, null, null,
                UUID.randomUUID(), "Initial");

        mockMvc.perform(post("/api/v1/prompts/{id}/versions", templateId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldGetVersions() throws Exception {
        var templateId = UUID.randomUUID();
        when(versionService.getVersions(templateId)).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/prompts/{id}/versions", templateId))
                .andExpect(status().isOk());
    }

    @Test
    void shouldGetLatestVersion() throws Exception {
        var templateId = UUID.randomUUID();
        var response = PromptVersionResponse.builder()
                .id(UUID.randomUUID()).templateId(templateId).version(2)
                .promptBody("Test").createdAt(OffsetDateTime.now())
                .createdBy(UUID.randomUUID()).build();

        when(versionService.getLatestVersion(templateId)).thenReturn(Optional.of(response));

        mockMvc.perform(get("/api/v1/prompts/{id}/versions/latest", templateId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.version").value(2));
    }

    @Test
    void shouldGetPublishedVersion() throws Exception {
        var templateId = UUID.randomUUID();
        var response = PromptVersionResponse.builder()
                .id(UUID.randomUUID()).templateId(templateId).version(1)
                .promptBody("Published").isPublished(true)
                .createdAt(OffsetDateTime.now()).createdBy(UUID.randomUUID()).build();

        when(versionService.getPublishedVersion(templateId)).thenReturn(Optional.of(response));

        mockMvc.perform(get("/api/v1/prompts/{id}/versions/published", templateId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isPublished").value(true));
    }

    @Test
    void shouldGetSpecificVersion() throws Exception {
        var templateId = UUID.randomUUID();
        when(versionService.getVersion(templateId, 1)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/prompts/{id}/versions/{version}", templateId, 1))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldPublish() throws Exception {
        var templateId = UUID.randomUUID();
        var response = PromptTemplateResponse.builder()
                .id(templateId).name("Test").slug("test")
                .category(PromptCategory.COMMERCE).scope(PromptScope.GLOBAL)
                .status(PromptStatus.PUBLISHED).owner(UUID.randomUUID())
                .createdBy(UUID.randomUUID()).createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now()).isActive(true).tags(List.of())
                .isPublished(true).build();

        when(promptService.publishTemplate(any(), any(), any())).thenReturn(response);

        var request = new PublishRequest(UUID.randomUUID(), UUID.randomUUID());

        mockMvc.perform(post("/api/v1/prompts/publish")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("PUBLISHED"));
    }

    @Test
    void shouldRollback() throws Exception {
        var templateId = UUID.randomUUID();
        var response = PromptTemplateResponse.builder()
                .id(templateId).name("Test").slug("test")
                .category(PromptCategory.COMMERCE).scope(PromptScope.GLOBAL)
                .status(PromptStatus.PUBLISHED).owner(UUID.randomUUID())
                .createdBy(UUID.randomUUID()).createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now()).isActive(true).tags(List.of())
                .build();

        when(promptService.rollbackTemplate(any(), anyInt(), any(), any())).thenReturn(response);

        var request = new RollbackRequest(templateId, 1, UUID.randomUUID(), "Rollback");

        mockMvc.perform(post("/api/v1/prompts/rollback")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void shouldRequestApproval() throws Exception {
        var response = PromptApprovalResponse.from(
                UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(),
                UUID.randomUUID(), com.sporekart.prompt.domain.ApprovalStatus.PENDING,
                null, null, OffsetDateTime.now(), "REVIEWER");

        when(approvalService.requestApproval(any(), any(), any(), any(), any())).thenReturn(response);

        var request = new ApprovalRequest(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), "REVIEWER", null);

        mockMvc.perform(post("/api/v1/prompts/approvals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldDecideApproval() throws Exception {
        var approvalId = UUID.randomUUID();
        var response = PromptApprovalResponse.from(
                approvalId, UUID.randomUUID(), UUID.randomUUID(),
                UUID.randomUUID(), com.sporekart.prompt.domain.ApprovalStatus.APPROVED,
                "Approved", OffsetDateTime.now(), OffsetDateTime.now(), "REVIEWER");

        when(approvalService.approve(any(), any(), any(), any())).thenReturn(response);

        var request = new ApprovalDecisionRequest(approvalId, "approve", "Looks good");

        mockMvc.perform(post("/api/v1/prompts/approvals/{approvalId}/decision", approvalId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void shouldPreview() throws Exception {
        var request = new PreviewRequest(UUID.randomUUID(), Map.of("name", "John"));
        var response = new PreviewResponse("Hello John", null,
                Map.of("name", "John"), List.of(), List.of(),
                new PreviewResponse.TokenEstimate(10, 5, 15));

        when(previewEngine.preview(any(), any())).thenReturn(response);

        mockMvc.perform(post("/api/v1/prompts/preview")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.renderedPrompt").value("Hello John"));
    }

    @Test
    void shouldExecuteTest() throws Exception {
        var request = new TestExecutionRequest(UUID.randomUUID(), Map.of(), "MOCK", "model", null);
        var response = new TestExecutionResponse("Hello", "Response",
                java.math.BigDecimal.valueOf(0.01), 10, 5, 15, 100, true, null);

        when(playgroundService.executeTest(any())).thenReturn(response);

        mockMvc.perform(post("/api/v1/prompts/execute-test")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void shouldCompareVersions() throws Exception {
        var templateId = UUID.randomUUID();
        var diffA = new ComparisonResponse.VersionDiff(1, "Hello", 5, java.math.BigDecimal.ZERO);
        var diffB = new ComparisonResponse.VersionDiff(2, "Hello World", 8, java.math.BigDecimal.ZERO);
        var response = new ComparisonResponse(diffA, diffB, List.of("World"), List.of(), 3, java.math.BigDecimal.ZERO, "");

        when(comparisonService.compare(templateId, 1, 2)).thenReturn(response);

        mockMvc.perform(get("/api/v1/prompts/{id}/compare", templateId)
                        .param("versionA", "1")
                        .param("versionB", "2"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldGetMetrics() throws Exception {
        var templateId = UUID.randomUUID();
        var response = new MetricsResponse(100, 90, 10, 250.0, 500.0,
                java.math.BigDecimal.valueOf(0.002), 90.0, 10.0);

        when(metricsService.getMetrics(templateId)).thenReturn(response);

        mockMvc.perform(get("/api/v1/prompts/{id}/metrics", templateId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalExecutions").value(100));
    }

    @Test
    void shouldGetAuditLog() throws Exception {
        var templateId = UUID.randomUUID();
        when(auditService.getAuditLog(templateId)).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/prompts/{id}/audit", templateId))
                .andExpect(status().isOk());
    }

    @Test
    void shouldSearch() throws Exception {
        when(promptService.search("test")).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/prompts/search")
                        .param("q", "test"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldGetDashboard() throws Exception {
        var response = new DashboardResponse(10, 5, 3, 2, 100,
                List.of(), List.of(), List.of());

        when(promptService.getDashboard()).thenReturn(response);

        mockMvc.perform(get("/api/v1/prompts/dashboard"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalTemplates").value(10));
    }

    @Test
    void shouldFilterByCategory() throws Exception {
        when(promptService.filterByCategory("COMMERCE")).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/prompts/filter/category/COMMERCE"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldFilterByStatus() throws Exception {
        when(promptService.filterByStatus("DRAFT")).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/prompts/filter/status/DRAFT"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldFilterByOwner() throws Exception {
        var owner = UUID.randomUUID();
        when(promptService.filterByOwner(owner)).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/prompts/filter/owner/{owner}", owner))
                .andExpect(status().isOk());
    }

    @Test
    void shouldGetApprovals() throws Exception {
        var templateId = UUID.randomUUID();
        when(approvalService.getApprovalsForTemplate(templateId)).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/prompts/{id}/approvals", templateId))
                .andExpect(status().isOk());
    }

    @Test
    void shouldGetPendingApprovals() throws Exception {
        var approver = UUID.randomUUID();
        when(approvalService.getPendingApprovals(approver)).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/prompts/approvals/pending")
                        .param("approver", approver.toString()))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturn400ForInvalidCreate() throws Exception {
        var request = new CreatePromptRequest(null, null, null, null, null, null, null, null);

        mockMvc.perform(post("/api/v1/prompts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
