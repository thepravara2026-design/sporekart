package com.sporekart.marketplace.sdk;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class PluginHookTest {

    @Test
    void shouldHaveAllTenValues() {
        var values = PluginHook.values();
        assertThat(values).containsExactly(
            PluginHook.BEFORE_CONVERSATION,
            PluginHook.AFTER_CONVERSATION,
            PluginHook.BEFORE_QUERY,
            PluginHook.AFTER_QUERY,
            PluginHook.BEFORE_ANALYTICS,
            PluginHook.AFTER_ANALYTICS,
            PluginHook.ON_ERROR,
            PluginHook.ON_DASHBOARD_LOAD,
            PluginHook.ON_REPORT_GENERATE,
            PluginHook.ON_NOTIFICATION_SEND);
    }

    @Test
    void shouldReturnName() {
        assertThat(PluginHook.BEFORE_CONVERSATION.name()).isEqualTo("BEFORE_CONVERSATION");
        assertThat(PluginHook.ON_NOTIFICATION_SEND.name()).isEqualTo("ON_NOTIFICATION_SEND");
    }

    @Test
    void shouldParseFromString() {
        assertThat(PluginHook.valueOf("AFTER_QUERY")).isEqualTo(PluginHook.AFTER_QUERY);
        assertThat(PluginHook.valueOf("ON_ERROR")).isEqualTo(PluginHook.ON_ERROR);
    }
}
