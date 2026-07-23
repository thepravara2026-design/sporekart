package com.sporekart.marketplace.lifecycle;

import com.sporekart.marketplace.domain.PluginInstance;
import com.sporekart.marketplace.domain.PluginState;
import com.sporekart.marketplace.registry.PluginRegistry;
import com.sporekart.marketplace.sdk.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PluginLifecycleManagerTest {

    @Mock
    private PluginRegistry registry;

    @Mock
    private PluginValidator validator;

    @InjectMocks
    private PluginLifecycleManager lifecycleManager;

    @Mock
    private SporekartPlugin sporekartPlugin;

    @Captor
    private ArgumentCaptor<PluginContext> contextCaptor;

    private PluginManifest manifest;
    private PluginInstance instance;

    @BeforeEach
    void setUp() {
        manifest = new PluginManifest(
            "test-plugin", "Test Plugin", "1.0.0", "author", "desc",
            PluginType.AI_COPILOT, List.of(PluginCapability.CONVERSATION),
            List.of(PluginPermission.READ_PRODUCTS), List.of(), "1.0.0", null,
            "/health", Map.of(), "exec.Main"
        );
        var metadata = new PluginMetadata("test-plugin", "1.0.0", null, null, null, "test", Map.of());
        instance = new PluginInstance("test-plugin", manifest, metadata);
        instance.setPlugin(sporekartPlugin);
    }

    @Test
    void install_shouldRegisterAndSetInstalledState() {
        when(validator.validate(manifest)).thenReturn(List.of());
        when(registry.register(eq("test-plugin"), any(PluginInstance.class))).thenReturn(instance);

        var result = lifecycleManager.install(instance);

        assertSame(instance, result);
        assertEquals(PluginState.INSTALLED, result.getState());
        verify(registry).register("test-plugin", instance);
    }

    @Test
    void install_shouldFailWhenValidationFails() {
        when(validator.validate(manifest)).thenReturn(List.of("pluginId is required"));

        var thrown = assertThrows(IllegalStateException.class, () -> lifecycleManager.install(instance));
        assertTrue(thrown.getMessage().contains("Plugin validation failed"));
        assertEquals(PluginState.ERROR, instance.getState());
        verify(registry).register("test-plugin", instance);
    }

    @Test
    void install_shouldRegisterEvenWhenValidationFails() {
        when(validator.validate(manifest)).thenReturn(List.of("name is required"));

        assertThrows(IllegalStateException.class, () -> lifecycleManager.install(instance));
        verify(registry).register("test-plugin", instance);
    }

    @Test
    void enable_shouldSetEnabledStateAndCallOnEnable() {
        when(registry.get("test-plugin")).thenReturn(Optional.of(instance));

        var result = lifecycleManager.enable("test-plugin", Map.of("key", "value"));

        assertTrue(result.isPresent());
        assertEquals(PluginState.ENABLED, result.get().getState());
        verify(sporekartPlugin).onEnable(any(PluginContext.class));
    }

    @Test
    void enable_shouldReturnEmptyWhenPluginNotInRegistry() {
        when(registry.get("unknown")).thenReturn(Optional.empty());

        var result = lifecycleManager.enable("unknown", Map.of());

        assertTrue(result.isEmpty());
        verifyNoInteractions(sporekartPlugin);
    }

    @Test
    void enable_shouldHandleNullPlugin() {
        instance.setPlugin(null);
        when(registry.get("test-plugin")).thenReturn(Optional.of(instance));

        var result = lifecycleManager.enable("test-plugin", Map.of());

        assertTrue(result.isPresent());
        assertEquals(PluginState.ENABLED, result.get().getState());
    }

    @Test
    void disable_shouldSetDisabledStateAndCallOnDisable() {
        when(registry.get("test-plugin")).thenReturn(Optional.of(instance));

        var result = lifecycleManager.disable("test-plugin");

        assertTrue(result.isPresent());
        assertEquals(PluginState.DISABLED, result.get().getState());
        verify(sporekartPlugin).onDisable(any(PluginContext.class));
    }

    @Test
    void disable_shouldReturnEmptyWhenPluginNotInRegistry() {
        when(registry.get("unknown")).thenReturn(Optional.empty());

        var result = lifecycleManager.disable("unknown");

        assertTrue(result.isEmpty());
    }

    @Test
    void disable_shouldHandleNullPlugin() {
        instance.setPlugin(null);
        when(registry.get("test-plugin")).thenReturn(Optional.of(instance));

        var result = lifecycleManager.disable("test-plugin");

        assertTrue(result.isPresent());
        assertEquals(PluginState.DISABLED, result.get().getState());
    }

    @Test
    void uninstall_shouldSetUninstalledStateAndUnregister() {
        when(registry.get("test-plugin")).thenReturn(Optional.of(instance));

        var result = lifecycleManager.uninstall("test-plugin");

        assertTrue(result.isPresent());
        assertEquals(PluginState.UNINSTALLED, result.get().getState());
        verify(sporekartPlugin).onUninstall(any(PluginContext.class));
        verify(registry).unregister("test-plugin");
    }

    @Test
    void uninstall_shouldReturnEmptyWhenPluginNotInRegistry() {
        when(registry.get("unknown")).thenReturn(Optional.empty());

        var result = lifecycleManager.uninstall("unknown");

        assertTrue(result.isEmpty());
        verify(registry, never()).unregister(any());
    }

    @Test
    void uninstall_shouldHandleNullPlugin() {
        instance.setPlugin(null);
        when(registry.get("test-plugin")).thenReturn(Optional.of(instance));

        var result = lifecycleManager.uninstall("test-plugin");

        assertTrue(result.isPresent());
        assertEquals(PluginState.UNINSTALLED, result.get().getState());
        verify(registry).unregister("test-plugin");
    }

    @Test
    void upgrade_shouldReplacePluginAndCallOnUpgrade() {
        when(registry.get("test-plugin")).thenReturn(Optional.of(instance));
        var newPlugin = mock(SporekartPlugin.class);

        var result = lifecycleManager.upgrade("test-plugin", newPlugin, "0.9.0");

        assertTrue(result.isPresent());
        assertEquals(PluginState.ENABLED, result.get().getState());
        verify(sporekartPlugin).onDisable(any(PluginContext.class));
        verify(newPlugin).onUpgrade(any(PluginContext.class), eq("0.9.0"));
    }

    @Test
    void upgrade_shouldReturnEmptyWhenPluginNotInRegistry() {
        when(registry.get("unknown")).thenReturn(Optional.empty());

        var result = lifecycleManager.upgrade("unknown", mock(SporekartPlugin.class), "0.9.0");

        assertTrue(result.isEmpty());
    }

    @Test
    void upgrade_shouldHandleNullNewPlugin() {
        when(registry.get("test-plugin")).thenReturn(Optional.of(instance));

        var result = lifecycleManager.upgrade("test-plugin", null, "0.9.0");

        assertTrue(result.isPresent());
        assertNull(instance.getPlugin());
        verify(sporekartPlugin).onDisable(any(PluginContext.class));
    }

    @Test
    void upgrade_shouldHandleNullExistingPlugin() {
        instance.setPlugin(null);
        when(registry.get("test-plugin")).thenReturn(Optional.of(instance));
        var newPlugin = mock(SporekartPlugin.class);

        var result = lifecycleManager.upgrade("test-plugin", newPlugin, "0.9.0");

        assertTrue(result.isPresent());
        assertSame(newPlugin, instance.getPlugin());
        verify(newPlugin).onUpgrade(any(PluginContext.class), eq("0.9.0"));
    }

    @Test
    void downgrade_shouldReplacePluginAndCallOnDowngrade() {
        when(registry.get("test-plugin")).thenReturn(Optional.of(instance));
        var downgradedPlugin = mock(SporekartPlugin.class);

        var result = lifecycleManager.downgrade("test-plugin", downgradedPlugin, "2.0.0");

        assertTrue(result.isPresent());
        assertEquals(PluginState.ENABLED, result.get().getState());
        verify(sporekartPlugin).onDisable(any(PluginContext.class));
        verify(downgradedPlugin).onDowngrade(any(PluginContext.class), eq("2.0.0"));
    }

    @Test
    void downgrade_shouldReturnEmptyWhenPluginNotInRegistry() {
        when(registry.get("unknown")).thenReturn(Optional.empty());

        var result = lifecycleManager.downgrade("unknown", mock(SporekartPlugin.class), "2.0.0");

        assertTrue(result.isEmpty());
    }

    @Test
    void downgrade_shouldHandleNullExistingPlugin() {
        instance.setPlugin(null);
        when(registry.get("test-plugin")).thenReturn(Optional.of(instance));
        var downgradedPlugin = mock(SporekartPlugin.class);

        var result = lifecycleManager.downgrade("test-plugin", downgradedPlugin, "2.0.0");

        assertTrue(result.isPresent());
        assertSame(downgradedPlugin, instance.getPlugin());
        verify(downgradedPlugin).onDowngrade(any(PluginContext.class), eq("2.0.0"));
    }

    @Test
    void executeAction_shouldReturnResultWhenPluginEnabled() {
        instance.setState(PluginState.ENABLED);
        when(registry.get("test-plugin")).thenReturn(Optional.of(instance));
        when(sporekartPlugin.execute("action1", Map.of("param", "value")))
            .thenReturn(Map.of("result", "success"));

        var result = lifecycleManager.executeAction("test-plugin", "action1", Map.of("param", "value"));

        assertTrue(result.isPresent());
        assertEquals("success", result.get().get("result"));
    }

    @Test
    void executeAction_shouldReturnEmptyWhenPluginNotInRegistry() {
        when(registry.get("unknown")).thenReturn(Optional.empty());

        var result = lifecycleManager.executeAction("unknown", "action", Map.of());

        assertTrue(result.isEmpty());
    }

    @Test
    void executeAction_shouldReturnEmptyWhenPluginNotEnabled() {
        instance.setState(PluginState.DISABLED);
        when(registry.get("test-plugin")).thenReturn(Optional.of(instance));

        var result = lifecycleManager.executeAction("test-plugin", "action", Map.of());

        assertTrue(result.isEmpty());
    }

    @Test
    void executeAction_shouldReturnErrorWhenPluginIsNull() {
        instance.setState(PluginState.ENABLED);
        instance.setPlugin(null);
        when(registry.get("test-plugin")).thenReturn(Optional.of(instance));

        var result = lifecycleManager.executeAction("test-plugin", "action", Map.of());

        assertTrue(result.isPresent());
        assertEquals("Plugin not loaded", result.get().get("error"));
    }

    @Test
    void enable_shouldPassConfigToPluginContext() {
        when(registry.get("test-plugin")).thenReturn(Optional.of(instance));
        var config = Map.<String, Object>of("timeout", "5000", "retry", "3");

        lifecycleManager.enable("test-plugin", config);

        verify(sporekartPlugin).onEnable(contextCaptor.capture());
        assertEquals(config, contextCaptor.getValue().getConfig());
    }
}
