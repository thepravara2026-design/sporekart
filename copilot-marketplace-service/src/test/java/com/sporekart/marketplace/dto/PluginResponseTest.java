package com.sporekart.marketplace.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sporekart.marketplace.domain.PluginInstance;
import com.sporekart.marketplace.domain.PluginState;
import com.sporekart.marketplace.sdk.PluginManifest;
import com.sporekart.marketplace.sdk.PluginMetadata;
import com.sporekart.marketplace.sdk.PluginType;

class PluginResponseTest {

    private PluginInstance instance;

    @BeforeEach
    void setUp() {
        var manifest = new PluginManifest(
            "p001", "Test", "1.0", "Author", "Desc", PluginType.ML,
            List.of(), List.of(), List.of(), "1", "2", "/h", Map.of(), "e");
        var metadata = new PluginMetadata("p001", "1.0", null, null, null, "src", Map.of());
        instance = new PluginInstance("p001", manifest, metadata);
    }

    @Test
    void shouldConstructWithAllFields() {
        var response = new PluginResponse("p1", "n", "1", "a", "d", "ML", "ENABLED", "HEALTHY");
        assertThat(response.pluginId()).isEqualTo("p1");
        assertThat(response.name()).isEqualTo("n");
        assertThat(response.version()).isEqualTo("1");
        assertThat(response.author()).isEqualTo("a");
        assertThat(response.description()).isEqualTo("d");
        assertThat(response.type()).isEqualTo("ML");
        assertThat(response.state()).isEqualTo("ENABLED");
        assertThat(response.healthStatus()).isEqualTo("HEALTHY");
    }

    @Test
    void shouldCreateFromInstance() {
        var response = PluginResponse.from(instance);
        assertThat(response.pluginId()).isEqualTo("p001");
        assertThat(response.name()).isEqualTo("Test");
        assertThat(response.version()).isEqualTo("1.0");
        assertThat(response.author()).isEqualTo("Author");
        assertThat(response.description()).isEqualTo("Desc");
        assertThat(response.type()).isEqualTo("ML");
        assertThat(response.state()).isEqualTo("INSTALLED");
        assertThat(response.healthStatus()).isEqualTo("UNKNOWN");
    }

    @Test
    void shouldCreateFromInstanceWithHealthStatus() {
        var response = PluginResponse.from(instance, "HEALTHY");
        assertThat(response.pluginId()).isEqualTo("p001");
        assertThat(response.healthStatus()).isEqualTo("HEALTHY");
    }

    @Test
    void shouldBeEqualForSameValues() {
        var r1 = new PluginResponse("p1", "n", "1", "a", "d", "ML", "E", "H");
        var r2 = new PluginResponse("p1", "n", "1", "a", "d", "ML", "E", "H");
        assertThat(r1).isEqualTo(r2);
        assertThat(r1.hashCode()).isEqualTo(r2.hashCode());
    }

    @Test
    void shouldReturnToString() {
        var response = new PluginResponse("p1", "n", "1", "a", "d", "ML", "E", "H");
        assertThat(response.toString()).contains("p1", "n", "ML", "E", "H");
    }
}
