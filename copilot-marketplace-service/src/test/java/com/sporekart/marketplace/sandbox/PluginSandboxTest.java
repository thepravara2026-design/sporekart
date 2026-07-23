package com.sporekart.marketplace.sandbox;

import com.sporekart.marketplace.config.MarketplaceConfig;
import com.sporekart.marketplace.domain.PluginInstance;
import com.sporekart.marketplace.sdk.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PluginSandboxTest {

    private MarketplaceConfig config;
    private MarketplaceConfig.SandboxConfig sandboxConfig;
    private PluginSandbox sandbox;
    private PluginInstance instance;

    @BeforeEach
    void setUp() {
        config = mock(MarketplaceConfig.class);
        sandboxConfig = mock(MarketplaceConfig.SandboxConfig.class);
        when(config.getSandbox()).thenReturn(sandboxConfig);
        when(sandboxConfig.getMaxExecutionTimeoutMs()).thenReturn(30000L);
        when(sandboxConfig.getMaxThreadsPerPlugin()).thenReturn(5);

        sandbox = new PluginSandbox(config);

        var manifest = new PluginManifest(
            "test-plugin", "Test Plugin", "1.0.0", "author", "desc",
            PluginType.AI_COPILOT, List.of(PluginCapability.CONVERSATION),
            List.of(PluginPermission.READ_PRODUCTS), List.of(), "1.0.0", null,
            "/health", Map.of(), "exec.Main"
        );
        var metadata = new PluginMetadata("test-plugin", "1.0.0", null, null, null, "test", Map.of());
        instance = new PluginInstance("test-plugin", manifest, metadata);
    }

    @Test
    void createSandbox_shouldCreateSandboxContext() {
        var context = sandbox.createSandbox(instance);

        assertNotNull(context);
        assertEquals("test-plugin", context.pluginId());
        assertEquals(30000L, context.maxExecutionTimeMs());
        assertEquals(5, context.maxThreads());
    }

    @Test
    void createSandbox_shouldIncreaseActiveCount() {
        assertEquals(0, sandbox.getActiveSandboxCount());

        sandbox.createSandbox(instance);
        assertEquals(1, sandbox.getActiveSandboxCount());
    }

    @Test
    void createSandbox_shouldSupportMultiplePlugins() {
        var manifest2 = new PluginManifest(
            "plugin2", "Plugin 2", "1.0.0", "author", "desc",
            PluginType.ANALYTICS, List.of(PluginCapability.ANALYTICS),
            List.of(PluginPermission.READ_ANALYTICS), List.of(), "1.0.0", null,
            "/health", Map.of(), "exec.Main"
        );
        var metadata2 = new PluginMetadata("plugin2", "1.0.0", null, null, null, "test", Map.of());
        var instance2 = new PluginInstance("plugin2", manifest2, metadata2);

        sandbox.createSandbox(instance);
        sandbox.createSandbox(instance2);
        assertEquals(2, sandbox.getActiveSandboxCount());
    }

    @Test
    void createSandbox_shouldOverwriteExistingSandbox() {
        sandbox.createSandbox(instance);
        sandbox.createSandbox(instance);
        assertEquals(1, sandbox.getActiveSandboxCount());
    }

    @Test
    void executeInSandbox_shouldReturnTaskResultOnSuccess() throws Exception {
        sandbox.createSandbox(instance);

        var result = sandbox.executeInSandbox("test-plugin", () -> Map.of("key", "value"));

        assertEquals("value", result.get("key"));
    }

    @Test
    void executeInSandbox_shouldReturnErrorWhenNoSandbox() {
        var result = sandbox.executeInSandbox("unknown", () -> Map.of("key", "value"));

        assertEquals("No sandbox for plugin: unknown", result.get("error"));
    }

    @Test
    void executeInSandbox_shouldReturnTimeoutError() {
        sandbox.createSandbox(instance);

        var result = sandbox.executeInSandbox("test-plugin", () -> {
            Thread.sleep(50000);
            return Map.of("key", "value");
        });

        assertEquals("Execution timed out", result.get("error"));
        assertEquals("test-plugin", result.get("pluginId"));
    }

    @Test
    void executeInSandbox_shouldReturnFailureOnException() {
        sandbox.createSandbox(instance);

        var result = sandbox.executeInSandbox("test-plugin", () -> {
            throw new RuntimeException("Something broke");
        });

        assertTrue(((String) result.get("error")).contains("Execution failed"));
        assertEquals("test-plugin", result.get("pluginId"));
    }

    @Test
    void executeInSandbox_shouldIncrementTimeoutCountOnTimeout() {
        var context = sandbox.createSandbox(instance);

        sandbox.executeInSandbox("test-plugin", () -> {
            Thread.sleep(50000);
            return Map.of();
        });

        assertTrue(context.getTimeoutCount() > 0);
    }

    @Test
    void executeInSandbox_shouldIncrementErrorCountOnFailure() {
        var context = sandbox.createSandbox(instance);

        sandbox.executeInSandbox("test-plugin", () -> {
            throw new RuntimeException("fail");
        });

        assertTrue(context.getErrorCount() > 0);
    }

    @Test
    void destroySandbox_shouldRemoveSandbox() {
        sandbox.createSandbox(instance);
        assertEquals(1, sandbox.getActiveSandboxCount());

        sandbox.destroySandbox("test-plugin");
        assertEquals(0, sandbox.getActiveSandboxCount());
    }

    @Test
    void destroySandbox_shouldDoNothingForUnknownPlugin() {
        sandbox.createSandbox(instance);
        sandbox.destroySandbox("unknown");
        assertEquals(1, sandbox.getActiveSandboxCount());
    }

    @Test
    void getActiveSandboxCount_shouldReturnZeroInitially() {
        assertEquals(0, sandbox.getActiveSandboxCount());
    }

    @Test
    void sandboxContext_storeStateAndGetState() {
        var context = new PluginSandbox.SandboxContext("test", 30000, 5);
        context.storeState("key1", "value1");
        context.storeState("key2", 42);

        assertEquals("value1", context.getState("key1"));
        assertEquals(42, context.getState("key2"));
    }

    @Test
    void sandboxContext_getState_shouldReturnNullForUnknownKey() {
        var context = new PluginSandbox.SandboxContext("test", 30000, 5);
        assertNull(context.getState("nonexistent"));
    }

    @Test
    void sandboxContext_storeState_shouldOverwriteExisting() {
        var context = new PluginSandbox.SandboxContext("test", 30000, 5);
        context.storeState("key", "first");
        context.storeState("key", "second");

        assertEquals("second", context.getState("key"));
    }

    @Test
    void sandboxContext_timeoutCount_shouldStartAtZero() {
        var context = new PluginSandbox.SandboxContext("test", 30000, 5);
        assertEquals(0, context.getTimeoutCount());
    }

    @Test
    void sandboxContext_errorCount_shouldStartAtZero() {
        var context = new PluginSandbox.SandboxContext("test", 30000, 5);
        assertEquals(0, context.getErrorCount());
    }

    @Test
    void sandboxContext_incrementTimeoutCount_shouldIncrease() {
        var context = new PluginSandbox.SandboxContext("test", 30000, 5);
        context.incrementTimeoutCount();
        context.incrementTimeoutCount();
        assertEquals(2, context.getTimeoutCount());
    }

    @Test
    void sandboxContext_incrementErrorCount_shouldIncrease() {
        var context = new PluginSandbox.SandboxContext("test", 30000, 5);
        context.incrementErrorCount();
        context.incrementErrorCount();
        context.incrementErrorCount();
        assertEquals(3, context.getErrorCount());
    }

    @Test
    void sandboxContext_shouldHaveCorrectInitialValues() {
        var context = new PluginSandbox.SandboxContext("plugin-id", 15000, 10);
        assertEquals("plugin-id", context.pluginId());
        assertEquals(15000, context.maxExecutionTimeMs());
        assertEquals(10, context.maxThreads());
    }
}
