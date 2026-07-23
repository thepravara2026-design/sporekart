package com.sporekart.marketplace.registry;

import com.sporekart.marketplace.domain.PluginInstance;
import com.sporekart.marketplace.domain.PluginState;
import com.sporekart.marketplace.sdk.PluginCapability;
import com.sporekart.marketplace.sdk.PluginManifest;
import com.sporekart.marketplace.sdk.PluginMetadata;
import com.sporekart.marketplace.sdk.PluginPermission;
import com.sporekart.marketplace.sdk.PluginType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PluginRegistryTest {

    private PluginRegistry registry;

    private PluginInstance pluginA;
    private PluginInstance pluginB;
    private PluginInstance pluginC;

    @BeforeEach
    void setUp() {
        registry = new PluginRegistry();

        var manifestA = new PluginManifest(
            "p1", "Plugin A", "1.0.0", "authorA", "descA",
            PluginType.AI_COPILOT, List.of(PluginCapability.CONVERSATION),
            List.of(PluginPermission.READ_PRODUCTS), List.of(), "1.0.0", "2.0.0",
            "/health", Map.of("key", "value"), "exec.p1.Main"
        );
        var metadataA = new PluginMetadata("p1", "1.0.0", null, null, null, "test", Map.of());
        pluginA = new PluginInstance("p1", manifestA, metadataA);

        var manifestB = new PluginManifest(
            "p2", "Plugin B", "2.0.0", "authorB", "descB",
            PluginType.ANALYTICS, List.of(PluginCapability.ANALYTICS),
            List.of(PluginPermission.READ_ANALYTICS), List.of(), "1.0.0", null,
            "/health", Map.of(), "exec.p2.Main"
        );
        var metadataB = new PluginMetadata("p2", "2.0.0", null, null, null, "test", Map.of());
        pluginB = new PluginInstance("p2", manifestB, metadataB);

        var manifestC = new PluginManifest(
            "p3", "Plugin C", "1.5.0", "authorC", "descC",
            PluginType.AI_COPILOT, List.of(PluginCapability.KNOWLEDGE),
            List.of(PluginPermission.ACCESS_KNOWLEDGE), List.of(), "1.0.0", "3.0.0",
            "/health", Map.of(), "exec.p3.Main"
        );
        var metadataC = new PluginMetadata("p3", "1.5.0", null, null, null, "test", Map.of());
        pluginC = new PluginInstance("p3", manifestC, metadataC);
    }

    @Test
    void register_shouldStorePlugin() {
        registry.register("p1", pluginA);
        assertTrue(registry.exists("p1"));
        assertEquals(1, registry.count());
    }

    @Test
    void register_shouldReturnInstance() {
        var result = registry.register("p1", pluginA);
        assertSame(pluginA, result);
    }

    @Test
    void register_shouldOverwriteExisting() {
        registry.register("p1", pluginA);
        registry.register("p1", pluginB);
        assertSame(pluginB, registry.get("p1").orElseThrow());
    }

    @Test
    void get_shouldReturnPluginWhenExists() {
        registry.register("p1", pluginA);
        var result = registry.get("p1");
        assertTrue(result.isPresent());
        assertSame(pluginA, result.get());
    }

    @Test
    void get_shouldReturnEmptyWhenNotExists() {
        var result = registry.get("nonexistent");
        assertTrue(result.isEmpty());
    }

    @Test
    void getAll_shouldReturnAllRegisteredPlugins() {
        registry.register("p1", pluginA);
        registry.register("p2", pluginB);
        var all = registry.getAll();
        assertEquals(2, all.size());
        assertTrue(all.contains(pluginA));
        assertTrue(all.contains(pluginB));
    }

    @Test
    void getAll_shouldReturnUnmodifiableList() {
        registry.register("p1", pluginA);
        var all = registry.getAll();
        assertThrows(UnsupportedOperationException.class, () -> all.add(pluginB));
    }

    @Test
    void getByState_shouldFilterCorrectly() {
        pluginA.setState(PluginState.ENABLED);
        pluginB.setState(PluginState.DISABLED);
        pluginC.setState(PluginState.ENABLED);
        registry.register("p1", pluginA);
        registry.register("p2", pluginB);
        registry.register("p3", pluginC);

        var enabled = registry.getByState(PluginState.ENABLED);
        assertEquals(2, enabled.size());
        assertTrue(enabled.contains(pluginA));
        assertTrue(enabled.contains(pluginC));
    }

    @Test
    void getByState_shouldReturnEmptyWhenNoMatch() {
        registry.register("p1", pluginA);
        var result = registry.getByState(PluginState.ERROR);
        assertTrue(result.isEmpty());
    }

    @Test
    void getByType_shouldFilterCorrectly() {
        registry.register("p1", pluginA);
        registry.register("p2", pluginB);
        registry.register("p3", pluginC);

        var aiCopilots = registry.getByType("AI_COPILOT");
        assertEquals(2, aiCopilots.size());

        var analytics = registry.getByType("ANALYTICS");
        assertEquals(1, analytics.size());
        assertSame(pluginB, analytics.get(0));
    }

    @Test
    void getByType_shouldBeCaseInsensitive() {
        registry.register("p1", pluginA);
        var result = registry.getByType("ai_copilot");
        assertEquals(1, result.size());
    }

    @Test
    void getByType_shouldReturnEmptyWhenNoMatch() {
        registry.register("p1", pluginA);
        var result = registry.getByType("ML");
        assertTrue(result.isEmpty());
    }

    @Test
    void exists_shouldReturnTrueWhenRegistered() {
        registry.register("p1", pluginA);
        assertTrue(registry.exists("p1"));
    }

    @Test
    void exists_shouldReturnFalseWhenNotRegistered() {
        assertFalse(registry.exists("p1"));
    }

    @Test
    void unregister_shouldRemovePlugin() {
        registry.register("p1", pluginA);
        var removed = registry.unregister("p1");
        assertSame(pluginA, removed);
        assertFalse(registry.exists("p1"));
    }

    @Test
    void unregister_shouldReturnNullWhenNotExists() {
        var removed = registry.unregister("nonexistent");
        assertNull(removed);
    }

    @Test
    void count_shouldReturnCorrectNumber() {
        assertEquals(0, registry.count());
        registry.register("p1", pluginA);
        assertEquals(1, registry.count());
        registry.register("p2", pluginB);
        assertEquals(2, registry.count());
    }

    @Test
    void countByState_shouldReturnCorrectCount() {
        pluginA.setState(PluginState.ENABLED);
        pluginB.setState(PluginState.ENABLED);
        pluginC.setState(PluginState.DISABLED);
        registry.register("p1", pluginA);
        registry.register("p2", pluginB);
        registry.register("p3", pluginC);

        assertEquals(2, registry.countByState(PluginState.ENABLED));
        assertEquals(1, registry.countByState(PluginState.DISABLED));
        assertEquals(0, registry.countByState(PluginState.ERROR));
    }

    @Test
    void updateState_shouldChangePluginState() {
        registry.register("p1", pluginA);
        assertEquals(PluginState.INSTALLED, pluginA.getState());

        registry.updateState("p1", PluginState.ENABLED);
        assertEquals(PluginState.ENABLED, pluginA.getState());
    }

    @Test
    void updateState_shouldDoNothingWhenPluginNotExists() {
        registry.updateState("nonexistent", PluginState.ENABLED);
        assertFalse(registry.exists("nonexistent"));
    }

    @Test
    void threadSafety_shouldHandleConcurrentRegistrations() throws InterruptedException {
        int threadCount = 50;
        var threads = new Thread[threadCount];
        for (int i = 0; i < threadCount; i++) {
            final int index = i;
            threads[i] = new Thread(() -> {
                var manifest = new PluginManifest(
                    "p" + index, "Plugin" + index, "1.0.0", "author",
                    "desc", PluginType.AI_COPILOT, List.of(PluginCapability.CONVERSATION),
                    List.of(PluginPermission.READ_PRODUCTS), List.of(), "1.0.0", null,
                    "/health", Map.of(), "exec.Main"
                );
                var metadata = new PluginMetadata("p" + index, "1.0.0", null, null, null, "test", Map.of());
                var instance = new PluginInstance("p" + index, manifest, metadata);
                registry.register("p" + index, instance);
            });
            threads[i].start();
        }
        for (var t : threads) {
            t.join();
        }
        assertEquals(threadCount, registry.count());
    }

    @Test
    void threadSafety_shouldHandleConcurrentReadsAndWrites() throws InterruptedException {
        registry.register("p1", pluginA);
        var reader = new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                registry.get("p1");
                registry.exists("p1");
                registry.count();
                registry.getAll();
            }
        });
        var writer = new Thread(() -> {
            for (int i = 0; i < 50; i++) {
                registry.updateState("p1", PluginState.ENABLED);
                registry.updateState("p1", PluginState.DISABLED);
            }
        });
        reader.start();
        writer.start();
        reader.join();
        writer.join();
        assertTrue(registry.exists("p1"));
    }

    @Test
    void getAll_shouldReturnSnapshotWhenPluginAddedAfter() {
        var all = registry.getAll();
        registry.register("p1", pluginA);
        assertEquals(0, all.size());
    }

    @Test
    void unregister_shouldNotAffectOtherPlugins() {
        registry.register("p1", pluginA);
        registry.register("p2", pluginB);
        registry.unregister("p1");
        assertFalse(registry.exists("p1"));
        assertTrue(registry.exists("p2"));
        assertEquals(1, registry.count());
    }
}
