package com.sporekart.marketplace.sdk;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class PluginPermissionTest {

    @Test
    void shouldHaveAllTenValues() {
        var values = PluginPermission.values();
        assertThat(values).containsExactly(
            PluginPermission.READ_PRODUCTS,
            PluginPermission.READ_ORDERS,
            PluginPermission.READ_INVENTORY,
            PluginPermission.READ_CUSTOMERS,
            PluginPermission.READ_ANALYTICS,
            PluginPermission.READ_TRAINING,
            PluginPermission.EXECUTE_AI,
            PluginPermission.ACCESS_KNOWLEDGE,
            PluginPermission.SEND_NOTIFICATIONS,
            PluginPermission.CALL_APIS);
    }

    @Test
    void shouldReturnName() {
        assertThat(PluginPermission.READ_PRODUCTS.name()).isEqualTo("READ_PRODUCTS");
        assertThat(PluginPermission.CALL_APIS.name()).isEqualTo("CALL_APIS");
    }

    @Test
    void shouldParseFromString() {
        assertThat(PluginPermission.valueOf("READ_ORDERS")).isEqualTo(PluginPermission.READ_ORDERS);
        assertThat(PluginPermission.valueOf("EXECUTE_AI")).isEqualTo(PluginPermission.EXECUTE_AI);
    }
}
