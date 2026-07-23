package com.sporekart.marketplace.health;

import com.sporekart.marketplace.config.MarketplaceConfig;
import com.sporekart.marketplace.domain.PluginInstance;
import com.sporekart.marketplace.domain.PluginHealthStatus;
import com.sporekart.marketplace.registry.PluginRegistry;
import com.sporekart.marketplace.sdk.PluginManifest;
import com.sporekart.marketplace.sdk.PluginMetadata;
import com.sporekart.marketplace.sdk.PluginType;
import com.sporekart.marketplace.sdk.SporekartPlugin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PluginHealthMonitorTest {

    @Mock
    private PluginRegistry registry;

    @Mock
    private MarketplaceConfig config;

    @Mock
    private MarketplaceConfig.HealthConfig healthConfig;

    private PluginHealthMonitor monitor;

    private PluginManifest manifest;
    private PluginMetadata metadata;

    @BeforeEach
    void setUp() {
        monitor = new PluginHealthMonitor(registry, config);
        manifest = new PluginManifest(
            "test-plugin", "Test Plugin", "1.0.0", "Author",
            "Test", PluginType.AI_COPILOT, List.of(), List.of(),
            List.of(), "1.0.0", "2.0.0", "/health", Map.of(), "TestImpl"
        );
        metadata = new PluginMetadata(
            "test-plugin", "1.0.0", Instant.now(), Instant.now(), null, "local", Map.of()
        );
    }

    private SporekartPlugin createPluginWithHealthStatus(String status) {
        var plugin = mock(SporekartPlugin.class);
        when(plugin.healthCheck()).thenReturn(Map.of("status", status));
        return plugin;
    }

    @Test
    void checkPlugin_shouldReturnHealthy() {
        var plugin = createPluginWithHealthStatus("HEALTHY");
        var instance = new PluginInstance("test-plugin", manifest, metadata);
        instance.setPlugin(plugin);

        var result = monitor.checkPlugin(instance);

        assertEquals("test-plugin", result.pluginId());
        assertEquals("HEALTHY", result.status());
        assertTrue(result.isHealthy());
        assertFalse(result.isUnhealthy());
        assertEquals(0, result.consecutiveFailures());
    }

    @Test
    void checkPlugin_shouldReturnUnhealthyWhenStatusNotHealthy() {
        var plugin = createPluginWithHealthStatus("DEGRADED");
        var instance = new PluginInstance("test-plugin", manifest, metadata);
        instance.setPlugin(plugin);

        var result = monitor.checkPlugin(instance);

        assertEquals("UNHEALTHY", result.status());
        assertTrue(result.isUnhealthy());
    }

    @Test
    void checkPlugin_shouldReturnUnknownWhenPluginIsNull() {
        var instance = new PluginInstance("test-plugin", manifest, metadata);
        var result = monitor.checkPlugin(instance);

        assertEquals("UNKNOWN", result.status());
        assertFalse(result.isHealthy());
        assertFalse(result.isUnhealthy());
    }

    @Test
    void checkPlugin_shouldReturnUnhealthyOnException() {
        var plugin = mock(SporekartPlugin.class);
        when(plugin.healthCheck()).thenThrow(new RuntimeException("Plugin crashed"));
        var instance = new PluginInstance("test-plugin", manifest, metadata);
        instance.setPlugin(plugin);

        var result = monitor.checkPlugin(instance);

        assertEquals("UNHEALTHY", result.status());
        assertTrue(result.isUnhealthy());
        assertTrue(result.details().containsKey("error"));
    }

    @Test
    void checkPlugin_shouldTrackConsecutiveFailures() {
        var plugin = mock(SporekartPlugin.class);
        when(plugin.healthCheck()).thenThrow(new RuntimeException("fail"));
        var instance = new PluginInstance("test-plugin", manifest, metadata);
        instance.setPlugin(plugin);

        monitor.checkPlugin(instance);
        monitor.checkPlugin(instance);
        var result = monitor.checkPlugin(instance);

        assertEquals(2, result.consecutiveFailures());
    }

    @Test
    void checkPlugin_shouldResetConsecutiveFailuresOnRecovery() {
        var plugin = mock(SporekartPlugin.class);
        when(plugin.healthCheck())
            .thenThrow(new RuntimeException("fail"), new RuntimeException("fail"))
            .thenReturn(Map.of("status", "HEALTHY"));
        var instance = new PluginInstance("test-plugin", manifest, metadata);
        instance.setPlugin(plugin);

        monitor.checkPlugin(instance);
        monitor.checkPlugin(instance);
        var result = monitor.checkPlugin(instance);

        assertEquals("HEALTHY", result.status());
        assertEquals(0, result.consecutiveFailures());
    }

    @Test
    void getHealth_shouldReturnStatusWhenPresent() {
        var plugin = createPluginWithHealthStatus("HEALTHY");
        var instance = new PluginInstance("test-plugin", manifest, metadata);
        instance.setPlugin(plugin);
        monitor.checkPlugin(instance);

        var result = monitor.getHealth("test-plugin");
        assertTrue(result.isPresent());
        assertEquals("HEALTHY", result.get().status());
    }

    @Test
    void getHealth_shouldReturnEmptyWhenNotChecked() {
        var result = monitor.getHealth("unknown");
        assertFalse(result.isPresent());
    }

    @Test
    void getAllHealth_shouldReturnAllStatuses() {
        var p1 = createPluginWithHealthStatus("HEALTHY");
        var i1 = new PluginInstance("p1", manifest, metadata);
        i1.setPlugin(p1);
        monitor.checkPlugin(i1);

        var p2 = createPluginWithHealthStatus("UNHEALTHY");
        var manifest2 = new PluginManifest(
            "p2", "Plugin 2", "1.0.0", "Author", "Desc",
            PluginType.AI_COPILOT, List.of(), List.of(), List.of(),
            "1.0.0", "2.0.0", "/health", Map.of(), "Impl"
        );
        var i2 = new PluginInstance("p2", manifest2, new PluginMetadata(
            "p2", "1.0.0", Instant.now(), Instant.now(), null, "local", Map.of()
        ));
        i2.setPlugin(p2);
        monitor.checkPlugin(i2);

        var all = monitor.getAllHealth();
        assertEquals(2, all.size());
    }

    @Test
    void getHealthyCount_shouldReturnCorrectCount() {
        var p1 = createPluginWithHealthStatus("HEALTHY");
        var i1 = new PluginInstance("p1", manifest, metadata);
        i1.setPlugin(p1);
        monitor.checkPlugin(i1);

        var p2 = createPluginWithHealthStatus("UNHEALTHY");
        var manifest2 = new PluginManifest(
            "p2", "P2", "1.0.0", "A", "D",
            PluginType.AI_COPILOT, List.of(), List.of(), List.of(),
            "1.0.0", "2.0.0", "/health", Map.of(), "I"
        );
        var i2 = new PluginInstance("p2", manifest2, new PluginMetadata(
            "p2", "1.0.0", Instant.now(), Instant.now(), null, "local", Map.of()
        ));
        i2.setPlugin(p2);
        monitor.checkPlugin(i2);

        assertEquals(1, monitor.getHealthyCount());
        assertEquals(1, monitor.getUnhealthyCount());
    }

    @Test
    void getHealthSummary_shouldReturnSummaryWithCorrectCounts() {
        var p1 = createPluginWithHealthStatus("HEALTHY");
        var i1 = new PluginInstance("p1", manifest, metadata);
        i1.setPlugin(p1);
        monitor.checkPlugin(i1);

        var p2 = createPluginWithHealthStatus("UNHEALTHY");
        var manifest2 = new PluginManifest(
            "p2", "P2", "1.0.0", "A", "D",
            PluginType.AI_COPILOT, List.of(), List.of(), List.of(),
            "1.0.0", "2.0.0", "/health", Map.of(), "I"
        );
        var i2 = new PluginInstance("p2", manifest2, new PluginMetadata(
            "p2", "1.0.0", Instant.now(), Instant.now(), null, "local", Map.of()
        ));
        i2.setPlugin(p2);
        monitor.checkPlugin(i2);

        var summary = monitor.getHealthSummary();
        assertEquals(2, summary.get("total"));
        assertEquals(1L, summary.get("healthy"));
        assertEquals(1L, summary.get("unhealthy"));
        assertEquals(0L, summary.get("unknown"));
        assertTrue((double) summary.get("averageResponseTimeMs") >= 0);
    }

    @Test
    void getHealthSummary_shouldIncludeUnknownCount() {
        var instance = new PluginInstance("p1", manifest, metadata);
        monitor.checkPlugin(instance);

        var summary = monitor.getHealthSummary();
        assertEquals(1, summary.get("total"));
        assertEquals(0L, summary.get("healthy"));
        assertEquals(0L, summary.get("unhealthy"));
        assertEquals(1L, summary.get("unknown"));
    }
}
