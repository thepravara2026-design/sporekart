package com.sporekart.marketplace.loader;

import com.sporekart.marketplace.domain.PluginInstance;
import com.sporekart.marketplace.domain.PluginVersion;
import com.sporekart.marketplace.lifecycle.PluginLifecycleManager;
import com.sporekart.marketplace.lifecycle.VersionManager;
import com.sporekart.marketplace.registry.PluginRegistry;
import com.sporekart.marketplace.sandbox.PluginSandbox;
import com.sporekart.marketplace.sandbox.SandboxSecurityManager;
import com.sporekart.marketplace.sdk.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PluginLoaderTest {

    @Mock
    private PluginRegistry registry;
    @Mock
    private PluginLifecycleManager lifecycleManager;
    @Mock
    private VersionManager versionManager;
    @Mock
    private PluginSandbox sandbox;
    @Mock
    private SandboxSecurityManager securityManager;
    @Mock
    private SporekartPlugin plugin;

    private PluginLoader loader;
    private PluginManifest manifest;

    @BeforeEach
    void setUp() {
        loader = new PluginLoader(registry, lifecycleManager, versionManager, sandbox, securityManager);
        manifest = new PluginManifest(
            "test-plugin", "Test Plugin", "1.0.0", "Author",
            "Description", PluginType.AI_COPILOT,
            List.of(PluginCapability.CONVERSATION),
            List.of(PluginPermission.READ_PRODUCTS, PluginPermission.EXECUTE_AI),
            List.of(), "1.0.0", "2.0.0", "/health", Map.of(), "com.TestPlugin"
        );
    }

    @Test
    void loadPlugin_shouldCreateAndRegisterInstance() {
        when(registry.exists("test-plugin")).thenReturn(false);

        var instance = loader.loadPlugin(manifest, plugin);

        assertNotNull(instance);
        assertEquals("test-plugin", instance.getId());
        assertEquals(plugin, instance.getPlugin());
        assertEquals(com.sporekart.marketplace.domain.PluginState.INSTALLED, instance.getState());

        verify(lifecycleManager).install(instance);
        verify(versionManager).recordVersion(eq("test-plugin"), any(PluginVersion.class));
        verify(securityManager).grantPermissions(eq("test-plugin"), eq(manifest.requiredPermissions()));
        verify(sandbox).createSandbox(instance);
    }

    @Test
    void loadPlugin_shouldThrowWhenPluginAlreadyRegistered() {
        when(registry.exists("test-plugin")).thenReturn(true);

        var ex = assertThrows(IllegalStateException.class,
            () -> loader.loadPlugin(manifest, plugin));
        assertTrue(ex.getMessage().contains("already registered"));
        verifyNoInteractions(lifecycleManager, versionManager, securityManager, sandbox, plugin);
    }

    @Test
    void loadPlugin_shouldPassPluginToLifecycleManagerViaInstance() {
        when(registry.exists("test-plugin")).thenReturn(false);

        var instance = loader.loadPlugin(manifest, plugin);

        var captor = ArgumentCaptor.<PluginInstance>captor();
        verify(lifecycleManager).install(captor.capture());
        var captured = captor.getValue();
        assertSame(plugin, captured.getPlugin());
        assertEquals("test-plugin", captured.getManifest().pluginId());
    }

    @Test
    void unloadPlugin_shouldCallLifecycleSecurityAndSandbox() {
        loader.unloadPlugin("test-plugin");

        verify(lifecycleManager).uninstall("test-plugin");
        verify(securityManager).revokePermissions("test-plugin");
        verify(sandbox).destroySandbox("test-plugin");
    }

    @Test
    void reloadPlugin_shouldUnloadThenLoad() {
        when(registry.exists("test-plugin")).thenReturn(false);

        loader.reloadPlugin("test-plugin", manifest, plugin);

        verify(lifecycleManager).uninstall("test-plugin");
        verify(securityManager).revokePermissions("test-plugin");
        verify(sandbox).destroySandbox("test-plugin");

        verify(lifecycleManager).install(any(PluginInstance.class));
        verify(versionManager).recordVersion(eq("test-plugin"), any(PluginVersion.class));
        verify(securityManager).grantPermissions(eq("test-plugin"), eq(manifest.requiredPermissions()));
        verify(sandbox).createSandbox(any(PluginInstance.class));
    }

    @Test
    void reloadPlugin_shouldThrowIfPluginAlreadyRegisteredAfterUnload() {
        when(registry.exists("test-plugin")).thenReturn(true);

        assertThrows(IllegalStateException.class,
            () -> loader.reloadPlugin("test-plugin", manifest, plugin));

        verify(lifecycleManager).uninstall("test-plugin");
        verify(securityManager).revokePermissions("test-plugin");
        verify(sandbox).destroySandbox("test-plugin");
    }
}
