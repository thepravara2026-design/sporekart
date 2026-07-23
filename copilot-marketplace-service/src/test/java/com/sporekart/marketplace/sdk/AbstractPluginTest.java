package com.sporekart.marketplace.sdk;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AbstractPluginTest {

    private TestPlugin plugin;
    private PluginContext context;

    @BeforeEach
    void setUp() {
        plugin = new TestPlugin();
        context = new PluginContext("p001", Map.of("key", "val"), Map.of());
    }

    @Test
    void shouldSetContextOnInstall() {
        plugin.onInstall(context);
        assertThat(plugin.getContext()).isSameAs(context);
    }

    @Test
    void shouldEnableOnEnable() {
        plugin.onEnable(context);
        assertThat(plugin.isEnabled()).isTrue();
        assertThat(plugin.getContext()).isSameAs(context);
    }

    @Test
    void shouldDisableOnDisable() {
        plugin.onEnable(context);
        plugin.onDisable(context);
        assertThat(plugin.isEnabled()).isFalse();
    }

    @Test
    void shouldClearContextOnUninstall() {
        plugin.onInstall(context);
        plugin.onUninstall(context);
        assertThat(plugin.isEnabled()).isFalse();
        assertThat(plugin.getContext()).isNull();
    }

    @Test
    void shouldSetContextOnUpgrade() {
        plugin.onUpgrade(context, "1.0.0");
        assertThat(plugin.getContext()).isSameAs(context);
    }

    @Test
    void shouldSetContextOnDowngrade() {
        plugin.onDowngrade(context, "2.0.0");
        assertThat(plugin.getContext()).isSameAs(context);
    }

    @Test
    void shouldReturnHealthyStatusWhenEnabled() {
        plugin.onEnable(context);
        var health = plugin.healthCheck();
        assertThat(health).containsEntry("status", "HEALTHY");
        assertThat(health).containsEntry("pluginId", "test-plugin");
    }

    @Test
    void shouldReturnDisabledStatusWhenNotEnabled() {
        var health = plugin.healthCheck();
        assertThat(health).containsEntry("status", "DISABLED");
        assertThat(health).containsEntry("pluginId", "test-plugin");
    }

    @Test
    void shouldExecuteAndReturnEmpty() {
        assertThat(plugin.execute("test", Map.of())).isEmpty();
    }

    @Test
    void shouldReturnManifest() {
        var m = plugin.getManifest();
        assertThat(m.pluginId()).isEqualTo("test-plugin");
        assertThat(m.name()).isEqualTo("Test Plugin");
    }

    static class TestPlugin extends AbstractPlugin {
        @Override
        public PluginManifest getManifest() {
            return new PluginManifest(
                "test-plugin", "Test Plugin", "1.0.0", "tester",
                "A test plugin", PluginType.ANALYTICS,
                List.of(), List.of(), List.of(),
                "1.0.0", "2.0.0", "/health", Map.of(), "main.js");
        }

        @Override
        public Map<String, Object> execute(String action, Map<String, Object> params) {
            return Map.of();
        }
    }
}
