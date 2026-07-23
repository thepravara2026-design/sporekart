package com.sporekart.marketplace.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class PluginInstallRequestTest {

    @Test
    void shouldConstructWithAllFields() {
        var request = new PluginInstallRequest("p001", "1.0.0", "marketplace");

        assertThat(request.pluginId()).isEqualTo("p001");
        assertThat(request.version()).isEqualTo("1.0.0");
        assertThat(request.source()).isEqualTo("marketplace");
    }

    @Test
    void shouldAllowNullVersionAndSource() {
        var request = new PluginInstallRequest("p001", null, null);

        assertThat(request.pluginId()).isEqualTo("p001");
        assertThat(request.version()).isNull();
        assertThat(request.source()).isNull();
    }

    @Test
    void shouldBeEqualForSameValues() {
        var r1 = new PluginInstallRequest("p1", "1.0", "src");
        var r2 = new PluginInstallRequest("p1", "1.0", "src");
        assertThat(r1).isEqualTo(r2);
        assertThat(r1.hashCode()).isEqualTo(r2.hashCode());
    }

    @Test
    void shouldNotEqualDifferentPluginId() {
        var r1 = new PluginInstallRequest("p1", "1.0", "src");
        var r2 = new PluginInstallRequest("p2", "1.0", "src");
        assertThat(r1).isNotEqualTo(r2);
    }

    @Test
    void shouldReturnToString() {
        var request = new PluginInstallRequest("p1", "1.0", "src");
        assertThat(request.toString()).contains("p1", "1.0", "src");
    }
}
