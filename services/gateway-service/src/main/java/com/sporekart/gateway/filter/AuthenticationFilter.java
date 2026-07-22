package com.sporekart.gateway.filter;

import com.sporekart.gateway.error.GatewayException;
import com.sporekart.gateway.security.JwtValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class AuthenticationFilter implements GlobalFilter, Ordered {

    private static final Logger log = LoggerFactory.getLogger(AuthenticationFilter.class);
    private static final List<String> PUBLIC_PREFIXES = List.of(
        "/actuator/health", "/actuator/info",
        "/api/public", "/webjars", "/v3/api-docs", "/swagger-ui"
    );

    private final JwtValidator jwtValidator;

    public AuthenticationFilter(JwtValidator jwtValidator) {
        this.jwtValidator = jwtValidator;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        var request = exchange.getRequest();
        var path = request.getURI().getPath();

        if (isPublic(path)) {
            return chain.filter(exchange);
        }

        var authHeader = request.getHeaders().getFirst("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return Mono.error(new GatewayException(
                HttpStatus.UNAUTHORIZED, "UNAUTHORIZED", "Missing or invalid Authorization header"));
        }

        var token = authHeader.substring(7);
        try {
            var claims = jwtValidator.validate(token);
            if (claims == null) {
                return Mono.error(new GatewayException(
                    HttpStatus.UNAUTHORIZED, "INVALID_TOKEN", "Token validation failed"));
            }
            var mutatedRequest = request.mutate()
                .header("X-User-Id", claims.subject())
                .header("X-User-Roles", String.join(",", claims.roles()))
                .build();
            var mutatedExchange = exchange.mutate().request(mutatedRequest).build();
            return chain.filter(mutatedExchange);
        } catch (Exception e) {
            log.warn("Authentication failed: {}", e.getMessage());
            return Mono.error(new GatewayException(
                HttpStatus.UNAUTHORIZED, "AUTH_FAILED", "Authentication failed: " + e.getMessage()));
        }
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 2;
    }

    private boolean isPublic(String path) {
        return PUBLIC_PREFIXES.stream().anyMatch(path::startsWith);
    }
}
