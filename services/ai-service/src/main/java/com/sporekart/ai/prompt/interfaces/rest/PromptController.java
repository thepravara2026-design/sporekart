package com.sporekart.ai.prompt.interfaces.rest;

import com.sporekart.ai.core.domain.ResponseEnvelope;
import com.sporekart.ai.prompt.application.PromptApplicationService;
import com.sporekart.ai.prompt.application.PromptAuditService;
import com.sporekart.ai.prompt.application.PromptCategoryService;
import com.sporekart.ai.prompt.application.PromptImportExportService;
import com.sporekart.ai.prompt.application.PromptLifecycleService;
import com.sporekart.ai.prompt.application.PromptNotFoundException;
import com.sporekart.ai.prompt.application.PromptRenderService;
import com.sporekart.ai.prompt.application.PromptSearchService;
import com.sporekart.ai.prompt.application.PromptValidationException;
import com.sporekart.ai.prompt.application.PromptVersionService;
import com.sporekart.ai.prompt.infrastructure.PromptKafkaEventPublisher;
import com.sporekart.ai.prompt.infrastructure.PromptRedisCacheService;
import com.sporekart.ai.prompt.infrastructure.persistence.PromptCategoryEntity;
import com.sporekart.ai.prompt.infrastructure.persistence.PromptTemplateEntity;
import com.sporekart.ai.prompt.infrastructure.persistence.PromptVersionEntity;
import com.sporekart.ai.prompt.interfaces.rest.dto.AuditResponse;
import com.sporekart.ai.prompt.interfaces.rest.dto.CategoryResponse;
import com.sporekart.ai.prompt.interfaces.rest.dto.CreatePromptRequest;
import com.sporekart.ai.prompt.interfaces.rest.dto.PromptResponse;
import com.sporekart.ai.prompt.interfaces.rest.dto.RenderPromptRequest;
import com.sporekart.ai.prompt.interfaces.rest.dto.RenderPromptResponse;
import com.sporekart.ai.prompt.interfaces.rest.dto.UpdatePromptRequest;
import com.sporekart.ai.prompt.interfaces.rest.dto.VersionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/ai/prompts")
@Tag(name = "Prompt Management", description = "Enterprise Prompt Management Platform APIs")
public class PromptController {

    private final PromptApplicationService promptService;
    private final PromptCategoryService categoryService;
    private final PromptVersionService versionService;
    private final PromptLifecycleService lifecycleService;
    private final PromptAuditService auditService;
    private final PromptSearchService searchService;
    private final PromptRenderService renderService;
    private final PromptImportExportService importExportService;
    private final PromptRedisCacheService cacheService;
    private final PromptKafkaEventPublisher kafkaPublisher;

    public PromptController(PromptApplicationService promptService,
                            PromptCategoryService categoryService,
                            PromptVersionService versionService,
                            PromptLifecycleService lifecycleService,
                            PromptAuditService auditService,
                            PromptSearchService searchService,
                            PromptRenderService renderService,
                            PromptImportExportService importExportService,
                            PromptRedisCacheService cacheService,
                            PromptKafkaEventPublisher kafkaPublisher) {
        this.promptService = promptService;
        this.categoryService = categoryService;
        this.versionService = versionService;
        this.lifecycleService = lifecycleService;
        this.auditService = auditService;
        this.searchService = searchService;
        this.renderService = renderService;
        this.importExportService = importExportService;
        this.cacheService = cacheService;
        this.kafkaPublisher = kafkaPublisher;
    }

