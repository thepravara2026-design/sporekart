package com.sporekart.marketplace.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class PluginDependencyTest {

    @Test
    void shouldConstructWithAllFields() {
        var dep = new PluginDependency("plugin-a", "1.0.0", true);
        assertThat(dep.pluginId()).isEqualTo("plugin-a");
        assertThat(dep.version()).isEqualTo("1.0.0");
        assertThat(dep.required()).isTrue();
    }

    @Test
    void shouldSupportOptionalDependency() {
        var dep = new PluginDependency("plugin-b", "2.0.0", false);
        assertThat(dep.required()).isFalse();
    }

    @Test
    void shouldBeEqualForSameValues() {
        var d1 = new PluginDependency("p1", "1.0", true);
        var d2 = new PluginDependency("p1", "1.0", true);
        assertThat(d1).isEqualTo(d2);
        assertThat(d1.hashCode()).isEqualTo(d2.hashCode());
    }

    @Test
    void shouldNotEqualDifferentPluginId() {
        var d1 = new PluginDependency("p1", "1.0", true);
        var d2 = new PluginDependency("p2", "1.0", true);
        assertThat(d1).isNotEqualTo(d2);
    }

    @Test
    void shouldReturnToString() {
        var dep = new PluginDependency("p1", "1.0", true);
        assertThat(dep.toString()).contains("p1", "1.0", "true");
    }
}
