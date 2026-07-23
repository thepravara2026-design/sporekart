package com.sporekart.marketplace.sdk;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.util.Map;

import org.junit.jupiter.api.Test;

class PluginMetadataTest {

    private final Instant now = Instant.now();

    @Test
    void shouldConstructWithAllFields() {
        var attrs = Map.of("key1", "val1");
        var metadata = new PluginMetadata(
            "p001", "1.0.0", now, now, now, "marketplace", attrs);

        assertThat(metadata.pluginId()).isEqualTo("p001");
        assertThat(metadata.installedVersion()).isEqualTo("1.0.0");
        assertThat(metadata.installedAt()).isEqualTo(now);
        assertThat(metadata.lastUpdatedAt()).isEqualTo(now);
        assertThat(metadata.lastHealthCheckAt()).isEqualTo(now);
        assertThat(metadata.installSource()).isEqualTo("marketplace");
        assertThat(metadata.customAttributes()).isSameAs(attrs);
    }

    @Test
    void shouldSupportEqualsAndHashCode() {
        var m1 = new PluginMetadata("p1", "1", now, now, now, "src", Map.of());
        var m2 = new PluginMetadata("p1", "1", now, now, now, "src", Map.of());

        assertThat(m1).isEqualTo(m2);
        assertThat(m1.hashCode()).isEqualTo(m2.hashCode());
    }

    @Test
    void shouldNotEqualDifferentId() {
        var m1 = new PluginMetadata("p1", "1", now, now, now, "src", Map.of());
        var m2 = new PluginMetadata("p2", "1", now, now, now, "src", Map.of());

        assertThat(m1).isNotEqualTo(m2);
    }

    @Test
    void shouldReturnToString() {
        var metadata = new PluginMetadata("p1", "1", now, now, now, "src", Map.of());

        assertThat(metadata.toString()).contains("p1", "1");
    }
}
