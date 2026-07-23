package com.sporekart.marketplace.integration;

import com.sporekart.marketplace.domain.PluginInstance;
import com.sporekart.marketplace.domain.PluginState;
import com.sporekart.marketplace.health.PluginHealthMonitor;
import com.sporekart.marketplace.infrastructure.monitoring.MarketplaceMetricsService;
import com.sporekart.marketplace.lifecycle.PluginLifecycleManager;
import com.sporekart.marketplace.lifecycle.VersionManager;
import com.sporekart.marketplace.loader.PluginLoader;
import com.sporekart.marketplace.permission.PermissionManager;
import com.sporekart.marketplace.sdk.PluginCapability;
import com.sporekart.marketplace.sdk.PluginManifest;
import com.sporekart.marketplace.sdk.PluginMetadata;
import com.sporekart.marketplace.sdk.PluginPermission;
import com.sporekart.marketplace.sdk.PluginType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@WithMockUser
class MarketplaceIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PluginLifecycleManager lifecycleManager;

    @MockBean
    private PluginHealthMonitor healthMonitor;

    @MockBean
    private PluginLoader pluginLoader;

    @MockBean
    private PermissionManager permissionManager;

    @MockBean
    private VersionManager versionManager;

    @MockBean
    private MarketplaceMetricsService metricsService;

    private PluginInstance createTestInstance(String pluginId) {
        var manifest = new PluginManifest(
            pluginId, "Test Plugin", "1.0.0", "SporeKart",
            "Test plugin description", PluginType.AI_COPILOT,
            List.of(PluginCapability.CONVERSATION),
            List.of(PluginPermission.READ_PRODUCTS, PluginPermission.EXECUTE_AI),
            List.of(), "1.0.0", "2.0.0",
            "/health", Map.of(), pluginId + ".PluginImpl"
        );
        var metadata = new PluginMetadata(
            pluginId, "1.0.0", Instant.now(), Instant.now(), null, "marketplace", Map.of()
        );
        var instance = new PluginInstance(pluginId, manifest, metadata);
        instance.setState(PluginState.INSTALLED);
        return instance;
    }

    @Test
    void testInstallPlugin() throws Exception {
        var pluginId = "test-install-plugin";
        var instance = createTestInstance(pluginId);

        when(lifecycleManager.install(any())).thenReturn(instance);
        when(permissionManager.approve(anyString(), any())).thenReturn(true);
        doNothing().when(versionManager).recordVersion(anyString(), any());
        doNothing().when(metricsService).recordPluginInstall();

        mockMvc.perform(post("/api/v1/plugins/install")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"pluginId": "%s", "source": "marketplace"}
                    """.formatted(pluginId)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.pluginId").value(pluginId))
            .andExpect(jsonPath("$.data.state").value("INSTALLED"));

        verify(lifecycleManager).install(any());
        verify(permissionManager, times(2)).approve(anyString(), any());
        verify(metricsService).recordPluginInstall();
    }

    @Test
    void testInstallPluginValidationFailure() throws Exception {
        mockMvc.perform(post("/api/v1/plugins/install")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"pluginId": "", "source": "marketplace"}
                    """))
            .andExpect(status().isBadRequest());
    }

    @Test
    void testEnablePlugin() throws Exception {
        var pluginId = "test-enable-plugin";
        var instance = createTestInstance(pluginId);
        instance.setState(PluginState.ENABLED);

        when(lifecycleManager.enable(eq(pluginId), anyMap())).thenReturn(Optional.of(instance));

        mockMvc.perform(post("/api/v1/plugins/enable/{pluginId}", pluginId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.pluginId").value(pluginId))
            .andExpect(jsonPath("$.data.state").value("ENABLED"));

        verify(lifecycleManager).enable(eq(pluginId), anyMap());
    }

    @Test
    void testEnablePluginNotFound() throws Exception {
        when(lifecycleManager.enable(anyString(), anyMap())).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/v1/plugins/enable/{pluginId}", "nonexistent-plugin"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.success").value(false))
            .andExpect(jsonPath("$.errorCode").value("NOT_FOUND"));
    }

    @Test
    void testDisablePlugin() throws Exception {
        var pluginId = "test-disable-plugin";
        var instance = createTestInstance(pluginId);
        instance.setState(PluginState.DISABLED);

        when(lifecycleManager.disable(eq(pluginId))).thenReturn(Optional.of(instance));

        mockMvc.perform(post("/api/v1/plugins/disable/{pluginId}", pluginId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.pluginId").value(pluginId))
            .andExpect(jsonPath("$.data.state").value("DISABLED"));

        verify(lifecycleManager).disable(eq(pluginId));
    }

    @Test
    void testDisablePluginNotFound() throws Exception {
        when(lifecycleManager.disable(anyString())).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/v1/plugins/disable/{pluginId}", "nonexistent-plugin"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.success").value(false))
            .andExpect(jsonPath("$.errorCode").value("NOT_FOUND"));
    }

    @Test
    void testUninstallPlugin() throws Exception {
        var pluginId = "test-uninstall-plugin";

        when(lifecycleManager.uninstall(eq(pluginId))).thenReturn(Optional.empty());
        doNothing().when(permissionManager).revokeAll(eq(pluginId));
        doNothing().when(metricsService).recordPluginUninstall();

        mockMvc.perform(delete("/api/v1/plugins/uninstall/{pluginId}", pluginId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.message").value("Plugin uninstalled"));

        verify(lifecycleManager).uninstall(eq(pluginId));
        verify(permissionManager).revokeAll(eq(pluginId));
        verify(metricsService).recordPluginUninstall();
    }

    @Test
    void testFullLifecycle() throws Exception {
        var pluginId = "full-lifecycle-plugin";

        var installedInstance = createTestInstance(pluginId);
        when(lifecycleManager.install(any())).thenReturn(installedInstance);
        when(permissionManager.approve(anyString(), any())).thenReturn(true);
        doNothing().when(versionManager).recordVersion(anyString(), any());
        doNothing().when(metricsService).recordPluginInstall();

        mockMvc.perform(post("/api/v1/plugins/install")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"pluginId": "%s", "source": "marketplace"}
                    """.formatted(pluginId)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.state").value("INSTALLED"));

        var enabledInstance = createTestInstance(pluginId);
        enabledInstance.setState(PluginState.ENABLED);
        when(lifecycleManager.enable(eq(pluginId), anyMap())).thenReturn(Optional.of(enabledInstance));

        mockMvc.perform(post("/api/v1/plugins/enable/{pluginId}", pluginId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.state").value("ENABLED"));

        when(healthMonitor.getAllHealth()).thenReturn(List.of());
        when(healthMonitor.getHealthyCount()).thenReturn(0L);
        when(healthMonitor.getUnhealthyCount()).thenReturn(0L);
        when(healthMonitor.getHealthSummary()).thenReturn(Map.of(
            "total", 0, "healthy", 0, "unhealthy", 0, "unknown", 0,
            "averageResponseTimeMs", 0.0
        ));

        mockMvc.perform(get("/api/v1/plugins/health"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.totalPlugins").value(0));

        var disabledInstance = createTestInstance(pluginId);
        disabledInstance.setState(PluginState.DISABLED);
        when(lifecycleManager.disable(eq(pluginId))).thenReturn(Optional.of(disabledInstance));

        mockMvc.perform(post("/api/v1/plugins/disable/{pluginId}", pluginId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.state").value("DISABLED"));

        when(lifecycleManager.uninstall(eq(pluginId))).thenReturn(Optional.empty());
        doNothing().when(permissionManager).revokeAll(eq(pluginId));
        doNothing().when(metricsService).recordPluginUninstall();

        mockMvc.perform(delete("/api/v1/plugins/uninstall/{pluginId}", pluginId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.message").value("Plugin uninstalled"));
    }

    @Test
    void testListPluginsReturnsEmpty() throws Exception {
        mockMvc.perform(get("/api/v1/plugins"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.total").value(0))
            .andExpect(jsonPath("$.data.plugins").isArray());
    }

    @Test
    void testMarketplaceEndpoints() throws Exception {
        mockMvc.perform(get("/api/v1/plugins/marketplace"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").isArray());

        mockMvc.perform(get("/api/v1/plugins/capabilities"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    void testGetPluginNotFound() throws Exception {
        mockMvc.perform(get("/api/v1/plugins/{pluginId}", "nonexistent"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.success").value(false))
            .andExpect(jsonPath("$.errorCode").value("NOT_FOUND"));
    }

    @Test
    void testExecuteActionOnNonexistentPlugin() throws Exception {
        when(lifecycleManager.executeAction(anyString(), anyString(), anyMap()))
            .thenReturn(Optional.empty());

        mockMvc.perform(post("/api/v1/plugins/{pluginId}/execute", "nonexistent")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"action": "ping", "params": {}}
                    """))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.success").value(false))
            .andExpect(jsonPath("$.errorCode").value("NOT_FOUND"));
    }
}
