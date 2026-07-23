package com.sporekart.marketplace.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;

class PluginListResponseTest {

    @Test
    void shouldConstructWithAllFields() {
        var plugins = List.of(
            new PluginResponse("p1", "n", "1", "a", "d", "ML", "E", "H"));
        var response = new PluginListResponse(10, 5, 3, 2, plugins);

        assertThat(response.total()).isEqualTo(10);
        assertThat(response.installed()).isEqualTo(5);
        assertThat(response.enabled()).isEqualTo(3);
        assertThat(response.errored()).isEqualTo(2);
        assertThat(response.plugins()).isSameAs(plugins);
    }

    @Test
    void shouldBeEqualForSameValues() {
        var list = List.of(
            new PluginResponse("p1", "n", "1", "a", "d", "ML", "E", "H"));
        var r1 = new PluginListResponse(1, 1, 1, 0, list);
        var r2 = new PluginListResponse(1, 1, 1, 0, list);
        assertThat(r1).isEqualTo(r2);
        assertThat(r1.hashCode()).isEqualTo(r2.hashCode());
    }

    @Test
    void shouldReturnToString() {
        var response = new PluginListResponse(1, 1, 1, 0, List.of());
        assertThat(response.toString()).contains("total=1", "installed=1");
    }
}
