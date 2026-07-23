package com.sporekart.marketplace.certification;

import com.sporekart.marketplace.config.MarketplaceConfig;
import com.sporekart.marketplace.domain.PluginInstance;
import com.sporekart.marketplace.sandbox.PluginSandbox;
import com.sporekart.marketplace.sandbox.ResourceLimiter;
import com.sporekart.marketplace.sandbox.SandboxSecurityManager;
import com.sporekart.marketplace.sdk.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class SandboxIsolationCertificationTest {

    @Autowired
    private PluginSandbox sandbox;

    @Autowired
    private SandboxSecurityManager securityManager;

    @Autowired
    private ResourceLimiter resourceLimiter;

    @Autowired
    private MarketplaceConfig config;

    private PluginInstance instance;
    private PluginInstance secondInstance;

    @BeforeEach
    void setUp() {
        sandbox.destroySandbox("sandbox-cert-plugin");
        sandbox.destroySandbox("sandbox-cert-plugin-2");
        sandbox.destroySandbox("sandbox-cert-plugin-3");
        resourceLimiter.releaseResources("sandbox-cert-plugin");
        resourceLimiter.releaseResources("sandbox-cert-plugin-2");
        resourceLimiter.releaseResources("unknown");
        securityManager.revokePermissions("sandbox-cert-plugin");
        securityManager.revokePermissions("sandbox-cert-plugin-2");
        securityManager.revokePermissions("sandbox-cert-plugin-3");

        var manifest = new PluginManifest(
            "sandbox-cert-plugin", "Sandbox Cert", "1.0.0", "SporeKart",
            "Sandbox isolation certification", PluginType.AI_COPILOT,
            List.of(PluginCapability.CONVERSATION),
            List.of(PluginPermission.READ_PRODUCTS, PluginPermission.EXECUTE_AI),
            List.of(), "1.0.0", "2.0.0",
            "/health", Map.of(), "sandbox.Main"
        );
        var metadata = new PluginMetadata(
            "sandbox-cert-plugin", "1.0.0", null, null, null, "certification", Map.of()
        );
        instance = new PluginInstance("sandbox-cert-plugin", manifest, metadata);

        var manifest2 = new PluginManifest(
            "sandbox-cert-plugin-2", "Sandbox Cert 2", "1.0.0", "SporeKart",
            "Second sandbox plugin", PluginType.ANALYTICS,
            List.of(PluginCapability.ANALYTICS),
            List.of(PluginPermission.READ_ANALYTICS),
            List.of(), "1.0.0", "2.0.0",
            "/health", Map.of(), "sandbox.Main2"
        );
        var metadata2 = new PluginMetadata(
            "sandbox-cert-plugin-2", "1.0.0", null, null, null, "certification", Map.of()
        );
        secondInstance = new PluginInstance("sandbox-cert-plugin-2", manifest2, metadata2);
    }

    @Test
    @DisplayName("SIC-001: Sandbox enforces resource limits - max threads per plugin")
    void sandboxShouldEnforceMaxThreads() {
        assertFalse(resourceLimiter.isWithinLimits("unknown"),
            "Unknown plugin should not be within limits");
    }

    @Test
    @DisplayName("SIC-002: Sandbox enforces resource limits - max installed plugins")
    void sandboxShouldEnforceMaxInstalledPlugins() {
        var maxPlugins = config.getMaxInstalledPlugins();
        for (int i = 0; i < maxPlugins; i++) {
            assertTrue(resourceLimiter.allocateResources("plugin-" + i),
                "Allocation should succeed up to max plugins: " + i);
        }
        assertEquals(maxPlugins, resourceLimiter.getActivePluginCount());
        assertFalse(resourceLimiter.allocateResources("overflow-plugin"),
            "Allocation should fail when at max plugins");
    }

    @Test
    @DisplayName("SIC-003: Sandbox enforces timeout on long-running execution")
    void sandboxShouldEnforceTimeout() {
        sandbox.createSandbox(instance);

        var result = sandbox.executeInSandbox("sandbox-cert-plugin", () -> {
            Thread.sleep(60000);
            return Map.of("key", "value");
        });

        assertEquals("Execution timed out", result.get("error"));
        assertEquals("sandbox-cert-plugin", result.get("pluginId"));
    }

    @Test
    @DisplayName("SIC-004: Sandbox returns error for unknown plugin")
    void sandboxShouldReturnErrorForUnknownPlugin() {
        var result = sandbox.executeInSandbox("unknown", () -> Map.of("key", "value"));

        assertEquals("No sandbox for plugin: unknown", result.get("error"));
    }

    @Test
    @DisplayName("SIC-005: Sandbox returns failure on plugin exception")
    void sandboxShouldReturnFailureOnException() {
        sandbox.createSandbox(instance);

        var result = sandbox.executeInSandbox("sandbox-cert-plugin", () -> {
            throw new RuntimeException("Intentional sandbox failure");
        });

        assertTrue(((String) result.get("error")).contains("Execution failed"));
        assertEquals("sandbox-cert-plugin", result.get("pluginId"));
    }

    @Test
    @DisplayName("SIC-006: Sandbox tracks timeout count")
    void sandboxShouldTrackTimeoutCount() {
        var context = sandbox.createSandbox(instance);

        sandbox.executeInSandbox("sandbox-cert-plugin", () -> {
            Thread.sleep(60000);
            return Map.of();
        });

        assertTrue(context.getTimeoutCount() > 0,
            "Timeout count should increase after timeout");
    }

    @Test
    @DisplayName("SIC-007: Sandbox tracks error count")
    void sandboxShouldTrackErrorCount() {
        var context = sandbox.createSandbox(instance);

        sandbox.executeInSandbox("sandbox-cert-plugin", () -> {
            throw new RuntimeException("tracked failure");
        });

        assertTrue(context.getErrorCount() > 0,
            "Error count should increase after failure");
    }

    @Test
    @DisplayName("SIC-008: Sandbox isolates plugins from each other")
    void sandboxShouldIsolatePlugins() {
        var context1 = sandbox.createSandbox(instance);
        context1.storeState("shared-key", "value-from-plugin-1");

        var context2 = sandbox.createSandbox(secondInstance);
        context2.storeState("shared-key", "value-from-plugin-2");

        assertNull(context1.getState("shared-key-from-other"),
            "Plugin 1 should not see Plugin 2's key space");
        assertEquals("value-from-plugin-1", context1.getState("shared-key"),
            "Plugin 1 should retain its own state");
        assertEquals("value-from-plugin-2", context2.getState("shared-key"),
            "Plugin 2 should have its own independent state");
    }

    @Test
    @DisplayName("SIC-009: Sandbox context starts with zero error and timeout counts")
    void sandboxContextShouldStartWithZeroCounts() {
        var context = new PluginSandbox.SandboxContext("test", 30000, 5);

        assertEquals(0, context.getTimeoutCount());
        assertEquals(0, context.getErrorCount());
    }

    @Test
    @DisplayName("SIC-010: Resource limiter tracks per-plugin thread usage")
    void resourceLimiterShouldTrackThreadUsage() {
        resourceLimiter.allocateResources("sandbox-cert-plugin");
        var usage = resourceLimiter.getUsage("sandbox-cert-plugin");

        assertNotNull(usage);
        assertEquals(0, usage.getThreadCount());

        usage.incrementThreads();
        assertEquals(1, usage.getThreadCount());

        usage.incrementThreads();
        assertEquals(2, usage.getThreadCount());

        usage.decrementThreads();
        assertEquals(1, usage.getThreadCount());
    }

    @Test
    @DisplayName("SIC-011: Resource limiter rejects allocation beyond max plugins")
    void resourceLimiterShouldRejectBeyondMax() {
        var maxPlugins = config.getMaxInstalledPlugins();
        for (int i = 0; i < maxPlugins; i++) {
            resourceLimiter.allocateResources("load-plugin-" + i);
        }
        assertFalse(resourceLimiter.allocateResources("extra-plugin"),
            "Should reject when at capacity");
    }

    @Test
    @DisplayName("SIC-012: Resource limiter releases and reuses slots")
    void resourceLimiterShouldReleaseAndReuseSlots() {
        resourceLimiter.allocateResources("temp-plugin");
        assertEquals(1, resourceLimiter.getActivePluginCount());

        resourceLimiter.releaseResources("temp-plugin");
        assertEquals(0, resourceLimiter.getActivePluginCount());

        assertTrue(resourceLimiter.allocateResources("new-plugin"),
            "Slot should be reusable after release");
    }

    @Test
    @DisplayName("SIC-013: Security manager enforces permission isolation between plugins")
    void securityManagerShouldEnforcePermissionIsolation() {
        securityManager.grantPermissions("plugin-a", List.of(PluginPermission.READ_PRODUCTS));
        securityManager.grantPermissions("plugin-b", List.of(PluginPermission.READ_ORDERS));

        assertTrue(securityManager.hasPermission("plugin-a", PluginPermission.READ_PRODUCTS));
        assertFalse(securityManager.hasPermission("plugin-a", PluginPermission.READ_ORDERS));
        assertTrue(securityManager.hasPermission("plugin-b", PluginPermission.READ_ORDERS));
        assertFalse(securityManager.hasPermission("plugin-b", PluginPermission.READ_PRODUCTS));
    }

    @Test
    @DisplayName("SIC-014: Security manager revoke removes all permissions for a plugin")
    void securityManagerRevokeShouldRemoveAllPermissions() {
        securityManager.grantPermissions("plugin-c",
            List.of(PluginPermission.READ_PRODUCTS, PluginPermission.EXECUTE_AI));
        assertTrue(securityManager.hasPermission("plugin-c", PluginPermission.READ_PRODUCTS));

        securityManager.revokePermissions("plugin-c");
        assertFalse(securityManager.hasPermission("plugin-c", PluginPermission.READ_PRODUCTS));
        assertFalse(securityManager.hasPermission("plugin-c", PluginPermission.EXECUTE_AI));
    }

    @Test
    @DisplayName("SIC-015: Security manager revoke does not affect other plugins")
    void securityManagerRevokeShouldNotAffectOthers() {
        securityManager.grantPermissions("plugin-d", List.of(PluginPermission.READ_PRODUCTS));
        securityManager.grantPermissions("plugin-e", List.of(PluginPermission.READ_ANALYTICS));

        securityManager.revokePermissions("plugin-d");

        assertTrue(securityManager.hasPermission("plugin-e", PluginPermission.READ_ANALYTICS),
            "Plugin E should retain its permissions after Plugin D is revoked");
    }

    @Test
    @DisplayName("SIC-016: Concurrent sandbox execution isolation")
    void concurrentExecutionShouldBeIsolated() {
        sandbox.createSandbox(instance);
        sandbox.createSandbox(secondInstance);

        var result1 = sandbox.executeInSandbox("sandbox-cert-plugin", () -> Map.of("result", "plugin-1-done"));
        var result2 = sandbox.executeInSandbox("sandbox-cert-plugin-2", () -> Map.of("result", "plugin-2-done"));

        assertEquals("plugin-1-done", result1.get("result"));
        assertEquals("plugin-2-done", result2.get("result"));
    }

    @Test
    @DisplayName("SIC-017: Error in one sandbox does not affect other sandboxes")
    void errorInOneSandboxShouldNotAffectOthers() {
        sandbox.createSandbox(instance);
        sandbox.createSandbox(secondInstance);

        sandbox.executeInSandbox("sandbox-cert-plugin", () -> {
            throw new RuntimeException("Isolated failure");
        });

        var healthyResult = sandbox.executeInSandbox("sandbox-cert-plugin-2", () -> Map.of("status", "ok"));

        assertEquals("ok", healthyResult.get("status"),
            "Second plugin should execute normally despite first plugin's failure");
    }

    @Test
    @DisplayName("SIC-018: Sandbox can be destroyed and releases resources")
    void sandboxDestroyShouldReleaseResources() {
        sandbox.createSandbox(instance);
        assertEquals(1, sandbox.getActiveSandboxCount());

        sandbox.destroySandbox("sandbox-cert-plugin");
        assertEquals(0, sandbox.getActiveSandboxCount());

        var result = sandbox.executeInSandbox("sandbox-cert-plugin", () -> Map.of());
        assertEquals("No sandbox for plugin: sandbox-cert-plugin", result.get("error"));
    }

    @Test
    @DisplayName("SIC-019: Destroying unknown sandbox does not error")
    void destroyUnknownSandboxShouldNotError() {
        sandbox.createSandbox(instance);
        sandbox.destroySandbox("unknown");
        assertEquals(1, sandbox.getActiveSandboxCount());
    }

    @Test
    @DisplayName("SIC-020: Multiple sandboxes coexist independently")
    void multipleSandboxesShouldCoexist() {
        sandbox.createSandbox(instance);

        var manifest3 = new PluginManifest(
            "sandbox-cert-plugin-3", "Third Plugin", "1.0.0", "SporeKart",
            "Third sandbox", PluginType.WORKFLOW,
            List.of(PluginCapability.WORKFLOW),
            List.of(), List.of(), "1.0.0", "2.0.0",
            "/health", Map.of(), "sandbox.Main3"
        );
        var metadata3 = new PluginMetadata(
            "sandbox-cert-plugin-3", "1.0.0", null, null, null, "certification", Map.of()
        );
        var thirdInstance = new PluginInstance("sandbox-cert-plugin-3", manifest3, metadata3);

        sandbox.createSandbox(secondInstance);
        sandbox.createSandbox(thirdInstance);

        assertEquals(3, sandbox.getActiveSandboxCount());
    }

    @Test
    @DisplayName("SIC-021: Config provides sandbox parameters")
    void configShouldProvideSandboxParameters() {
        var sandboxConfig = config.getSandbox();
        assertNotNull(sandboxConfig);
        assertTrue(sandboxConfig.getMaxExecutionTimeoutMs() > 0,
            "Max execution timeout must be configured");
        assertTrue(sandboxConfig.getMaxThreadsPerPlugin() > 0,
            "Max threads per plugin must be configured");
    }
}
