package com.sporekart.marketplace.sdk;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PluginContextTest {

    private PluginContext context;

    @BeforeEach
    void setUp() {
        context = new PluginContext("p001",
            Map.of("timeout", "30", "retries", "3"),
            Map.of("tenant", "acme"));
    }

    @Test
    void shouldReturnPluginId() {
        assertThat(context.getPluginId()).isEqualTo("p001");
    }

    @Test
    void shouldReturnConfig() {
        assertThat(context.getConfig())
            .containsEntry("timeout", "30")
            .containsEntry("retries", "3");
    }

    @Test
    void shouldReturnWorkspace() {
        assertThat(context.getWorkspace()).containsEntry("tenant", "acme");
    }

    @Test
    void shouldGetConfigValueByKey() {
        assertThat(context.getConfigValue("timeout")).isEqualTo("30");
    }

    @Test
    void shouldReturnNullForMissingConfigKey() {
        assertThat(context.getConfigValue("nonexistent")).isNull();
    }

    @Test
    void shouldReturnConfigValueWhenValueIsNonString() {
        var ctx = new PluginContext("p1", Map.of("count", 42), Map.of());
        assertThat(ctx.getConfigValue("count")).isEqualTo("42");
    }
}
