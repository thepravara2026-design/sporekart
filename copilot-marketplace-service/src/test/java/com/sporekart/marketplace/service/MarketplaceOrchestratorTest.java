package com.sporekart.marketplace.service;

import com.sporekart.marketplace.domain.PluginInstance;
import com.sporekart.marketplace.domain.PluginHealthStatus;
import com.sporekart.marketplace.domain.PluginState;
import com.sporekart.marketplace.domain.PluginVersion;
import com.sporekart.marketplace.health.PluginHealthMonitor;
import com.sporekart.marketplace.infrastructure.monitoring.MarketplaceMetricsService;
import com.sporekart.marketplace.lifecycle.PluginLifecycleManager;
import com.sporekart.marketplace.lifecycle.VersionManager;
import com.sporekart.marketplace.loader.PluginLoader;
import com.sporekart.marketplace.permission.PermissionManager;
import com.sporekart.marketplace.registry.CapabilityRegistry;
import com.sporekart.marketplace.registry.PluginRegistry;
import com.sporekart.marketplace.sdk.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MarketplaceOrchestratorTest {

    @Mock
    private PluginRegistry registry;
    @Mock
    private CapabilityRegistry capabilityRegistry;
    @Mock
    private PluginLifecycleManager lifecycleManager;
    @Mock
    private VersionManager versionManager;
    @Mock
    private PluginLoader pluginLoader;
    @Mock
    private PermissionManager permissionManager;
    @Mock
    private PluginHealthMonitor healthMonitor;
    @Mock
    private MarketplaceMetricsService metricsService;

    private MarketplaceOrchestrator orchestrator;

    @BeforeEach
    void setUp() {
        orchestrator = new MarketplaceOrchestrator(
            registry, capabilityRegistry, lifecycleManager, versionManager,
            pluginLoader, permissionManager, healthMonitor, metricsService
        );
    }

    @Test
    void installPlugin_shouldCreatePluginAndRegisterComponents() {
        var installed = new PluginInstance("test-plugin",
            new PluginManifest("test-plugin", "Test Plugin", "1.0.0", "Author",
                "Desc", PluginType.AI_COPILOT, List.of(PluginCapability.CONVERSATION),
                List.of(PluginPermission.READ_PRODUCTS, PluginPermission.EXECUTE_AI),
                List.of(), "1.0.0", "2.0.0", "/health", Map.of(), "Impl"),
            new PluginMetadata("test-plugin", "1.0.0", Instant.now(), Instant.now(), null, "local", Map.of()));
        when(lifecycleManager.install(any())).thenReturn(installed);

        var result = orchestrator.installPlugin("test-plugin", "marketplace");

        assertNotNull(result);
        assertEquals("test-plugin", result.getId());
        verify(permissionManager).approve("test-plugin", PluginPermission.READ_PRODUCTS);
        verify(permissionManager).approve("test-plugin", PluginPermission.EXECUTE_AI);
        verify(versionManager).recordVersion(eq("test-plugin"), any(PluginVersion.class));
        verify(capabilityRegistry).register(eq("test-plugin"), anyString(), eq(PluginCapability.CONVERSATION));
        verify(metricsService).recordPluginInstall();
        verify(metricsService).recordQueryLatency(anyLong());
    }

    @Test
    void installPlugin_shouldRecordLatencyEvenOnException() {
        when(lifecycleManager.install(any())).thenThrow(new RuntimeException("Install failed"));

        assertThrows(RuntimeException.class,
            () -> orchestrator.installPlugin("test-plugin", "marketplace"));

        verify(metricsService).recordQueryLatency(anyLong());
        verify(metricsService, never()).recordPluginInstall();
    }

    @Test
    void enablePlugin_shouldDelegateToLifecycleManager() {
        var instance = new PluginInstance("test-plugin",
            mock(PluginManifest.class), mock(PluginMetadata.class));
        when(lifecycleManager.enable("test-plugin", Map.of())).thenReturn(Optional.of(instance));

        var result = orchestrator.enablePlugin("test-plugin");

        assertTrue(result.isPresent());
        assertEquals("test-plugin", result.get().getId());
        verify(metricsService).recordQueryLatency(anyLong());
    }

    @Test
    void enablePlugin_shouldReturnEmptyWhenNotFound() {
        when(lifecycleManager.enable("unknown", Map.of())).thenReturn(Optional.empty());

        var result = orchestrator.enablePlugin("unknown");
        assertFalse(result.isPresent());
    }

    @Test
    void disablePlugin_shouldDelegateToLifecycleManager() {
        var instance = new PluginInstance("test-plugin",
            mock(PluginManifest.class), mock(PluginMetadata.class));
        when(lifecycleManager.disable("test-plugin")).thenReturn(Optional.of(instance));

        var result = orchestrator.disablePlugin("test-plugin");

        assertTrue(result.isPresent());
        verify(metricsService).recordQueryLatency(anyLong());
    }

    @Test
    void disablePlugin_shouldReturnEmptyWhenNotFound() {
        when(lifecycleManager.disable("unknown")).thenReturn(Optional.empty());

        assertFalse(orchestrator.disablePlugin("unknown").isPresent());
    }

    @Test
    void updatePlugin_shouldUpdateVersionAndRecordMetric() {
        var manifest = new PluginManifest(
            "test-plugin", "Test", "1.0.0", "A", "D",
            PluginType.AI_COPILOT, List.of(PluginCapability.CONVERSATION),
            List.of(PluginPermission.READ_PRODUCTS), List.of(),
            "1.0.0", "2.0.0", "/health", Map.of(), "Impl"
        );
        var metadata = new PluginMetadata("test-plugin", "1.0.0",
            Instant.now(), Instant.now(), null, "local", Map.of());
        var existing = new PluginInstance("test-plugin", manifest, metadata);
        existing.setState(PluginState.INSTALLED);

        when(registry.get("test-plugin")).thenReturn(Optional.of(existing));

        var result = orchestrator.updatePlugin("test-plugin");

        assertTrue(result.isPresent());
        assertEquals(PluginState.ENABLED, result.get().getState());
        verify(metricsService).recordPluginUpdate();
        verify(metricsService).recordQueryLatency(anyLong());
    }

    @Test
    void updatePlugin_shouldReturnEmptyWhenNotFound() {
        when(registry.get("unknown")).thenReturn(Optional.empty());

        assertFalse(orchestrator.updatePlugin("unknown").isPresent());
        verify(metricsService).recordQueryLatency(anyLong());
        verify(metricsService, never()).recordPluginUpdate();
    }

    @Test
    void uninstallPlugin_shouldCleanupAllRegistrations() {
        orchestrator.uninstallPlugin("test-plugin");

        verify(lifecycleManager).uninstall("test-plugin");
        verify(permissionManager).revokeAll("test-plugin");
        verify(capabilityRegistry).unregisterAll("test-plugin");
        verify(metricsService).recordPluginUninstall();
        verify(metricsService).recordQueryLatency(anyLong());
    }

    @Test
    void getAvailablePlugins_shouldReturnFivePlugins() {
        var available = orchestrator.getAvailablePlugins();
        assertEquals(5, available.size());
    }

    @Test
    void getPluginHealth_shouldBuildHealthResponse() {
        var healthStatus = new PluginHealthStatus("p1", "HEALTHY", Instant.now(), 10, Map.of(), 0);
        when(healthMonitor.getAllHealth()).thenReturn(List.of(healthStatus));
        when(registry.getAll()).thenReturn(List.of(mock(PluginInstance.class)));
        when(healthMonitor.getHealthyCount()).thenReturn(1L);
        when(healthMonitor.getUnhealthyCount()).thenReturn(0L);
        when(healthMonitor.getHealthSummary()).thenReturn(Map.of("total", 1));

        var response = orchestrator.getPluginHealth();

        assertEquals(1, response.totalPlugins());
        assertEquals(1, response.healthy());
        assertEquals(0, response.unhealthy());
        assertEquals(0, response.unknown());
        assertEquals(1, response.statuses().size());
        assertNotNull(response.summary());
    }

    @Test
    void executePluginAction_shouldDelegateToLifecycleManager() {
        var result = Map.<String, Object>of("output", "success");
        when(lifecycleManager.executeAction("test-plugin", "process", Map.of("key", "val")))
            .thenReturn(Optional.of(result));

        var response = orchestrator.executePluginAction("test-plugin", "process", Map.of("key", "val"));

        assertTrue(response.isPresent());
        assertEquals("success", response.get().get("output"));
        verify(metricsService).recordQueryLatency(anyLong());
    }

    @Test
    void executePluginAction_shouldReturnEmptyWhenPluginNotFound() {
        when(lifecycleManager.executeAction("unknown", "run", Map.of()))
            .thenReturn(Optional.empty());

        assertFalse(orchestrator.executePluginAction("unknown", "run", Map.of()).isPresent());
    }

    @Test
    void installPlugin_shouldHandleNullCapabilitiesGracefully() {
        var nullCapsManifest = new PluginManifest(
            "test-null", "Test", "1.0.0", "A", "D",
            PluginType.AI_COPILOT, null,
            List.of(PluginPermission.READ_PRODUCTS),
            List.of(), "1.0.0", "2.0.0", "/health", Map.of(), "Impl"
        );
        var installed = new PluginInstance("test-null", nullCapsManifest, new PluginMetadata(
            "test-null", "1.0.0", Instant.now(), Instant.now(), null, "local", Map.of()));
        when(lifecycleManager.install(any())).thenReturn(installed);

        var result = orchestrator.installPlugin("test-null", "local");

        assertNotNull(result);
        verify(capabilityRegistry).register(anyString(), anyString(), eq(PluginCapability.CONVERSATION));
        verify(capabilityRegistry, never()).register(anyString(), anyString(), (PluginCapability) isNull());
    }
}
