package com.sporekart.marketplace.sdk;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class PluginCapabilityTest {

    @Test
    void shouldHaveAllFourteenValues() {
        var values = PluginCapability.values();
        assertThat(values).containsExactly(
            PluginCapability.CONVERSATION,
            PluginCapability.KNOWLEDGE,
            PluginCapability.SEARCH,
            PluginCapability.FORECAST,
            PluginCapability.MARKETING,
            PluginCapability.ANALYTICS,
            PluginCapability.REPORTING,
            PluginCapability.INVENTORY,
            PluginCapability.ORDERS,
            PluginCapability.TRAINING,
            PluginCapability.NOTIFICATIONS,
            PluginCapability.AI_MODELS,
            PluginCapability.DASHBOARD,
            PluginCapability.WORKFLOW,
            PluginCapability.AUTOMATION);
    }

    @Test
    void shouldReturnName() {
        assertThat(PluginCapability.CONVERSATION.name()).isEqualTo("CONVERSATION");
        assertThat(PluginCapability.AUTOMATION.name()).isEqualTo("AUTOMATION");
    }

    @Test
    void shouldParseFromString() {
        assertThat(PluginCapability.valueOf("SEARCH")).isEqualTo(PluginCapability.SEARCH);
        assertThat(PluginCapability.valueOf("AI_MODELS")).isEqualTo(PluginCapability.AI_MODELS);
    }
}
