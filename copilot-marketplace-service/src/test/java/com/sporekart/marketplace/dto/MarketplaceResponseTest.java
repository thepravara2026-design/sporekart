package com.sporekart.marketplace.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class MarketplaceResponseTest {

    @Test
    void shouldConstructWithAllFields() {
        var response = new MarketplaceResponse<>(true, "message", "data", "ERR001");
        assertThat(response.success()).isTrue();
        assertThat(response.message()).isEqualTo("message");
        assertThat(response.data()).isEqualTo("data");
        assertThat(response.errorCode()).isEqualTo("ERR001");
    }

    @Test
    void shouldCreateSuccessResponse() {
        var response = MarketplaceResponse.success("plugin data");
        assertThat(response.success()).isTrue();
        assertThat(response.message()).isEqualTo("Success");
        assertThat(response.data()).isEqualTo("plugin data");
        assertThat(response.errorCode()).isNull();
    }

    @Test
    void shouldCreateSuccessResponseWithCustomMessage() {
        var response = MarketplaceResponse.success("Plugin installed", "data");
        assertThat(response.success()).isTrue();
        assertThat(response.message()).isEqualTo("Plugin installed");
        assertThat(response.data()).isEqualTo("data");
        assertThat(response.errorCode()).isNull();
    }

    @Test
    void shouldCreateErrorResponse() {
        var response = MarketplaceResponse.error("Plugin not found", "PLUGIN_NOT_FOUND");
        assertThat(response.success()).isFalse();
        assertThat(response.message()).isEqualTo("Plugin not found");
        assertThat(response.data()).isNull();
        assertThat(response.errorCode()).isEqualTo("PLUGIN_NOT_FOUND");
    }

    @Test
    void shouldSupportDifferentDataTypes() {
        var intResponse = MarketplaceResponse.success(42);
        assertThat(intResponse.data()).isEqualTo(42);

        var listResponse = MarketplaceResponse.success(java.util.List.of("a", "b"));
        assertThat(listResponse.data()).containsExactly("a", "b");
    }

    @Test
    void shouldBeEqualForSameValues() {
        var r1 = new MarketplaceResponse<>(true, "m", "d", null);
        var r2 = new MarketplaceResponse<>(true, "m", "d", null);
        assertThat(r1).isEqualTo(r2);
        assertThat(r1.hashCode()).isEqualTo(r2.hashCode());
    }

    @Test
    void shouldReturnToString() {
        var response = new MarketplaceResponse<>(true, "m", "d", null);
        assertThat(response.toString()).contains("true", "m", "d");
    }
}
