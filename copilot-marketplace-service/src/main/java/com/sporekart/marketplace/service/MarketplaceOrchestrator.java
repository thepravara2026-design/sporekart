package com.sporekart.marketplace.service;

import com.sporekart.marketplace.domain.*;
import com.sporekart.marketplace.dto.PluginHealthResponse;
import com.sporekart.marketplace.health.PluginHealthMonitor;
import com.sporekart.marketplace.loader.PluginLoader;
import com.sporekart.marketplace.lifecycle.PluginLifecycleManager;
import com.sporekart.marketplace.lifecycle.VersionManager;
import com.sporekart.marketplace.permission.PermissionManager;
import com.sporekart.marketplace.registry.CapabilityRegistry;
import com.sporekart.marketplace.registry.PluginRegistry;
import com.sporekart.marketplace.sdk.*;
import com.sporekart.marketplace.infrastructure.monitoring.MarketplaceMetricsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Service
public class MarketplaceOrchestrator {

    private static final Logger log = LoggerFactory.getLogger(MarketplaceOrchestrator.class);

    private final PluginRegistry registry;
    private final CapabilityRegistry capabilityRegistry;
    private final PluginLifecycleManager lifecycleManager;
    private final VersionManager versionManager;
    private final PluginLoader pluginLoader;
    private final PermissionManager permissionManager;
    private final PluginHealthMonitor healthMonitor;
    private final MarketplaceMetricsService metricsService;

    public MarketplaceOrchestrator(PluginRegistry registry, CapabilityRegistry capabilityRegistry,
                                    PluginLifecycleManager lifecycleManager, VersionManager versionManager,
                                    PluginLoader pluginLoader, PermissionManager permissionManager,
                                    PluginHealthMonitor healthMonitor, MarketplaceMetricsService metricsService) {
        this.registry = registry;
        this.capabilityRegistry = capabilityRegistry;
        this.lifecycleManager = lifecycleManager;
        this.versionManager = versionManager;
        this.pluginLoader = pluginLoader;
        this.permissionManager = permissionManager;
        this.healthMonitor = healthMonitor;
        this.metricsService = metricsService;
    }

    public PluginInstance installPlugin(String pluginId, String source) {
        long start = System.currentTimeMillis();
        try {
            var manifest = new PluginManifest(
                pluginId, pluginId, "1.0.0", "SporeKart",
                pluginId + " plugin", PluginType.AI_COPILOT,
                List.of(PluginCapability.CONVERSATION),
                List.of(PluginPermission.READ_PRODUCTS, PluginPermission.EXECUTE_AI),
                List.of(), "1.0.0", "2.0.0",
                "/health", Map.of(), pluginId + ".PluginImpl"
            );
            var metadata = new PluginMetadata(pluginId, "1.0.0", Instant.now(), Instant.now(), null, source, Map.of());
            var instance = new PluginInstance(pluginId, manifest, metadata);

            var installed = lifecycleManager.install(instance);

            versionManager.recordVersion(pluginId, new PluginVersion("1.0.0", "1.0.0", "2.0.0", 100));

            capabilityRegistry.register(pluginId, manifest.name(), PluginCapability.CONVERSATION);
            if (manifest.capabilities() != null) {
                manifest.capabilities().stream()
                    .filter(c -> c != PluginCapability.CONVERSATION)
                    .forEach(cap -> capabilityRegistry.register(pluginId, manifest.name(), cap));
            }

            manifest.requiredPermissions().forEach(p -> permissionManager.approve(pluginId, p));

            metricsService.recordPluginInstall();
            log.info("Plugin installed: {}", pluginId);
            return installed;
        } finally {
            metricsService.recordQueryLatency(System.currentTimeMillis() - start);
        }
    }

    public Optional<PluginInstance> enablePlugin(String pluginId) {
        long start = System.currentTimeMillis();
        try {
            return lifecycleManager.enable(pluginId, Map.of());
        } finally {
            metricsService.recordQueryLatency(System.currentTimeMillis() - start);
        }
    }

    public Optional<PluginInstance> disablePlugin(String pluginId) {
        long start = System.currentTimeMillis();
        try {
            return lifecycleManager.disable(pluginId);
        } finally {
            metricsService.recordQueryLatency(System.currentTimeMillis() - start);
        }
    }

    public Optional<PluginInstance> updatePlugin(String pluginId) {
        long start = System.currentTimeMillis();
        try {
            return registry.get(pluginId).map(instance -> {
                var oldVersion = instance.getManifest().version();
                var newManifest = new PluginManifest(
                    pluginId, instance.getManifest().name(), "1.0.1",
                    instance.getManifest().author(), instance.getManifest().description(),
                    instance.getManifest().type(), instance.getManifest().capabilities(),
                    instance.getManifest().requiredPermissions(), instance.getManifest().dependencies(),
                    instance.getManifest().minPlatformVersion(), instance.getManifest().maxPlatformVersion(),
                    instance.getManifest().healthEndpoint(), instance.getManifest().configurationSchema(),
                    instance.getManifest().entryPoint()
                );
                instance.setState(PluginState.ENABLED);
                metricsService.recordPluginUpdate();
                return instance;
            });
        } finally {
            metricsService.recordQueryLatency(System.currentTimeMillis() - start);
        }
    }

    public void uninstallPlugin(String pluginId) {
        long start = System.currentTimeMillis();
        try {
            lifecycleManager.uninstall(pluginId);
            permissionManager.revokeAll(pluginId);
            capabilityRegistry.unregisterAll(pluginId);
            metricsService.recordPluginUninstall();
        } finally {
            metricsService.recordQueryLatency(System.currentTimeMillis() - start);
        }
    }

    public List<Map<String, Object>> getAvailablePlugins() {
        return List.of(
            Map.of("id", "customer-copilot", "name", "Customer Copilot", "version", "1.0.0", "type", "AI_COPILOT"),
            Map.of("id", "analytics-plugin", "name", "Analytics Plugin", "version", "1.0.0", "type", "ANALYTICS"),
            Map.of("id", "knowledge-pack", "name", "Knowledge Pack", "version", "1.0.0", "type", "KNOWLEDGE"),
            Map.of("id", "workflow-automation", "name", "Workflow Automation", "version", "1.0.0", "type", "WORKFLOW"),
            Map.of("id", "reporting-plugin", "name", "Reporting Plugin", "version", "1.0.0", "type", "REPORTING")
        );
    }

    public PluginHealthResponse getPluginHealth() {
        var allHealth = healthMonitor.getAllHealth();
        var plugins = registry.getAll();
        return new PluginHealthResponse(
            plugins.size(),
            (int) healthMonitor.getHealthyCount(),
            (int) healthMonitor.getUnhealthyCount(),
            (int) (plugins.size() - healthMonitor.getHealthyCount() - healthMonitor.getUnhealthyCount()),
            allHealth,
            healthMonitor.getHealthSummary()
        );
    }

    public Optional<Map<String, Object>> executePluginAction(String pluginId, String action, Map<String, Object> params) {
        long start = System.currentTimeMillis();
        try {
            return lifecycleManager.executeAction(pluginId, action, params);
        } finally {
            metricsService.recordQueryLatency(System.currentTimeMillis() - start);
        }
    }
}
