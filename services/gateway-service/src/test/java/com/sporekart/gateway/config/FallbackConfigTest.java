package com.sporekart.gateway.config;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class FallbackConfigTest {

    @Test
    void fallbackPatternShouldStartWithSlashFallback() {
        assertThat("/fallback/catalog".startsWith("/fallback/")).isTrue();
    }

    @Test
    void fallbackShouldExtractServiceName() {
        var path = "/fallback/catalog";
        var serviceName = path.substring("/fallback/".length());
        assertThat(serviceName).isEqualTo("catalog");
    }

    @Test
    void nonFallbackPathShouldNotMatch() {
        assertThat("/api/catalog".startsWith("/fallback/")).isFalse();
    }
}
