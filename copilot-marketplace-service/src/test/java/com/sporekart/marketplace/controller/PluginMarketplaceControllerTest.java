package com.sporekart.marketplace.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sporekart.marketplace.domain.CapabilityRegistration;
import com.sporekart.marketplace.domain.PluginInstance;
import com.sporekart.marketplace.domain.PluginState;
import com.sporekart.marketplace.dto.*;
import com.sporekart.marketplace.registry.CapabilityRegistry;
import com.sporekart.marketplace.registry.PluginRegistry;
import com.sporekart.marketplace.service.MarketplaceOrchestrator;
import com.sporekart.marketplace.sdk.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class PluginMarketplaceControllerTest {

    @Mock
    private MarketplaceOrchestrator orchestrator;
    @Mock
    private PluginRegistry pluginRegistry;
    @Mock
    private CapabilityRegistry capabilityRegistry;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private PluginManifest manifest;
    private PluginMetadata metadata;
    private PluginInstance instance;

    @BeforeEach
    void setUp() {
        var controller = new PluginMarketplaceController(orchestrator, pluginRegistry, capabilityRegistry);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        objectMapper = new ObjectMapper();

        manifest = new PluginManifest(
            "test-plugin", "Test Plugin", "1.0.0", "Author",
            "A test plugin", PluginType.AI_COPILOT,
            List.of(PluginCapability.CONVERSATION),
            List.of(PluginPermission.READ_PRODUCTS),
            List.of(), "1.0.0", "2.0.0", "/health", Map.of(), "com.TestPlugin"
        );
        metadata = new PluginMetadata(
            "test-plugin", "1.0.0", Instant.now(), Instant.now(), null, "local", Map.of()
        );
        instance = new PluginInstance("test-plugin", manifest, metadata);
        instance.setState(PluginState.ENABLED);
    }

    @Test
    void listPlugins_shouldReturnAllPlugins() throws Exception {
        when(pluginRegistry.getAll()).thenReturn(List.of(instance));

        mockMvc.perform(get("/api/v1/plugins"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.total").value(1))
            .andExpect(jsonPath("$.data.installed").value(1))
            .andExpect(jsonPath("$.data.enabled").value(1))
            .andExpect(jsonPath("$.data.errored").value(0))
            .andExpect(jsonPath("$.data.plugins[0].pluginId").value("test-plugin"));
    }

    @Test
    void installPlugin_shouldReturnInstalledPlugin() throws Exception {
        when(orchestrator.installPlugin(eq("test-plugin"), eq("marketplace")))
            .thenReturn(instance);

        var request = new PluginInstallRequest("test-plugin", "1.0.0", "marketplace");
        mockMvc.perform(post("/api/v1/plugins/install")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.pluginId").value("test-plugin"));
    }

    @Test
    void enablePlugin_shouldReturnEnabledPlugin() throws Exception {
        when(orchestrator.enablePlugin("test-plugin")).thenReturn(Optional.of(instance));

        mockMvc.perform(post("/api/v1/plugins/enable/test-plugin"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.pluginId").value("test-plugin"));
    }

    @Test
    void enablePlugin_shouldReturnBadRequestWhenNotFound() throws Exception {
        when(orchestrator.enablePlugin("unknown")).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/v1/plugins/enable/unknown"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.success").value(false))
            .andExpect(jsonPath("$.errorCode").value("NOT_FOUND"));
    }

    @Test
    void disablePlugin_shouldReturnDisabledPlugin() throws Exception {
        when(orchestrator.disablePlugin("test-plugin")).thenReturn(Optional.of(instance));

        mockMvc.perform(post("/api/v1/plugins/disable/test-plugin"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.pluginId").value("test-plugin"));
    }

    @Test
    void disablePlugin_shouldReturnBadRequestWhenNotFound() throws Exception {
        when(orchestrator.disablePlugin("unknown")).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/v1/plugins/disable/unknown"))
            .andExpect(status().isBadRequest());
    }

    @Test
    void updatePlugin_shouldReturnUpdatedPlugin() throws Exception {
        when(orchestrator.updatePlugin("test-plugin")).thenReturn(Optional.of(instance));

        mockMvc.perform(post("/api/v1/plugins/update/test-plugin"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.pluginId").value("test-plugin"));
    }

    @Test
    void updatePlugin_shouldReturnBadRequestWhenNotFound() throws Exception {
        when(orchestrator.updatePlugin("unknown")).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/v1/plugins/update/unknown"))
            .andExpect(status().isBadRequest());
    }

    @Test
    void uninstallPlugin_shouldReturnSuccess() throws Exception {
        doNothing().when(orchestrator).uninstallPlugin("test-plugin");

        mockMvc.perform(delete("/api/v1/plugins/uninstall/test-plugin"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.message").value("Plugin uninstalled"));
    }

    @Test
    void marketplace_shouldReturnAvailablePlugins() throws Exception {
        var available = List.<Map<String, Object>>of(Map.of("id", "customer-copilot", "name", "Customer Copilot"));
        when(orchestrator.getAvailablePlugins()).thenReturn(available);

        mockMvc.perform(get("/api/v1/plugins/marketplace"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data[0].id").value("customer-copilot"));
    }

    @Test
    void health_shouldReturnHealthResponse() throws Exception {
        var healthResponse = new PluginHealthResponse(1, 1, 0, 0, List.of(), Map.of("total", 1));
        when(orchestrator.getPluginHealth()).thenReturn(healthResponse);

        mockMvc.perform(get("/api/v1/plugins/health"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.totalPlugins").value(1))
            .andExpect(jsonPath("$.data.healthy").value(1));
    }

    @Test
    void capabilities_shouldReturnCapabilities() throws Exception {
        var cap = new CapabilityRegistration("cap-1", PluginCapability.CONVERSATION,
            "test-plugin", "Test Plugin", Instant.now(), true);
        when(capabilityRegistry.getAll()).thenReturn(List.of(cap));

        mockMvc.perform(get("/api/v1/plugins/capabilities"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data[0].capabilityId").value("cap-1"));
    }

    @Test
    void getPlugin_shouldReturnPlugin() throws Exception {
        when(pluginRegistry.get("test-plugin")).thenReturn(Optional.of(instance));

        mockMvc.perform(get("/api/v1/plugins/test-plugin"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.pluginId").value("test-plugin"));
    }

    @Test
    void getPlugin_shouldReturnBadRequestWhenNotFound() throws Exception {
        when(pluginRegistry.get("unknown")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/plugins/unknown"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.errorCode").value("NOT_FOUND"));
    }

    @Test
    void executeAction_shouldReturnResult() throws Exception {
        var result = Map.<String, Object>of("output", "done");
        when(orchestrator.executePluginAction(eq("test-plugin"), eq("process"), any()))
            .thenReturn(Optional.of(result));

        var body = Map.of("action", "process", "params", Map.of("key", "value"));
        mockMvc.perform(post("/api/v1/plugins/test-plugin/execute")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.output").value("done"));
    }

    @Test
    void executeAction_shouldUseDefaultActionWhenNotProvided() throws Exception {
        var result = Map.<String, Object>of("output", "done");
        when(orchestrator.executePluginAction(eq("test-plugin"), eq("default"), any()))
            .thenReturn(Optional.of(result));

        var body = Map.of("params", Map.of("key", "value"));
        mockMvc.perform(post("/api/v1/plugins/test-plugin/execute")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.output").value("done"));
    }

    @Test
    void executeAction_shouldReturnBadRequestWhenPluginNotFound() throws Exception {
        when(orchestrator.executePluginAction(anyString(), anyString(), any()))
            .thenReturn(Optional.empty());

        mockMvc.perform(post("/api/v1/plugins/unknown/execute")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of("action", "run"))))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.errorCode").value("NOT_FOUND"));
    }
}
