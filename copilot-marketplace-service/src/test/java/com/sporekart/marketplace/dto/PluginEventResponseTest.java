package com.sporekart.marketplace.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.util.Map;

import org.junit.jupiter.api.Test;

class PluginEventResponseTest {

    private final Instant now = Instant.now();

    @Test
    void shouldConstructWithAllFields() {
        var data = Map.<String, Object>of("key", "value");
        var response = new PluginEventResponse("evt-001", "INSTALL", "p001", now, data);

        assertThat(response.eventId()).isEqualTo("evt-001");
        assertThat(response.eventType()).isEqualTo("INSTALL");
        assertThat(response.pluginId()).isEqualTo("p001");
        assertThat(response.timestamp()).isEqualTo(now);
        assertThat(response.data()).isSameAs(data);
    }

    @Test
    void shouldBeEqualForSameValues() {
        var r1 = new PluginEventResponse("e1", "INSTALL", "p1", now, Map.of());
        var r2 = new PluginEventResponse("e1", "INSTALL", "p1", now, Map.of());
        assertThat(r1).isEqualTo(r2);
        assertThat(r1.hashCode()).isEqualTo(r2.hashCode());
    }

    @Test
    void shouldNotEqualDifferentEventId() {
        var r1 = new PluginEventResponse("e1", "INSTALL", "p1", now, Map.of());
        var r2 = new PluginEventResponse("e2", "INSTALL", "p1", now, Map.of());
        assertThat(r1).isNotEqualTo(r2);
    }

    @Test
    void shouldReturnToString() {
        var response = new PluginEventResponse("e1", "INSTALL", "p1", now, Map.of());
        assertThat(response.toString()).contains("e1", "INSTALL", "p1");
    }
}
