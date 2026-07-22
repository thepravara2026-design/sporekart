package com.sporekart.gateway.security;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SecurityHeaderFilterTest {

    private final SecurityHeaderFilter filter = new SecurityHeaderFilter();

    @Test
    void shouldApplyAllSecurityHeaders() {
        var exchange = mock(ServerWebExchange.class);
        var response = mock(ServerHttpResponse.class);
        var headers = new HttpHeaders();
        when(response.getHeaders()).thenReturn(headers);
        when(exchange.getResponse()).thenReturn(response);

        filter.applySecurityHeaders(exchange);

        assertThat(headers.getFirst("X-Content-Type-Options")).isEqualTo("nosniff");
        assertThat(headers.getFirst("X-Frame-Options")).isEqualTo("DENY");
        assertThat(headers.getFirst("X-XSS-Protection")).isEqualTo("1; mode=block");
        assertThat(headers.getFirst("Referrer-Policy")).isEqualTo("strict-origin-when-cross-origin");
        assertThat(headers.getFirst("Strict-Transport-Security")).contains("max-age=31536000");
        assertThat(headers.getFirst("Content-Security-Policy")).isEqualTo("default-src 'self'");
    }

    @Test
    void shouldRemoveServerHeader() {
        var exchange = mock(ServerWebExchange.class);
        var response = mock(ServerHttpResponse.class);
        var headers = new HttpHeaders();
        headers.set("Server", "Apache");
        headers.set("X-Powered-By", "Spring");
        when(response.getHeaders()).thenReturn(headers);
        when(exchange.getResponse()).thenReturn(response);

        filter.applySecurityHeaders(exchange);

        assertThat(headers.get("Server")).isNull();
        assertThat(headers.get("X-Powered-By")).isNull();
    }
}
