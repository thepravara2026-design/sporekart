package com.sporekart.marketplace.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.sporekart.marketplace.domain.PluginHealthStatus;

class PluginHealthResponseTest {

    @Test
    void shouldConstructWithAllFields() {
        var statuses = List.of(
            new PluginHealthStatus("p1", "HEALTHY", Instant.now(), 100L, Map.of(), 0));
        var summary = Map.<String, Object>of("uptime", "99.9%");
        var response = new PluginHealthResponse(10, 5, 3, 2, statuses, summary);

        assertThat(response.totalPlugins()).isEqualTo(10);
        assertThat(response.healthy()).isEqualTo(5);
        assertThat(response.unhealthy()).isEqualTo(3);
        assertThat(response.unknown()).isEqualTo(2);
        assertThat(response.statuses()).isSameAs(statuses);
        assertThat(response.summary()).isSameAs(summary);
    }

    @Test
    void shouldBeEqualForSameValues() {
        var now = Instant.now();
        var r1 = new PluginHealthResponse(1, 1, 0, 0, List.of(
            new PluginHealthStatus("p1", "HEALTHY", now, 0, Map.of(), 0)), Map.of());
        var r2 = new PluginHealthResponse(1, 1, 0, 0, List.of(
            new PluginHealthStatus("p1", "HEALTHY", now, 0, Map.of(), 0)), Map.of());
        assertThat(r1).isEqualTo(r2);
        assertThat(r1.hashCode()).isEqualTo(r2.hashCode());
    }

    @Test
    void shouldReturnToString() {
        var response = new PluginHealthResponse(1, 1, 0, 0, List.of(), Map.of());
        assertThat(response.toString()).contains("totalPlugins=1", "healthy=1");
    }
}
