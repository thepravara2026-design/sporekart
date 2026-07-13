package com.sporekart.ai.promptregistry.interfaces.rest;

import com.sporekart.ai.core.domain.ResponseEnvelope;
import com.sporekart.ai.promptregistry.api.PromptRegistryService;
import com.sporekart.ai.promptregistry.api.PromptSearchService;
import com.sporekart.ai.promptregistry.api.PromptValidationService;
import com.sporekart.ai.promptregistry.application.PromptRegistryException;
import com.sporekart.ai.promptregistry.domain.PromptComparisonResult;
import com.sporekart.ai.promptregistry.domain.PromptRegistryEntry;
import com.sporekart.ai.promptregistry.domain.PromptStatus;
import com.sporekart.ai.promptregistry.domain.PromptVersionInfo;
import com.sporekart.ai.promptregistry.interfaces.rest.dto.PromptComparisonDto;
import com.sporekart.ai.promptregistry.interfaces.rest.dto.PromptRegistryRequestDto;
import com.sporekart.ai.promptregistry.interfaces.rest.dto.PromptRegistryResponseDto;
import com.sporekart.ai.promptregistry.interfaces.rest.dto.PromptSearchRequestDto;
import com.sporekart.ai.promptregistry.interfaces.rest.dto.PromptVersionHistoryDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/prompt-registry")
public class PromptRegistryController {

    private final PromptRegistryService registryService;
    private final PromptValidationService validationService;
    private final PromptSearchService searchService;

    public PromptRegistryController(PromptRegistryService registryService,
                                    PromptValidationService validationService,
                                    PromptSearchService searchService) {
        this.registryService = registryService;
        this.validationService = validationService;
        this.searchService = searchService;
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseEnvelope<PromptRegistryResponseDto>> register(
            @Valid @RequestBody PromptRegistryRequestDto request) {
        PromptRegistryEntry saved = registryService.registerPrompt(toEntry(request));
        return ResponseEntity.ok(ResponseEnvelope.ok(toResponse(saved)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseEnvelope<PromptRegistryResponseDto>> update(
            @PathVariable String id, @Valid @RequestBody PromptRegistryRequestDto request) {
        request.setPromptId(id);
        PromptRegistryEntry saved = registryService.updatePrompt(toEntry(request));
        return ResponseEntity.ok(ResponseEnvelope.ok(toResponse(saved)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseEnvelope<PromptRegistryResponseDto>> get(@PathVariable String id) {
        return registryService.getPrompt(id)
                .map(entry -> ResponseEntity.ok(ResponseEnvelope.ok(toResponse(entry))))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ResponseEnvelope.error("NOT_FOUND", "Prompt not found: " + id)));
    }

    @GetMapping("/list")
    public ResponseEntity<ResponseEnvelope<List<PromptRegistryResponseDto>>> list() {
        List<PromptRegistryResponseDto> result = new ArrayList<>();
        for (PromptRegistryEntry entry : registryService.listPrompts()) {
            result.add(toResponse(entry));
        }
        return ResponseEntity.ok(ResponseEnvelope.ok(result));
    }

    @GetMapping("/{id}/versions")
    public ResponseEntity<ResponseEnvelope<List<PromptVersionHistoryDto>>> versions(@PathVariable String id) {
        List<PromptVersionHistoryDto> result = new ArrayList<>();
        for (PromptVersionInfo info : registryService.getVersions(id)) {
            result.add(new PromptVersionHistoryDto(null, id, info.version(), info.status(),
                    info.changeSummary(), info.createdAt()));
        }
        return ResponseEntity.ok(ResponseEnvelope.ok(result));
    }

    @PostMapping("/{id}/rollback")
    public ResponseEntity<ResponseEnvelope<PromptRegistryResponseDto>> rollback(
            @PathVariable String id, @RequestParam int version) {
        PromptRegistryEntry saved = registryService.rollbackToVersion(id, version);
        return ResponseEntity.ok(ResponseEnvelope.ok(toResponse(saved)));
    }

    @GetMapping("/{id}/compare")
    public ResponseEntity<ResponseEnvelope<PromptComparisonDto>> compare(
            @PathVariable String id, @RequestParam int v1, @RequestParam int v2) {
        PromptComparisonResult result = registryService.compareVersions(id, v1, v2);
        return ResponseEntity.ok(ResponseEnvelope.ok(new PromptComparisonDto(
                result.promptId(), result.versionA(), result.versionB(), result.differences())));
    }

    @PostMapping("/validate")
    public ResponseEntity<ResponseEnvelope<List<String>>> validate(
            @Valid @RequestBody PromptRegistryRequestDto request) {
        PromptRegistryEntry entry = toEntry(request);
        List<String> errors = new ArrayList<>();
        errors.addAll(validationService.validateStructure(entry));
        errors.addAll(validationService.validateVariables(entry));
        errors.addAll(validationService.validateLength(entry));
        return ResponseEntity.ok(ResponseEnvelope.ok(errors));
    }

    @GetMapping("/search")
    public ResponseEntity<ResponseEnvelope<List<PromptRegistryResponseDto>>> search(
            @ModelAttribute PromptSearchRequestDto request) {
        List<PromptRegistryEntry> matched;
        if (request.getStatus() != null) {
            matched = searchService.searchByStatus(request.getStatus());
        } else if (request.getTags() != null && !request.getTags().isEmpty()) {
            matched = searchService.searchByTags(request.getTags());
        } else if (request.getName() != null && !request.getName().isBlank()) {
            matched = searchService.searchByName(request.getName());
        } else {
            matched = registryService.listPrompts();
        }
        List<PromptRegistryResponseDto> result = new ArrayList<>();
        for (PromptRegistryEntry entry : matched) {
            result.add(toResponse(entry));
        }
        return ResponseEntity.ok(ResponseEnvelope.ok(result));
    }

    @ExceptionHandler(PromptRegistryException.class)
    public ResponseEntity<ResponseEnvelope<Void>> handleNotFound(PromptRegistryException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ResponseEnvelope.error("NOT_FOUND", ex.getMessage()));
    }

    private PromptRegistryEntry toEntry(PromptRegistryRequestDto request) {
        PromptRegistryEntry entry = new PromptRegistryEntry();
        entry.setPromptId(request.getPromptId());
        entry.setPromptName(request.getPromptName());
        entry.setDescription(request.getDescription());
        entry.setPromptText(request.getPromptText());
        entry.setVersion(request.getVersion());
        entry.setOwner(request.getOwner());
        entry.setTags(request.getTags());
        entry.setStatus(request.getStatus());
        entry.setPreviousVersionId(request.getPreviousVersionId());
        entry.setMetadata(request.getMetadata());
        return entry;
    }

    private PromptRegistryResponseDto toResponse(PromptRegistryEntry entry) {
        return new PromptRegistryResponseDto(
                entry.getPromptId(), entry.getPromptName(), entry.getDescription(),
                entry.getPromptText(), entry.getVersion(), entry.getOwner(),
                entry.getTags(), entry.getStatus(), entry.getPreviousVersionId(),
                entry.getMetadata(), entry.getCreatedAt(), entry.getUpdatedAt());
    }
}
