package com.sporekart.marketplace.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.util.Map;

import org.junit.jupiter.api.Test;

class PluginHealthStatusTest {

    private final Instant now = Instant.now();

    @Test
    void shouldConstructWithAllFields() {
        var details = Map.<String, Object>of("cpu", "0.5");
        var status = new PluginHealthStatus("p001", "HEALTHY", now, 150L, details, 0);

        assertThat(status.pluginId()).isEqualTo("p001");
        assertThat(status.status()).isEqualTo("HEALTHY");
        assertThat(status.lastCheckedAt()).isEqualTo(now);
        assertThat(status.responseTimeMs()).isEqualTo(150L);
        assertThat(status.details()).isSameAs(details);
        assertThat(status.consecutiveFailures()).isZero();
    }

    @Test
    void isHealthy_shouldReturnTrueWhenStatusIsHealthy() {
        var status = new PluginHealthStatus("p1", "HEALTHY", now, 0, Map.of(), 0);
        assertThat(status.isHealthy()).isTrue();
        assertThat(status.isUnhealthy()).isFalse();
    }

    @Test
    void isUnhealthy_shouldReturnTrueWhenStatusIsUnhealthy() {
        var status = new PluginHealthStatus("p1", "UNHEALTHY", now, 0, Map.of(), 3);
        assertThat(status.isUnhealthy()).isTrue();
        assertThat(status.isHealthy()).isFalse();
    }

    @Test
    void shouldHandleUnknownStatus() {
        var status = new PluginHealthStatus("p1", "UNKNOWN", now, 0, Map.of(), 0);
        assertThat(status.isHealthy()).isFalse();
        assertThat(status.isUnhealthy()).isFalse();
    }

    @Test
    void shouldBeEqualForSameValues() {
        var s1 = new PluginHealthStatus("p1", "HEALTHY", now, 100L, Map.of("k", "v"), 1);
        var s2 = new PluginHealthStatus("p1", "HEALTHY", now, 100L, Map.of("k", "v"), 1);
        assertThat(s1).isEqualTo(s2);
        assertThat(s1.hashCode()).isEqualTo(s2.hashCode());
    }

    @Test
    void shouldNotEqualDifferentPluginId() {
        var s1 = new PluginHealthStatus("p1", "HEALTHY", now, 0, Map.of(), 0);
        var s2 = new PluginHealthStatus("p2", "HEALTHY", now, 0, Map.of(), 0);
        assertThat(s1).isNotEqualTo(s2);
    }

    @Test
    void shouldReturnToString() {
        var status = new PluginHealthStatus("p1", "HEALTHY", now, 0, Map.of(), 0);
        assertThat(status.toString()).contains("p1", "HEALTHY");
    }

    @Test
    void shouldTrackConsecutiveFailures() {
        var status = new PluginHealthStatus("p1", "UNHEALTHY", now, 5000L, Map.of(), 5);
        assertThat(status.consecutiveFailures()).isEqualTo(5);
    }
}
