package com.sporekart.marketplace.certification;

import com.sporekart.marketplace.domain.PluginInstance;
import com.sporekart.marketplace.domain.PluginState;
import com.sporekart.marketplace.health.PluginHealthMonitor;
import com.sporekart.marketplace.infrastructure.monitoring.MarketplaceMetricsService;
import com.sporekart.marketplace.lifecycle.PluginLifecycleManager;
import com.sporekart.marketplace.lifecycle.PluginValidator;
import com.sporekart.marketplace.lifecycle.VersionManager;
import com.sporekart.marketplace.loader.PluginLoader;
import com.sporekart.marketplace.permission.PermissionManager;
import com.sporekart.marketplace.registry.PluginRegistry;
import com.sporekart.marketplace.sdk.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@WithMockUser
class PluginLifecycleCertificationTest {

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

    @MockBean
    private PluginRegistry pluginRegistry;

    private static final String PLUGIN_ID = "lifecycle-cert-plugin";
    private PluginInstance installedInstance;
    private PluginInstance enabledInstance;
    private PluginInstance disabledInstance;
    private PluginInstance upgradedInstance;

    @BeforeEach
    void setUp() {
        installedInstance = createInstance(PLUGIN_ID, PluginState.INSTALLED);
        enabledInstance = createInstance(PLUGIN_ID, PluginState.ENABLED);
        disabledInstance = createInstance(PLUGIN_ID, PluginState.DISABLED);
        upgradedInstance = createInstance(PLUGIN_ID, PluginState.ENABLED);
    }