    @GetMapping
    @Operation(summary = "List all prompt templates")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "List of prompt templates"),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public ResponseEntity<ResponseEnvelope<List<PromptResponse>>> listPrompts(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String q) {
        List<PromptTemplateEntity> templates;
        if (q != null && !q.isBlank()) {
            templates = searchService.search(q);
        } else if (category != null && !category.isBlank()) {
            templates = searchService.findByCategory(UUID.fromString(category));
        } else if (status != null && !status.isBlank()) {
            templates = searchService.findByStatus(status);
        } else {
            templates = promptService.listTemplates();
        }
        return ResponseEntity.ok(ResponseEnvelope.ok(
                templates.stream().map(PromptResponse::from).collect(Collectors.toList())));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a prompt template by ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Prompt template found"),
        @ApiResponse(responseCode = "404", description = "Template not found", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public ResponseEntity<ResponseEnvelope<PromptResponse>> getPrompt(@PathVariable UUID id) {
        PromptTemplateEntity template = promptService.getTemplate(id);
        return ResponseEntity.ok(ResponseEnvelope.ok(PromptResponse.from(template)));
    }

    @PostMapping
    @Operation(summary = "Create a new prompt template")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Prompt template created"),
        @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public ResponseEntity<ResponseEnvelope<PromptResponse>> createPrompt(
            @Valid @RequestBody CreatePromptRequest request,
            @RequestParam(required = false) UUID createdBy) {
        List<PromptApplicationService.PromptVariableInput> variables = null;
        if (request.variables() != null) {
            variables = request.variables().stream()
                    .map(v -> new PromptApplicationService.PromptVariableInput(
                            v.name(), v.type(), v.required(), v.defaultValue(),
                            v.description(), v.validationRegex()))
                    .collect(Collectors.toList());
        }
        PromptTemplateEntity saved = promptService.createTemplate(
                request.categoryId(), request.name(), request.description(),
                request.templateText(), variables, createdBy);
        kafkaPublisher.publishCreated(saved.getId(), saved.getName(), createdBy);
        return ResponseEntity.created(URI.create("/api/v1/ai/prompts/" + saved.getId()))
                .body(ResponseEnvelope.ok(PromptResponse.from(saved)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a prompt template")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Prompt template updated"),
        @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
        @ApiResponse(responseCode = "404", description = "Template not found", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public ResponseEntity<ResponseEnvelope<PromptResponse>> updatePrompt(
            @PathVariable UUID id,
            @Valid @RequestBody UpdatePromptRequest request,
            @RequestParam(required = false) UUID updatedBy) {
        PromptTemplateEntity updated = promptService.updateTemplate(
                id, request.categoryId(), request.name(), request.description(),
                request.templateText(), updatedBy);
        cacheService.invalidatePrompt(id);
        kafkaPublisher.publishUpdated(id, updated.getName(), updatedBy);
        return ResponseEntity.ok(ResponseEnvelope.ok(PromptResponse.from(updated)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a prompt template")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Prompt template deleted"),
        @ApiResponse(responseCode = "404", description = "Template not found", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public ResponseEntity<Void> deletePrompt(
            @PathVariable UUID id,
            @RequestParam(required = false) UUID deletedBy) {
        promptService.deleteTemplate(id, deletedBy);
        cacheService.invalidatePrompt(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/publish")
    @Operation(summary = "Publish a prompt template")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Template published"),
        @ApiResponse(responseCode = "400", description = "Invalid state transition", content = @Content),
        @ApiResponse(responseCode = "404", description = "Template not found", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public ResponseEntity<ResponseEnvelope<PromptResponse>> publishPrompt(
            @PathVariable UUID id,
            @RequestParam(required = false) UUID publishedBy) {
        PromptTemplateEntity published = lifecycleService.publish(id, publishedBy);
        cacheService.invalidatePrompt(id);
        kafkaPublisher.publishPublished(id, published.getCurrentVersion(), publishedBy);
        return ResponseEntity.ok(ResponseEnvelope.ok(PromptResponse.from(published)));
    }

    @PostMapping("/{id}/rollback")
    @Operation(summary = "Rollback to a specific version")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Template rolled back"),
        @ApiResponse(responseCode = "400", description = "Invalid version", content = @Content),
        @ApiResponse(responseCode = "404", description = "Template or version not found", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public ResponseEntity<ResponseEnvelope<VersionResponse>> rollbackPrompt(
            @PathVariable UUID id,
            @RequestParam int version,
            @RequestParam(required = false) UUID rolledBackBy) {
        PromptVersionEntity rolledBack = versionService.rollback(id, version, rolledBackBy);
        cacheService.invalidatePrompt(id);
        kafkaPublisher.publishRolledBack(id, version, rolledBack.getVersionNumber(), rolledBackBy);
        return ResponseEntity.ok(ResponseEnvelope.ok(VersionResponse.from(rolledBack)));
    }

    @PostMapping("/render")
    @Operation(summary = "Render a prompt template with variables")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Template rendered"),
        @ApiResponse(responseCode = "400", description = "Invalid variables or unresolved placeholders", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public ResponseEntity<ResponseEnvelope<RenderPromptResponse>> renderPrompt(
            @Valid @RequestBody RenderPromptRequest request) {
        try {
            String rendered = renderService.renderRaw(request.templateText(), request.variables());
            return ResponseEntity.ok(ResponseEnvelope.ok(RenderPromptResponse.ok(rendered)));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ResponseEnvelope.error("AI-006", e.getMessage()));
        }
    }

    @GetMapping("/categories")
    @Operation(summary = "List all prompt categories")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "List of categories"),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public ResponseEntity<ResponseEnvelope<List<CategoryResponse>>> listCategories() {
        List<PromptCategoryEntity> categories = categoryService.listAll();
        return ResponseEntity.ok(ResponseEnvelope.ok(
                categories.stream().map(CategoryResponse::from).collect(Collectors.toList())));
    }

    @GetMapping("/history")
    @Operation(summary = "Get prompt audit history")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Audit history"),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public ResponseEntity<ResponseEnvelope<Map<String, Object>>> getHistory(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {
        Page<com.sporekart.ai.prompt.infrastructure.persistence.PromptAuditEntity> history =
                auditService.getAllHistory(page, size);
        List<AuditResponse> items = history.getContent().stream().map(AuditResponse::from).collect(Collectors.toList());
        Map<String, Object> result = new HashMap<>();
        result.put("items", items);
        result.put("page", history.getNumber());
        result.put("size", history.getSize());
        result.put("totalElements", history.getTotalElements());
        result.put("totalPages", history.getTotalPages());
        return ResponseEntity.ok(ResponseEnvelope.ok(result));
    }

    @GetMapping("/{id}/versions")
    @Operation(summary = "List versions of a prompt template")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "List of versions"),
        @ApiResponse(responseCode = "404", description = "Template not found", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public ResponseEntity<ResponseEnvelope<List<VersionResponse>>> listVersions(@PathVariable UUID id) {
        List<PromptVersionEntity> versions = versionService.listVersions(id);
        return ResponseEntity.ok(ResponseEnvelope.ok(
                versions.stream().map(VersionResponse::from).collect(Collectors.toList())));
    }

    @GetMapping("/{id}/history")
    @Operation(summary = "Get audit history for a specific template")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Template audit history"),
        @ApiResponse(responseCode = "404", description = "Template not found", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public ResponseEntity<ResponseEnvelope<List<AuditResponse>>> getTemplateHistory(@PathVariable UUID id) {
        return ResponseEntity.ok(ResponseEnvelope.ok(
                auditService.getHistoryForTemplate(id).stream().map(AuditResponse::from).collect(Collectors.toList())));
    }

    @PostMapping("/{id}/submit")
    @Operation(summary = "Submit a prompt for approval")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Submitted for approval"),
        @ApiResponse(responseCode = "400", description = "Invalid state transition", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public ResponseEntity<ResponseEnvelope<PromptResponse>> submitForApproval(
            @PathVariable UUID id,
            @RequestParam(required = false) UUID submittedBy) {
        PromptTemplateEntity submitted = lifecycleService.submitForApproval(id, submittedBy);
        return ResponseEntity.ok(ResponseEnvelope.ok(PromptResponse.from(submitted)));
    }

    @PostMapping("/{id}/approve")
    @Operation(summary = "Approve a pending prompt")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Prompt approved"),
        @ApiResponse(responseCode = "400", description = "Not in pending approval state", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public ResponseEntity<ResponseEnvelope<PromptResponse>> approvePrompt(
            @PathVariable UUID id,
            @RequestParam(required = false) UUID approvedBy) {
        PromptTemplateEntity approved = lifecycleService.approve(id, approvedBy);
        return ResponseEntity.ok(ResponseEnvelope.ok(PromptResponse.from(approved)));
    }

    @PostMapping("/{id}/deprecate")
    @Operation(summary = "Deprecate a prompt template")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Template deprecated"),
        @ApiResponse(responseCode = "400", description = "Invalid state transition", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public ResponseEntity<ResponseEnvelope<PromptResponse>> deprecatePrompt(
            @PathVariable UUID id,
            @RequestParam(required = false) UUID deprecatedBy) {
        PromptTemplateEntity deprecated = lifecycleService.deprecate(id, deprecatedBy);
        cacheService.invalidatePrompt(id);
        kafkaPublisher.publishDeprecated(id, deprecatedBy);
        return ResponseEntity.ok(ResponseEnvelope.ok(PromptResponse.from(deprecated)));
    }

    @PostMapping("/import")
    @Operation(summary = "Import prompts from JSON")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Prompts imported"),
        @ApiResponse(responseCode = "400", description = "Invalid import format", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public ResponseEntity<ResponseEnvelope<Map<String, Integer>>> importPrompts(
            @RequestBody String jsonContent,
            @RequestParam(required = false) UUID importedBy) {
        int count = importExportService.importPrompts(jsonContent, importedBy);
        cacheService.invalidateAll();
        return ResponseEntity.ok(ResponseEnvelope.ok(Map.of("imported", count)));
    }

    @GetMapping("/export")
    @Operation(summary = "Export all prompts as JSON")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Prompts exported"),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public ResponseEntity<String> exportPrompts() {
        String json = importExportService.exportAll();
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(json);
    }

    @GetMapping("/versions/{versionId}")
    @Operation(summary = "Get a specific version by ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Version found"),
        @ApiResponse(responseCode = "404", description = "Version not found", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public ResponseEntity<ResponseEnvelope<VersionResponse>> getVersion(@PathVariable UUID versionId) {
        PromptVersionEntity version = versionService.findVersion(
                versionId, 0);
        java.util.Optional<PromptVersionEntity> found = java.util.Optional.empty();
        try {
            found = versionService.listVersions(versionId)
                    .stream()
                    .filter(v -> v.getId().equals(versionId))
                    .findFirst();
        } catch (Exception ignored) {}
        return found.map(v -> ResponseEntity.ok(ResponseEnvelope.ok(VersionResponse.from(v))))
                .orElse(ResponseEntity.notFound().build());
    }

    @ExceptionHandler(PromptNotFoundException.class)
    public ResponseEntity<ResponseEnvelope<Void>> handleNotFound(PromptNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ResponseEnvelope.error("AI-012", e.getMessage()));
    }

    @ExceptionHandler(PromptValidationException.class)
    public ResponseEntity<ResponseEnvelope<Void>> handleValidation(PromptValidationException e) {
        return ResponseEntity.badRequest()
                .body(ResponseEnvelope.error("AI-006", e.getMessage()));
    }

    @ExceptionHandler(com.sporekart.ai.prompt.application.PromptLifecycleException.class)
    public ResponseEntity<ResponseEnvelope<Void>> handleLifecycle(
            com.sporekart.ai.prompt.application.PromptLifecycleException e) {
        return ResponseEntity.badRequest()
                .body(ResponseEnvelope.error("AI-006", e.getMessage()));
    }

    @ExceptionHandler(com.sporekart.ai.prompt.application.PromptRenderException.class)
    public ResponseEntity<ResponseEnvelope<Void>> handleRender(
            com.sporekart.ai.prompt.application.PromptRenderException e) {
        return ResponseEntity.badRequest()
                .body(ResponseEnvelope.error("AI-008", e.getMessage()));
    }
}
