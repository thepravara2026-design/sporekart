package com.sporekart.ai.apiregistry.interfaces.rest;

import com.sporekart.ai.apiregistry.api.ApiDiscoveryService;
import com.sporekart.ai.apiregistry.api.ApiHealthService;
import com.sporekart.ai.apiregistry.api.ApiRegistryService;
import com.sporekart.ai.apiregistry.application.ApiRegistryException;
import com.sporekart.ai.apiregistry.domain.ApiDependency;
import com.sporekart.ai.apiregistry.domain.ApiHealthStatus;
import com.sporekart.ai.apiregistry.domain.ApiRegistryEntry;
import com.sporekart.ai.apiregistry.interfaces.rest.dto.ApiDiscoveryDto;
import com.sporekart.ai.apiregistry.interfaces.rest.dto.ApiHealthDto;
import com.sporekart.ai.apiregistry.interfaces.rest.dto.ApiRegistryRequestDto;
import com.sporekart.ai.apiregistry.interfaces.rest.dto.ApiRegistryResponseDto;
import com.sporekart.ai.core.domain.ResponseEnvelope;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/api-registry")
public class ApiRegistryController {

    private final ApiRegistryService registryService;
    private final ApiHealthService healthService;
    private final ApiDiscoveryService discoveryService;

    public ApiRegistryController(ApiRegistryService registryService,
                                ApiHealthService healthService,
                                ApiDiscoveryService discoveryService) {
        this.registryService = registryService;
        this.healthService = healthService;
        this.discoveryService = discoveryService;
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseEnvelope<ApiRegistryResponseDto>> register(
            @Valid @RequestBody ApiRegistryRequestDto request) {
        ApiRegistryEntry saved = registryService.registerApi(toEntry(request));
        return ResponseEntity.ok(ResponseEnvelope.ok(toResponse(saved)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseEnvelope<ApiRegistryResponseDto>> update(
            @PathVariable String id, @Valid @RequestBody ApiRegistryRequestDto request) {
        request.setApiId(id);
        ApiRegistryEntry saved = registryService.updateApi(toEntry(request));
        return ResponseEntity.ok(ResponseEnvelope.ok(toResponse(saved)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseEnvelope<ApiRegistryResponseDto>> get(@PathVariable String id) {
        return registryService.getApi(id)
                .map(entry -> ResponseEntity.ok(ResponseEnvelope.ok(toResponse(entry))))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ResponseEnvelope.error("NOT_FOUND", "API not found: " + id)));
    }

    @GetMapping("/list")
    public ResponseEntity<ResponseEnvelope<List<ApiRegistryResponseDto>>> list() {
        List<ApiRegistryResponseDto> result = new ArrayList<>();
        for (ApiRegistryEntry entry : registryService.listApis()) {
            result.add(toResponse(entry));
        }
        return ResponseEntity.ok(ResponseEnvelope.ok(result));
    }

    @GetMapping("/module/{module}")
    public ResponseEntity<ResponseEnvelope<List<ApiRegistryResponseDto>>> byModule(
            @PathVariable String module) {
        List<ApiRegistryResponseDto> result = new ArrayList<>();
        for (ApiRegistryEntry entry : registryService.searchByModule(module)) {
            result.add(toResponse(entry));
        }
        return ResponseEntity.ok(ResponseEnvelope.ok(result));
    }

    @GetMapping("/path")
    public ResponseEntity<ResponseEnvelope<List<ApiRegistryResponseDto>>> byPath(
            @RequestParam("p") String path) {
        List<ApiRegistryResponseDto> result = new ArrayList<>();
        for (ApiRegistryEntry entry : registryService.searchByPath(path)) {
            result.add(toResponse(entry));
        }
        return ResponseEntity.ok(ResponseEnvelope.ok(result));
    }

    @GetMapping("/owner/{owner}")
    public ResponseEntity<ResponseEnvelope<List<ApiRegistryResponseDto>>> byOwner(
            @PathVariable String owner) {
        List<ApiRegistryResponseDto> result = new ArrayList<>();
        for (ApiRegistryEntry entry : registryService.getByOwner(owner)) {
            result.add(toResponse(entry));
        }
        return ResponseEntity.ok(ResponseEnvelope.ok(result));
    }

    @PostMapping("/{id}/health")
    public ResponseEntity<ResponseEnvelope<Void>> recordHealth(
            @PathVariable String id, @Valid @RequestBody ApiHealthDto request) {
        ApiHealthStatus status = request.getStatus() != null ? request.getStatus() : ApiHealthStatus.UNKNOWN;
        healthService.recordHealth(id, status);
        return ResponseEntity.ok(ResponseEnvelope.ok(null));
    }

    @GetMapping("/{id}/health")
    public ResponseEntity<ResponseEnvelope<ApiHealthDto>> getHealth(@PathVariable String id) {
        return healthService.getHealth(id)
                .map(status -> ResponseEntity.ok(ResponseEnvelope.ok(new ApiHealthDto(id, status, null))))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ResponseEnvelope.error("NOT_FOUND", "No health record for: " + id)));
    }

    @GetMapping("/health-report")
    public ResponseEntity<ResponseEnvelope<List<ApiHealthDto>>> healthReport() {
        List<ApiHealthDto> result = new ArrayList<>();
        for (ApiHealthService.ApiHealthReport report : healthService.getHealthReport()) {
            result.add(new ApiHealthDto(report.getApiId(), report.getStatus(), report.getCheckedAt()));
        }
        return ResponseEntity.ok(ResponseEnvelope.ok(result));
    }

    @GetMapping("/module/{module}/discover")
    public ResponseEntity<ResponseEnvelope<List<ApiDiscoveryDto>>> discover(
            @PathVariable String module) {
        List<ApiDiscoveryDto> result = new ArrayList<>();
        for (ApiRegistryEntry entry : discoveryService.discoverByModule(module)) {
            result.add(new ApiDiscoveryDto(
                    entry.getApiId(), entry.getApiName(), entry.getApiPath(),
                    entry.getModule(), entry.getOwner(),
                    discoveryService.findDependencies(entry.getApiId())));
        }
        return ResponseEntity.ok(ResponseEnvelope.ok(result));
    }

    @GetMapping("/{id}/dependencies")
    public ResponseEntity<ResponseEnvelope<List<ApiDependency>>> dependencies(
            @PathVariable String id) {
        return ResponseEntity.ok(ResponseEnvelope.ok(discoveryService.findDependencies(id)));
    }

    @GetMapping("/map")
    public ResponseEntity<ResponseEnvelope<Map<String, ApiRegistryResponseDto>>> apiMap() {
        Map<String, ApiRegistryResponseDto> result = new java.util.HashMap<>();
        for (Map.Entry<String, ApiRegistryEntry> entry : discoveryService.getApiMap().entrySet()) {
            result.put(entry.getKey(), toResponse(entry.getValue()));
        }
        return ResponseEntity.ok(ResponseEnvelope.ok(result));
    }

    @ExceptionHandler(ApiRegistryException.class)
    public ResponseEntity<ResponseEnvelope<Void>> handleNotFound(ApiRegistryException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ResponseEnvelope.error("NOT_FOUND", ex.getMessage()));
    }

    private ApiRegistryEntry toEntry(ApiRegistryRequestDto request) {
        ApiRegistryEntry entry = new ApiRegistryEntry();
        entry.setApiId(request.getApiId());
        entry.setApiName(request.getApiName());
        entry.setApiPath(request.getApiPath());
        entry.setHttpMethod(request.getHttpMethod());
        entry.setModule(request.getModule());
        entry.setOwner(request.getOwner());
        entry.setDescription(request.getDescription());
        entry.setVersion(request.getVersion());
        entry.setDeprecated(request.isDeprecated());
        entry.setDeprecationNotice(request.getDeprecationNotice());
        entry.setAuthRequired(request.isAuthRequired());
        entry.setRolesAllowed(request.getRolesAllowed());
        entry.setConsumers(request.getConsumers());
        entry.setDependencies(request.getDependencies());
        entry.setOpenApiSpec(request.getOpenApiSpec());
        return entry;
    }

    private ApiRegistryResponseDto toResponse(ApiRegistryEntry entry) {
        return new ApiRegistryResponseDto(
                entry.getApiId(), entry.getApiName(), entry.getApiPath(), entry.getHttpMethod(),
                entry.getModule(), entry.getOwner(), entry.getDescription(), entry.getVersion(),
                entry.isDeprecated(), entry.getDeprecationNotice(), entry.isAuthRequired(),
                entry.getRolesAllowed(), entry.getConsumers(), entry.getDependencies(),
                entry.getOpenApiSpec(), entry.getHealthStatus(),
                entry.getCreatedAt(), entry.getUpdatedAt());
    }
}
