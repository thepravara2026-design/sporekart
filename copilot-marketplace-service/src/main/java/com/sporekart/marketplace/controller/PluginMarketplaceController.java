package com.sporekart.marketplace.controller;

import com.sporekart.marketplace.domain.CapabilityRegistration;
import com.sporekart.marketplace.domain.PluginInstance;
import com.sporekart.marketplace.dto.*;
import com.sporekart.marketplace.registry.CapabilityRegistry;
import com.sporekart.marketplace.registry.PluginRegistry;
import com.sporekart.marketplace.service.MarketplaceOrchestrator;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/plugins")
public class PluginMarketplaceController {

    private static final Logger log = LoggerFactory.getLogger(PluginMarketplaceController.class);

    private final MarketplaceOrchestrator orchestrator;
    private final PluginRegistry pluginRegistry;
    private final CapabilityRegistry capabilityRegistry;

    public PluginMarketplaceController(MarketplaceOrchestrator orchestrator,
                                       PluginRegistry pluginRegistry,
                                       CapabilityRegistry capabilityRegistry) {
        this.orchestrator = orchestrator;
        this.pluginRegistry = pluginRegistry;
        this.capabilityRegistry = capabilityRegistry;
    }

    @GetMapping
    public ResponseEntity<MarketplaceResponse<PluginListResponse>> listPlugins() {
        log.info("GET /api/v1/plugins");
        var all = pluginRegistry.getAll();
        var plugins = all.stream()
            .map(p -> PluginResponse.from(p))
            .toList();
        var response = new PluginListResponse(
            all.size(),
            (int) all.stream().filter(PluginInstance::isInstalled).count(),
            (int) all.stream().filter(PluginInstance::isEnabled).count(),
            (int) all.stream().filter(p -> p.getState().name().equals("ERROR")).count(),
            plugins
        );
        return ResponseEntity.ok(MarketplaceResponse.success(response));
    }

    @PostMapping("/install")
    public ResponseEntity<MarketplaceResponse<PluginResponse>> installPlugin(
            @Valid @RequestBody PluginInstallRequest request) {
        log.info("POST /api/v1/plugins/install pluginId={}", request.pluginId());
        var instance = orchestrator.installPlugin(request.pluginId(), request.source());
        return ResponseEntity.ok(MarketplaceResponse.success("Plugin installed", PluginResponse.from(instance)));
    }

    @PostMapping("/enable/{pluginId}")
    public ResponseEntity<MarketplaceResponse<PluginResponse>> enablePlugin(@PathVariable String pluginId) {
        log.info("POST /api/v1/plugins/enable {}", pluginId);
        var instance = orchestrator.enablePlugin(pluginId);
        return instance.map(i ->
            ResponseEntity.ok(MarketplaceResponse.success("Plugin enabled", PluginResponse.from(i))))
            .orElse(ResponseEntity.badRequest()
                .body(MarketplaceResponse.error("Plugin not found", "NOT_FOUND")));
    }

    @PostMapping("/disable/{pluginId}")
    public ResponseEntity<MarketplaceResponse<PluginResponse>> disablePlugin(@PathVariable String pluginId) {
        log.info("POST /api/v1/plugins/disable {}", pluginId);
        var instance = orchestrator.disablePlugin(pluginId);
        return instance.map(i ->
            ResponseEntity.ok(MarketplaceResponse.success("Plugin disabled", PluginResponse.from(i))))
            .orElse(ResponseEntity.badRequest()
                .body(MarketplaceResponse.error("Plugin not found", "NOT_FOUND")));
    }

    @PostMapping("/update/{pluginId}")
    public ResponseEntity<MarketplaceResponse<PluginResponse>> updatePlugin(@PathVariable String pluginId) {
        log.info("POST /api/v1/plugins/update {}", pluginId);
        var instance = orchestrator.updatePlugin(pluginId);
        return instance.map(i ->
            ResponseEntity.ok(MarketplaceResponse.success("Plugin updated", PluginResponse.from(i))))
            .orElse(ResponseEntity.badRequest()
                .body(MarketplaceResponse.error("Plugin not found", "NOT_FOUND")));
    }

    @DeleteMapping("/uninstall/{pluginId}")
    public ResponseEntity<MarketplaceResponse<Void>> uninstallPlugin(@PathVariable String pluginId) {
        log.info("DELETE /api/v1/plugins/uninstall {}", pluginId);
        orchestrator.uninstallPlugin(pluginId);
        return ResponseEntity.ok(MarketplaceResponse.success("Plugin uninstalled", null));
    }

    @GetMapping("/marketplace")
    public ResponseEntity<MarketplaceResponse<List<Map<String, Object>>>> marketplace() {
        log.info("GET /api/v1/plugins/marketplace");
        var available = orchestrator.getAvailablePlugins();
        return ResponseEntity.ok(MarketplaceResponse.success(available));
    }

    @GetMapping("/health")
    public ResponseEntity<MarketplaceResponse<PluginHealthResponse>> health() {
        log.info("GET /api/v1/plugins/health");
        var response = orchestrator.getPluginHealth();
        return ResponseEntity.ok(MarketplaceResponse.success(response));
    }

    @GetMapping("/capabilities")
    public ResponseEntity<MarketplaceResponse<List<CapabilityRegistration>>> capabilities() {
        log.info("GET /api/v1/plugins/capabilities");
        var caps = capabilityRegistry.getAll();
        return ResponseEntity.ok(MarketplaceResponse.success(caps));
    }

    @GetMapping("/{pluginId}")
    public ResponseEntity<MarketplaceResponse<PluginResponse>> getPlugin(@PathVariable String pluginId) {
        log.info("GET /api/v1/plugins/{}", pluginId);
        return pluginRegistry.get(pluginId)
            .map(i -> ResponseEntity.ok(MarketplaceResponse.success(PluginResponse.from(i))))
            .orElse(ResponseEntity.badRequest()
                .body(MarketplaceResponse.error("Plugin not found", "NOT_FOUND")));
    }

    @PostMapping("/{pluginId}/execute")
    public ResponseEntity<MarketplaceResponse<Map<String, Object>>> executeAction(
            @PathVariable String pluginId, @RequestBody Map<String, Object> request) {
        log.info("POST /api/v1/plugins/{}/execute", pluginId);
        var action = request.getOrDefault("action", "default").toString();
        var params = (Map<String, Object>) request.getOrDefault("params", Map.of());
        var result = orchestrator.executePluginAction(pluginId, action, params);
        return result.map(r ->
            ResponseEntity.ok(MarketplaceResponse.success(r)))
            .orElse(ResponseEntity.badRequest()
                .body(MarketplaceResponse.error("Plugin not found or disabled", "NOT_FOUND")));
    }
}
