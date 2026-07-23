package com.sporekart.marketplace.sdk;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class PluginTypeTest {

    @Test
    void shouldHaveAllTenValues() {
        var values = PluginType.values();
        assertThat(values).containsExactly(
            PluginType.AI_COPILOT,
            PluginType.KNOWLEDGE,
            PluginType.WORKFLOW,
            PluginType.ANALYTICS,
            PluginType.CONNECTOR,
            PluginType.DASHBOARD,
            PluginType.AUTOMATION,
            PluginType.REPORTING,
            PluginType.NOTIFICATION,
            PluginType.ML);
    }

    @Test
    void shouldReturnName() {
        assertThat(PluginType.AI_COPILOT.name()).isEqualTo("AI_COPILOT");
        assertThat(PluginType.ML.name()).isEqualTo("ML");
    }

    @Test
    void shouldParseFromString() {
        assertThat(PluginType.valueOf("AI_COPILOT")).isEqualTo(PluginType.AI_COPILOT);
        assertThat(PluginType.valueOf("WORKFLOW")).isEqualTo(PluginType.WORKFLOW);
        assertThat(PluginType.valueOf("ML")).isEqualTo(PluginType.ML);
    }

    @Test
    void shouldHaveDistinctOrdinals() {
        var values = PluginType.values();
        for (int i = 0; i < values.length; i++) {
            assertThat(values[i].ordinal()).isEqualTo(i);
        }
    }
}