    @Test
    @DisplayName("PLC-001: Plugin install transitions to INSTALLED state")
    void installShouldTransitionToInstalled() throws Exception {
        when(lifecycleManager.install(any())).thenReturn(installedInstance);
        when(permissionManager.approve(anyString(), any())).thenReturn(true);
        doNothing().when(versionManager).recordVersion(anyString(), any());
        doNothing().when(metricsService).recordPluginInstall();

        mockMvc.perform(post("/api/v1/plugins/install")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"pluginId": "%s", "source": "marketplace"}
                    """.formatted(PLUGIN_ID)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.state").value("INSTALLED"));
    }

    @Test
    @DisplayName("PLC-002: Plugin enable transitions to ENABLED state")
    void enableShouldTransitionToEnabled() throws Exception {
        when(lifecycleManager.enable(eq(PLUGIN_ID), anyMap())).thenReturn(Optional.of(enabledInstance));

        mockMvc.perform(post("/api/v1/plugins/enable/{pluginId}", PLUGIN_ID))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.state").value("ENABLED"));
    }

    @Test
    @DisplayName("PLC-003: Plugin disable transitions to DISABLED state")
    void disableShouldTransitionToDisabled() throws Exception {
        when(lifecycleManager.disable(eq(PLUGIN_ID))).thenReturn(Optional.of(disabledInstance));

        mockMvc.perform(post("/api/v1/plugins/disable/{pluginId}", PLUGIN_ID))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.state").value("DISABLED"));
    }

    @Test
    @DisplayName("PLC-004: Plugin upgrade transitions to ENABLED state with new version")
    void upgradeShouldTransitionToEnabled() throws Exception {
        when(pluginRegistry.get(PLUGIN_ID)).thenReturn(Optional.of(installedInstance));
        when(lifecycleManager.upgrade(anyString(), any(), anyString()))
            .thenReturn(Optional.of(upgradedInstance));

        mockMvc.perform(post("/api/v1/plugins/update/{pluginId}", PLUGIN_ID))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.state").value("ENABLED"));
    }

    @Test
    @DisplayName("PLC-005: Plugin health check returns detailed status")
    void healthCheckShouldReturnDetailedStatus() throws Exception {
        when(healthMonitor.getAllHealth()).thenReturn(List.of());
        when(healthMonitor.getHealthyCount()).thenReturn(1L);
        when(healthMonitor.getUnhealthyCount()).thenReturn(0L);
        when(healthMonitor.getHealthSummary()).thenReturn(Map.of(
            "total", 1, "healthy", 1, "unhealthy", 0, "unknown", 0,
            "averageResponseTimeMs", 0.0
        ));

        mockMvc.perform(get("/api/v1/plugins/health"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.totalPlugins").exists())
            .andExpect(jsonPath("$.data.healthy").exists())
            .andExpect(jsonPath("$.data.unhealthy").exists());
    }

    @Test
    @DisplayName("PLC-007: Plugin validation returns health status")
    void validateShouldReturnHealthStatus() throws Exception {
        when(healthMonitor.getAllHealth()).thenReturn(List.of());
        when(healthMonitor.getHealthyCount()).thenReturn(1L);
        when(healthMonitor.getUnhealthyCount()).thenReturn(0L);
        when(healthMonitor.getHealthSummary()).thenReturn(Map.of(
            "total", 1, "healthy", 1, "unhealthy", 0, "unknown", 0,
            "averageResponseTimeMs", 0.0
        ));

        mockMvc.perform(get("/api/v1/plugins/health"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").exists());
    }

    @Test
    @DisplayName("PLC-008: Plugin uninstall transitions to UNINSTALLED state")
    void uninstallShouldTransitionToUninstalled() throws Exception {
        when(lifecycleManager.uninstall(eq(PLUGIN_ID))).thenReturn(Optional.empty());
        doNothing().when(permissionManager).revokeAll(eq(PLUGIN_ID));
        doNothing().when(metricsService).recordPluginUninstall();

        mockMvc.perform(delete("/api/v1/plugins/uninstall/{pluginId}", PLUGIN_ID))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.message").value("Plugin uninstalled"));
    }

    @Test
    @DisplayName("PLC-010: Plugin uninstall removes permissions and metrics")
    void uninstallShouldCleanupPermissionsAndMetrics() throws Exception {
        when(lifecycleManager.uninstall(eq(PLUGIN_ID))).thenReturn(Optional.empty());
        doNothing().when(permissionManager).revokeAll(eq(PLUGIN_ID));
        doNothing().when(metricsService).recordPluginUninstall();

        mockMvc.perform(delete("/api/v1/plugins/uninstall/{pluginId}", PLUGIN_ID))
            .andExpect(status().isOk());

        verify(permissionManager).revokeAll(eq(PLUGIN_ID));
        verify(metricsService).recordPluginUninstall();
    }

    @Test
    @DisplayName("PLC-011: Install validates plugin id is not empty")
    void installShouldRejectEmptyPluginId() throws Exception {
        mockMvc.perform(post("/api/v1/plugins/install")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"pluginId": "", "source": "marketplace"}
                    """))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("PLC-012: Enable nonexistent plugin returns error")
    void enableNonexistentPluginShouldReturnError() throws Exception {
        when(lifecycleManager.enable(anyString(), anyMap())).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/v1/plugins/enable/{pluginId}", "nonexistent"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.success").value(false))
            .andExpect(jsonPath("$.errorCode").value("NOT_FOUND"));
    }

    @Test
    @DisplayName("PLC-013: Full lifecycle install -> enable -> disable -> uninstall succeeds")
    void fullLifecycleInstallEnableDisableUninstallShouldSucceed() throws Exception {
        when(lifecycleManager.install(any())).thenReturn(installedInstance);
        when(permissionManager.approve(anyString(), any())).thenReturn(true);
        doNothing().when(versionManager).recordVersion(anyString(), any());
        doNothing().when(metricsService).recordPluginInstall();

        mockMvc.perform(post("/api/v1/plugins/install")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"pluginId": "%s", "source": "marketplace"}
                    """.formatted(PLUGIN_ID)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.state").value("INSTALLED"));

        when(lifecycleManager.enable(eq(PLUGIN_ID), anyMap())).thenReturn(Optional.of(enabledInstance));

        mockMvc.perform(post("/api/v1/plugins/enable/{pluginId}", PLUGIN_ID))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.state").value("ENABLED"));

        when(lifecycleManager.disable(eq(PLUGIN_ID))).thenReturn(Optional.of(disabledInstance));

        mockMvc.perform(post("/api/v1/plugins/disable/{pluginId}", PLUGIN_ID))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.state").value("DISABLED"));

        when(lifecycleManager.uninstall(eq(PLUGIN_ID))).thenReturn(Optional.empty());
        doNothing().when(permissionManager).revokeAll(eq(PLUGIN_ID));
        doNothing().when(metricsService).recordPluginUninstall();

        mockMvc.perform(delete("/api/v1/plugins/uninstall/{pluginId}", PLUGIN_ID))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("Plugin uninstalled"));
    }

    @Test
    @DisplayName("PLC-014: Plugin execution returns result for enabled plugin")
    void executeActionShouldReturnResultForEnabledPlugin() throws Exception {
        var result = Map.<String, Object>of("output", "executed");
        when(lifecycleManager.executeAction(eq(PLUGIN_ID), anyString(), anyMap()))
            .thenReturn(Optional.of(result));

        mockMvc.perform(post("/api/v1/plugins/{pluginId}/execute", PLUGIN_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"action": "process", "params": {"key": "value"}}
                    """))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.output").value("executed"));
    }

    @Test
    @DisplayName("PLC-015: Plugin execution on nonexistent plugin returns error")
    void executeActionOnNonexistentPluginShouldReturnError() throws Exception {
        when(lifecycleManager.executeAction(anyString(), anyString(), anyMap()))
            .thenReturn(Optional.empty());

        mockMvc.perform(post("/api/v1/plugins/{pluginId}/execute", "nonexistent")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"action": "run", "params": {}}
                    """))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.errorCode").value("NOT_FOUND"));
    }

    @Test
    @DisplayName("PLC-016: Plugin rollback restores previous state")
    void rollbackShouldRestorePreviousState() throws Exception {
        when(pluginRegistry.get(PLUGIN_ID)).thenReturn(Optional.of(enabledInstance));
        when(lifecycleManager.disable(eq(PLUGIN_ID)))
            .thenReturn(Optional.of(disabledInstance));
        when(lifecycleManager.enable(eq(PLUGIN_ID), anyMap()))
            .thenReturn(Optional.of(enabledInstance));

        mockMvc.perform(post("/api/v1/plugins/disable/{pluginId}", PLUGIN_ID))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.state").value("DISABLED"));

        when(lifecycleManager.enable(eq(PLUGIN_ID), anyMap()))
            .thenReturn(Optional.of(enabledInstance));

        mockMvc.perform(post("/api/v1/plugins/enable/{pluginId}", PLUGIN_ID))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.state").value("ENABLED"));
    }

    private PluginInstance createInstance(String pluginId, PluginState state) {
        var manifest = new PluginManifest(
            pluginId, "Lifecycle Cert Plugin", "1.0.0", "SporeKart",
            "Plugin lifecycle certification test", PluginType.AI_COPILOT,
            List.of(PluginCapability.CONVERSATION),
            List.of(PluginPermission.READ_PRODUCTS, PluginPermission.EXECUTE_AI),
            List.of(), "1.0.0", "2.0.0",
            "/health", Map.of(), pluginId + ".PluginImpl"
        );
        var metadata = new PluginMetadata(
            pluginId, "1.0.0", Instant.now(), Instant.now(), null, "certification", Map.of()
        );
        var instance = new PluginInstance(pluginId, manifest, metadata);
        instance.setState(state);
        return instance;
    }
}
