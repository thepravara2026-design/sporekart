package com.sporekart.ai.providerregistry.interfaces.rest;

import com.sporekart.ai.providerregistry.api.ProviderDiscoveryService;
import com.sporekart.ai.providerregistry.api.ProviderHealthService;
import com.sporekart.ai.providerregistry.api.ProviderRegistryService;
import com.sporekart.ai.providerregistry.domain.FallbackChain;
import com.sporekart.ai.providerregistry.domain.ProviderCapability;
import com.sporekart.ai.providerregistry.domain.ProviderHealthStatus;
import com.sporekart.ai.providerregistry.domain.ProviderRegistryEntry;
import com.sporekart.ai.providerregistry.domain.ProviderStatus;
import com.sporekart.ai.providerregistry.domain.ProviderType;
import com.sporekart.ai.providerregistry.interfaces.rest.dto.ProviderDiscoveryResponseDto;
import com.sporekart.ai.providerregistry.interfaces.rest.dto.ProviderHealthDto;
import com.sporekart.ai.providerregistry.interfaces.rest.dto.ProviderRegistryRequestDto;
import com.sporekart.ai.providerregistry.interfaces.rest.dto.ProviderRegistryResponseDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/provider-registry")
@Tag(name = "Provider Registry API", description = "Enterprise AI provider registry and discovery endpoints")
public class ProviderRegistryController {

    private final ProviderRegistryService registryService;
    private final ProviderHealthService healthService;
    private final ProviderDiscoveryService discoveryService;

    public ProviderRegistryController(ProviderRegistryService registryService,
                                       ProviderHealthService healthService,
                                       ProviderDiscoveryService discoveryService) {
        this.registryService = registryService;
        this.healthService = healthService;
        this.discoveryService = discoveryService;
    }

    @PostMapping("/register")
    @Operation(summary = "Register a new AI provider")
    public ResponseEntity<ProviderRegistryResponseDto> register(
            @Valid @RequestBody ProviderRegistryRequestDto request) {
        ProviderRegistryEntry entry = toEntry(request, null);
        ProviderRegistryEntry saved = registryService.registerProvider(entry);
        return ResponseEntity.status(HttpStatus.CREATED).body(ProviderRegistryResponseDto.from(saved));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing AI provider")
    public ResponseEntity<ProviderRegistryResponseDto> update(
            @PathVariable String id,
            @Valid @RequestBody ProviderRegistryRequestDto request) {
        ProviderRegistryEntry entry = toEntry(request, id);
        ProviderRegistryEntry saved = registryService.updateProvider(entry);
        return ResponseEntity.ok(ProviderRegistryResponseDto.from(saved));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a provider by id")
    public ResponseEntity<ProviderRegistryResponseDto> get(@PathVariable String id) {
        return registryService.getProvider(id)
                .map(e -> ResponseEntity.ok(ProviderRegistryResponseDto.from(e)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/list")
    @Operation(summary = "List all registered providers")
    public ResponseEntity<List<ProviderRegistryResponseDto>> list() {
        List<ProviderRegistryResponseDto> result = registryService.listProviders().stream()
                .map(ProviderRegistryResponseDto::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/search")
    @Operation(summary = "Search providers by name")
    public ResponseEntity<List<ProviderRegistryResponseDto>> search(@RequestParam String name) {
        List<ProviderRegistryResponseDto> result = registryService.searchProviders(name).stream()
                .map(ProviderRegistryResponseDto::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get providers by status")
    public ResponseEntity<List<ProviderRegistryResponseDto>> byStatus(@PathVariable String status) {
        ProviderStatus providerStatus = ProviderStatus.valueOf(status.toUpperCase());
        List<ProviderRegistryResponseDto> result = registryService.getProvidersByStatus(providerStatus).stream()
                .map(ProviderRegistryResponseDto::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/capability/{capability}")
    @Operation(summary = "Get providers that support a capability")
    public ResponseEntity<List<ProviderRegistryResponseDto>> byCapability(@PathVariable String capability) {
        ProviderCapability cap = ProviderCapability.valueOf(capability.toUpperCase());
        List<ProviderRegistryResponseDto> result = registryService.getProvidersByCapability(cap).stream()
                .map(ProviderRegistryResponseDto::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @PostMapping("/{id}/health")
    @Operation(summary = "Record a health check for a provider")
    public ResponseEntity<ProviderHealthDto> recordHealth(
            @PathVariable String id,
            @Valid @RequestBody ProviderHealthDto request) {
        healthService.recordHealth(id, request.getStatus());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ProviderHealthDto(id, request.getStatus(), java.time.Instant.now()));
    }

    @GetMapping("/{id}/health")
    @Operation(summary = "Get the latest health status of a provider")
    public ResponseEntity<ProviderHealthDto> getHealth(@PathVariable String id) {
        Optional<ProviderHealthStatus> status = healthService.getHealth(id);
        return status.map(s -> ResponseEntity.ok(new ProviderHealthDto(id, s, java.time.Instant.now())))
                .orElseGet(() -> ResponseEntity.ok(new ProviderHealthDto(id, ProviderHealthStatus.UNKNOWN, java.time.Instant.now())));
    }

    @GetMapping("/discover/{capability}")
    @Operation(summary = "Discover providers for a capability")
    public ResponseEntity<ProviderDiscoveryResponseDto> discover(@PathVariable String capability) {
        ProviderCapability cap = ProviderCapability.valueOf(capability.toUpperCase());
        List<ProviderRegistryResponseDto> providers = discoveryService.discoverByCapability(cap).stream()
                .map(ProviderRegistryResponseDto::from)
                .collect(Collectors.toList());
        ProviderRegistryResponseDto recommended = providers.isEmpty() ? null : providers.get(0);
        return ResponseEntity.ok(new ProviderDiscoveryResponseDto(recommended, providers));
    }

    @GetMapping("/fallback/{capability}")
    @Operation(summary = "Get the fallback chain for a capability")
    public ResponseEntity<ProviderDiscoveryResponseDto> fallback(@PathVariable String capability) {
        ProviderCapability cap = ProviderCapability.valueOf(capability.toUpperCase());
        FallbackChain chain = discoveryService.getFallbackChain(cap);
        List<ProviderRegistryResponseDto> providers = chain.chain().stream()
                .map(ProviderRegistryResponseDto::from)
                .collect(Collectors.toList());
        ProviderRegistryResponseDto recommended = providers.isEmpty() ? null : providers.get(0);
        return ResponseEntity.ok(new ProviderDiscoveryResponseDto(recommended, providers));
    }

    private ProviderRegistryEntry toEntry(ProviderRegistryRequestDto request, String providerId) {
        ProviderRegistryEntry entry = new ProviderRegistryEntry();
        entry.setProviderId(providerId);
        entry.setProviderName(request.getProviderName());
        entry.setProviderType(request.getProviderType());
        entry.setVersion(request.getVersion());
        entry.setPriority(request.getPriority());
        entry.setSupportedModels(request.getSupportedModels());
        entry.setCapabilities(request.getCapabilities());
        entry.setMetadata(request.getMetadata());
        if (providerId == null) {
            entry.setStatus(ProviderStatus.ACTIVE);
        }
        return entry;
    }
}
