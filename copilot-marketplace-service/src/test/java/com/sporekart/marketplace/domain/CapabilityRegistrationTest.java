package com.sporekart.marketplace.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;

import org.junit.jupiter.api.Test;

import com.sporekart.marketplace.sdk.PluginCapability;

class CapabilityRegistrationTest {

    private final Instant now = Instant.now();

    @Test
    void shouldConstructWithAllFields() {
        var reg = new CapabilityRegistration(
            "cap-001", PluginCapability.SEARCH, "p001",
            "Search Plugin", now, true);

        assertThat(reg.capabilityId()).isEqualTo("cap-001");
        assertThat(reg.capability()).isEqualTo(PluginCapability.SEARCH);
        assertThat(reg.pluginId()).isEqualTo("p001");
        assertThat(reg.pluginName()).isEqualTo("Search Plugin");
        assertThat(reg.registeredAt()).isEqualTo(now);
        assertThat(reg.enabled()).isTrue();
    }

    @Test
    void shouldSupportDisabledRegistration() {
        var reg = new CapabilityRegistration(
            "cap-002", PluginCapability.KNOWLEDGE, "p002",
            "Knowledge Plugin", now, false);

        assertThat(reg.enabled()).isFalse();
    }

    @Test
    void shouldBeEqualForSameValues() {
        var r1 = new CapabilityRegistration("c1", PluginCapability.SEARCH, "p1", "n", now, true);
        var r2 = new CapabilityRegistration("c1", PluginCapability.SEARCH, "p1", "n", now, true);
        assertThat(r1).isEqualTo(r2);
        assertThat(r1.hashCode()).isEqualTo(r2.hashCode());
    }

    @Test
    void shouldNotEqualDifferentCapabilityId() {
        var r1 = new CapabilityRegistration("c1", PluginCapability.SEARCH, "p1", "n", now, true);
        var r2 = new CapabilityRegistration("c2", PluginCapability.SEARCH, "p1", "n", now, true);
        assertThat(r1).isNotEqualTo(r2);
    }

    @Test
    void shouldReturnToString() {
        var reg = new CapabilityRegistration("c1", PluginCapability.SEARCH, "p1", "n", now, true);
        assertThat(reg.toString()).contains("c1", "SEARCH", "p1");
    }
}
