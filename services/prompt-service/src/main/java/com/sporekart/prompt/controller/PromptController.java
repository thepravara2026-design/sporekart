package com.sporekart.prompt.controller;

import com.sporekart.prompt.dto.request.*;
import com.sporekart.prompt.dto.response.*;
import com.sporekart.prompt.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/prompts")
@Tag(name = "Prompt Management API", description = "Enterprise Prompt Management Platform endpoints")
public class PromptController {

    private static final Logger log = LoggerFactory.getLogger(PromptController.class);

    private final PromptService promptService;
    private final PromptVersionService versionService;
    private final PromptApprovalService approvalService;
    private final PreviewEngine previewEngine;
    private final PromptPlaygroundService playgroundService;
    private final PromptComparisonService comparisonService;
    private final PromptMetricsService metricsService;
    private final PromptAuditService auditService;

    public PromptController(PromptService promptService,
                            PromptVersionService versionService,
                            PromptApprovalService approvalService,
                            PreviewEngine previewEngine,
                            PromptPlaygroundService playgroundService,
                            PromptComparisonService comparisonService,
                            PromptMetricsService metricsService,
                            PromptAuditService auditService) {
        this.promptService = promptService;
        this.versionService = versionService;
        this.approvalService = approvalService;
        this.previewEngine = previewEngine;
        this.playgroundService = playgroundService;
        this.comparisonService = comparisonService;
        this.metricsService = metricsService;
        this.auditService = auditService;
    }

