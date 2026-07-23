package com.sporekart.marketplace.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class PluginStateTest {

    @Test
    void shouldHaveAllSevenValues() {
        var values = PluginState.values();
        assertThat(values).containsExactly(
            PluginState.INSTALLED,
            PluginState.ENABLED,
            PluginState.DISABLED,
            PluginState.ERROR,
            PluginState.UPGRADING,
            PluginState.UNINSTALLING,
            PluginState.UNINSTALLED);
    }

    @Test
    void shouldReturnName() {
        assertThat(PluginState.INSTALLED.name()).isEqualTo("INSTALLED");
        assertThat(PluginState.UNINSTALLED.name()).isEqualTo("UNINSTALLED");
    }

    @Test
    void shouldParseFromString() {
        assertThat(PluginState.valueOf("ENABLED")).isEqualTo(PluginState.ENABLED);
        assertThat(PluginState.valueOf("ERROR")).isEqualTo(PluginState.ERROR);
    }

    @Test
    void shouldHaveDistinctOrdinals() {
        var values = PluginState.values();
        for (int i = 0; i < values.length; i++) {
            assertThat(values[i].ordinal()).isEqualTo(i);
        }
    }
}
