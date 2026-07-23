package com.sporekart.marketplace.sdk;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

class PluginManifestTest {

    @Test
    void shouldConstructWithAllFields() {
        var capabilities = List.of(PluginCapability.SEARCH, PluginCapability.KNOWLEDGE);
        var permissions = List.of(PluginPermission.READ_PRODUCTS);
        var deps = List.of("plugin-a", "plugin-b");
        var schema = Map.<String, Object>of("apiKey", Map.of("type", "string"));

        var manifest = new PluginManifest(
            "p001", "Test Plugin", "1.0.0", "Author",
            "A test plugin", PluginType.AI_COPILOT,
            capabilities, permissions, deps,
            "1.0.0", "2.0.0", "/health", schema, "main.js");

        assertThat(manifest.pluginId()).isEqualTo("p001");
        assertThat(manifest.name()).isEqualTo("Test Plugin");
        assertThat(manifest.version()).isEqualTo("1.0.0");
        assertThat(manifest.author()).isEqualTo("Author");
        assertThat(manifest.description()).isEqualTo("A test plugin");
        assertThat(manifest.type()).isEqualTo(PluginType.AI_COPILOT);
        assertThat(manifest.capabilities()).isSameAs(capabilities);
        assertThat(manifest.requiredPermissions()).isSameAs(permissions);
        assertThat(manifest.dependencies()).isSameAs(deps);
        assertThat(manifest.minPlatformVersion()).isEqualTo("1.0.0");
        assertThat(manifest.maxPlatformVersion()).isEqualTo("2.0.0");
        assertThat(manifest.healthEndpoint()).isEqualTo("/health");
        assertThat(manifest.configurationSchema()).isSameAs(schema);
        assertThat(manifest.entryPoint()).isEqualTo("main.js");
    }

    @Test
    void shouldSupportEqualsAndHashCode() {
        var m1 = new PluginManifest("p1", "n", "1", "a", "d", PluginType.ML,
            List.of(), List.of(), List.of(), "1", "2", "/h", Map.of(), "e");
        var m2 = new PluginManifest("p1", "n", "1", "a", "d", PluginType.ML,
            List.of(), List.of(), List.of(), "1", "2", "/h", Map.of(), "e");

        assertThat(m1).isEqualTo(m2);
        assertThat(m1.hashCode()).isEqualTo(m2.hashCode());
    }

    @Test
    void shouldNotEqualDifferentManifest() {
        var m1 = new PluginManifest("p1", "n", "1", "a", "d", PluginType.ML,
            List.of(), List.of(), List.of(), "1", "2", "/h", Map.of(), "e");
        var m2 = new PluginManifest("p2", "n", "1", "a", "d", PluginType.ML,
            List.of(), List.of(), List.of(), "1", "2", "/h", Map.of(), "e");

        assertThat(m1).isNotEqualTo(m2);
    }

    @Test
    void shouldReturnToString() {
        var manifest = new PluginManifest("p1", "n", "1", "a", "d", PluginType.ML,
            List.of(), List.of(), List.of(), "1", "2", "/h", Map.of(), "e");

        assertThat(manifest.toString()).contains("p1", "n", "ML");
    }
}