    @PostMapping
    @Operation(summary = "Create a new prompt template")
    public ResponseEntity<PromptTemplateResponse> createTemplate(@Valid @RequestBody CreatePromptRequest request) {
        var response = promptService.createTemplate(request);
        log.info("Created prompt template: {} ({})", response.name(), response.id());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @Operation(summary = "List all prompt templates")
    public ResponseEntity<List<PromptTemplateResponse>> listTemplates() {
        return ResponseEntity.ok(promptService.listTemplates());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a prompt template by ID")
    public ResponseEntity<PromptTemplateResponse> getTemplate(@PathVariable UUID id) {
        return promptService.getTemplate(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a prompt template")
    public ResponseEntity<PromptTemplateResponse> updateTemplate(
            @PathVariable UUID id,
            @Valid @RequestBody UpdatePromptRequest request) {
        var response = promptService.updateTemplate(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a prompt template (soft delete)")
    public ResponseEntity<Void> deleteTemplate(
            @PathVariable UUID id,
            @RequestParam UUID performedBy) {
        promptService.deleteTemplate(id, performedBy);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/archive")
    @Operation(summary = "Archive a prompt template")
    public ResponseEntity<Void> archiveTemplate(
            @PathVariable UUID id,
            @RequestParam UUID performedBy) {
        promptService.archiveTemplate(id, performedBy);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/versions")
    @Operation(summary = "Create a new version for a prompt template")
    public ResponseEntity<PromptVersionResponse> createVersion(
            @PathVariable UUID id,
            @Valid @RequestBody CreateVersionRequest request) {
        var response = versionService.createVersion(id, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}/versions")
    @Operation(summary = "List all versions for a prompt template")
    public ResponseEntity<List<PromptVersionResponse>> getVersions(@PathVariable UUID id) {
        return ResponseEntity.ok(versionService.getVersions(id));
    }

    @GetMapping("/{id}/versions/latest")
    @Operation(summary = "Get the latest version of a prompt template")
    public ResponseEntity<PromptVersionResponse> getLatestVersion(@PathVariable UUID id) {
        return versionService.getLatestVersion(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/versions/published")
    @Operation(summary = "Get the published version of a prompt template")
    public ResponseEntity<PromptVersionResponse> getPublishedVersion(@PathVariable UUID id) {
        return versionService.getPublishedVersion(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/versions/{version}")
    @Operation(summary = "Get a specific version of a prompt template")
    public ResponseEntity<PromptVersionResponse> getVersion(
            @PathVariable UUID id,
            @PathVariable Integer version) {
        return versionService.getVersion(id, version)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/publish")
    @Operation(summary = "Publish a prompt version")
    public ResponseEntity<PromptTemplateResponse> publish(@Valid @RequestBody PublishRequest request) {
        var response = promptService.publishTemplate(request.versionId(), request.versionId(), request.performedBy());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/rollback")
    @Operation(summary = "Rollback to a previous version (creates a new version with old content)")
    public ResponseEntity<PromptTemplateResponse> rollback(@Valid @RequestBody RollbackRequest request) {
        var response = promptService.rollbackTemplate(request.templateId(), request.targetVersion(),
                request.performedBy(), request.reason());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/approvals")
    @Operation(summary = "Request approval for a prompt version")
    public ResponseEntity<PromptApprovalResponse> requestApproval(@Valid @RequestBody ApprovalRequest request) {
        var response = approvalService.requestApproval(
                request.versionId(), request.templateId(), request.approver(),
                request.step(), request.comments());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/approvals/{approvalId}/decision")
    @Operation(summary = "Approve or reject a prompt approval request")
    public ResponseEntity<PromptApprovalResponse> decideApproval(
            @PathVariable UUID approvalId,
            @Valid @RequestBody ApprovalDecisionRequest request) {
        var response = approvalService.approve(approvalId, request.decision(), request.comments(), approvalId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/approvals")
    @Operation(summary = "Get approval history for a prompt template")
    public ResponseEntity<List<PromptApprovalResponse>> getApprovals(@PathVariable UUID id) {
        return ResponseEntity.ok(approvalService.getApprovalsForTemplate(id));
    }

    @GetMapping("/approvals/pending")
    @Operation(summary = "Get pending approvals for an approver")
    public ResponseEntity<List<PromptApprovalResponse>> getPendingApprovals(@RequestParam UUID approver) {
        return ResponseEntity.ok(approvalService.getPendingApprovals(approver));
    }

    @PostMapping("/preview")
    @Operation(summary = "Preview a rendered prompt with variables")
    public ResponseEntity<PreviewResponse> preview(@Valid @RequestBody PreviewRequest request) {
        var response = previewEngine.preview(request.versionId(), request.variables());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/execute-test")
    @Operation(summary = "Execute a test prompt against a provider")
    public ResponseEntity<TestExecutionResponse> executeTest(@Valid @RequestBody TestExecutionRequest request) {
        var response = playgroundService.executeTest(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/compare")
    @Operation(summary = "Compare two versions of a prompt template")
    public ResponseEntity<ComparisonResponse> compare(
            @PathVariable UUID id,
            @RequestParam int versionA,
            @RequestParam int versionB) {
        var response = comparisonService.compare(id, versionA, versionB);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/metrics")
    @Operation(summary = "Get usage metrics for a prompt template")
    public ResponseEntity<MetricsResponse> getMetrics(@PathVariable UUID id) {
        return ResponseEntity.ok(metricsService.getMetrics(id));
    }

    @GetMapping("/{id}/audit")
    @Operation(summary = "Get audit log for a prompt template")
    public ResponseEntity<List<AuditEntryResponse>> getAuditLog(@PathVariable UUID id) {
        return ResponseEntity.ok(auditService.getAuditLog(id));
    }

    @GetMapping("/search")
    @Operation(summary = "Search prompt templates")
    public ResponseEntity<List<PromptTemplateResponse>> search(@RequestParam String q) {
        return ResponseEntity.ok(promptService.search(q));
    }

    @GetMapping("/dashboard")
    @Operation(summary = "Get prompt management dashboard")
    public ResponseEntity<DashboardResponse> dashboard() {
        return ResponseEntity.ok(promptService.getDashboard());
    }

    @GetMapping("/filter/category/{category}")
    @Operation(summary = "Filter prompt templates by category")
    public ResponseEntity<List<PromptTemplateResponse>> filterByCategory(@PathVariable String category) {
        return ResponseEntity.ok(promptService.filterByCategory(category));
    }

    @GetMapping("/filter/status/{status}")
    @Operation(summary = "Filter prompt templates by status")
    public ResponseEntity<List<PromptTemplateResponse>> filterByStatus(@PathVariable String status) {
        return ResponseEntity.ok(promptService.filterByStatus(status));
    }

    @GetMapping("/filter/owner/{owner}")
    @Operation(summary = "Filter prompt templates by owner")
    public ResponseEntity<List<PromptTemplateResponse>> filterByOwner(@PathVariable UUID owner) {
        return ResponseEntity.ok(promptService.filterByOwner(owner));
    }
}
