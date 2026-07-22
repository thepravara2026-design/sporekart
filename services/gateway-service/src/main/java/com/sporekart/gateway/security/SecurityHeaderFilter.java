package com.sporekart.gateway.security;

import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

@Component
public class SecurityHeaderFilter {

    private static final long HSTS_MAX_AGE = 31536000;

    public void applySecurityHeaders(ServerWebExchange exchange) {
        var response = exchange.getResponse();
        var headers = response.getHeaders();

        headers.set("X-Content-Type-Options", "nosniff");
        headers.set("X-Frame-Options", "DENY");
        headers.set("X-XSS-Protection", "1; mode=block");
        headers.set("Referrer-Policy", "strict-origin-when-cross-origin");
        headers.set("Permissions-Policy", "camera=(), microphone=(), geolocation=()");
        headers.set("Strict-Transport-Security",
            "max-age=" + HSTS_MAX_AGE + "; includeSubDomains");
        headers.set("Content-Security-Policy", "default-src 'self'");
        headers.remove("Server");
        headers.remove("X-Powered-By");
    }
}
