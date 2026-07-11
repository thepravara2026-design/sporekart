package com.sporekart.ai.interfaces.rest;

import com.sporekart.ai.core.api.ProviderHealth;
import com.sporekart.ai.core.api.ProviderHealthService;
import com.sporekart.ai.core.domain.ResponseEnvelope;
import com.sporekart.ai.provider.api.ProviderPort;
import com.sporekart.ai.provider.application.*;
import com.sporekart.ai.provider.domain.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/ai/providers")
@Tag(name = "AI Provider", description = "Enterprise AI Provider Abstraction — registry, capabilities, health, configuration")
public class ProviderController {

    private final ProviderRegistryImpl registry;
    private final ProviderFactoryImpl factory;
    private final ProviderHealthServiceImpl healthService;
    private final ProviderConfigurationService configurationService;
    private final ProviderSelectorImpl selector;
    private final ProviderValidatorImpl validator;

    public ProviderController(ProviderRegistryImpl registry, ProviderFactoryImpl factory,
            ProviderHealthServiceImpl healthService, ProviderConfigurationService configurationService,
            ProviderSelectorImpl selector, ProviderValidatorImpl validator) {
        this.registry = registry;
        this.factory = factory;
        this.healthService = healthService;
        this.configurationService = configurationService;
        this.selector = selector;
        this.validator = validator;
    }

    @GetMapping
    @Operation(summary = "List all registered providers")
    public ResponseEntity<ResponseEnvelope<List<Map<String, Object>>>> listProviders() {
        List<Map<String, Object>> providers = registry.all().stream()
                .map(this::toProviderSummary)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ResponseEnvelope.ok(providers));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get provider details by ID")
    public ResponseEntity<ResponseEnvelope<Map<String, Object>>> getProvider(@PathVariable String id) {
        var port = registry.findByType(id.toUpperCase());
        if (port.isEmpty()) {
            return ResponseEntity.ok(ResponseEnvelope.error("AI-010", "Provider not found: " + id));
        }
        Map<String, Object> details = toProviderDetail(port.get());
        return ResponseEntity.ok(ResponseEnvelope.ok(details));
    }

    @GetMapping("/capabilities")
    @Operation(summary = "List all provider capabilities")
    public ResponseEntity<ResponseEnvelope<Map<String, Object>>> listCapabilities() {
        Map<String, Object> caps = new LinkedHashMap<>();
        registry.all().forEach(port -> {
            String name = port.supports("GEMINI") ? "GEMINI" :
                    port.supports("OPENAI") ? "OPENAI" :
                    port.supports("CLAUDE") ? "CLAUDE" :
                    port.supports("AZURE_OPENAI") ? "AZURE_OPENAI" :
                    port.supports("BEDROCK") ? "BEDROCK" :
                    port.supports("OLLAMA") ? "OLLAMA" :
                    port.supports("MISTRAL") ? "MISTRAL" : "LOCAL_LLM";
            caps.put(name, port.capabilities().stream()
                    .map(Enum::name)
                    .collect(Collectors.toList()));
        });
        return ResponseEntity.ok(ResponseEnvelope.ok(caps));
    }

    @GetMapping("/health")
    @Operation(summary = "Get health status of all providers")
    public ResponseEntity<ResponseEnvelope<List<ProviderHealth>>> getHealth() {
        List<ProviderHealth> healthStatuses = healthService.checkAllProviders();
        return ResponseEntity.ok(ResponseEnvelope.ok(healthStatuses));
    }

    @PostMapping("/switch")
    @Operation(summary = "Switch active provider for a module")
    public ResponseEntity<ResponseEnvelope<Map<String, String>>> switchProvider(
            @RequestBody Map<String, String> request) {
        String module = request.get("module");
        String provider = request.get("provider");
        if (module == null || provider == null) {
            return ResponseEntity.ok(ResponseEnvelope.error("AI-006", "module and provider are required"));
        }
        var selected = selector.select(module, provider);
        Map<String, String> result = new LinkedHashMap<>();
        result.put("module", module);
        result.put("selectedProvider", selected.name());
        result.put("status", "SWITCHED");
        return ResponseEntity.ok(ResponseEnvelope.ok(result));
    }

    @PostMapping("/validate")
    @Operation(summary = "Validate a provider configuration")
    public ResponseEntity<ResponseEnvelope<Map<String, Object>>> validateProvider(
            @RequestBody Map<String, String> request) {
        String providerName = request.get("provider");
        if (providerName == null) {
            return ResponseEntity.ok(ResponseEnvelope.error("AI-006", "provider is required"));
        }
        boolean valid = validator.validateProvider(providerName.toUpperCase());
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("provider", providerName);
        result.put("valid", valid);
        result.put("error", valid ? null : validator.getValidationError());
        return ResponseEntity.ok(ResponseEnvelope.ok(result));
    }

    private Map<String, Object> toProviderSummary(ProviderPort port) {
        Map<String, Object> summary = new LinkedHashMap<>();
        summary.put("available", port.isAvailable());
        summary.put("capabilities", port.capabilities().stream().map(Enum::name).collect(Collectors.toList()));
        return summary;
    }

    private Map<String, Object> toProviderDetail(ProviderPort port) {
        Map<String, Object> detail = new LinkedHashMap<>();
        detail.put("available", port.isAvailable());
        detail.put("capabilities", port.capabilities().stream().map(Enum::name).collect(Collectors.toList()));
        return detail;
    }
}
