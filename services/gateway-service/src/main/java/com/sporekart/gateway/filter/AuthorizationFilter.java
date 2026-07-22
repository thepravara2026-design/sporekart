package com.sporekart.gateway.filter;

import com.sporekart.gateway.error.GatewayException;
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
import java.util.Map;

@Component
public class AuthorizationFilter implements GlobalFilter, Ordered {

    private static final Logger log = LoggerFactory.getLogger(AuthorizationFilter.class);

    private static final Map<String, List<String>> PATH_ROLES = Map.of(
        "/api/admin", List.of("ADMIN"),
        "/api/analytics", List.of("ADMIN", "ANALYST"),
        "/api/ai/admin", List.of("ADMIN"),
        "/internal", List.of("SYSTEM", "ADMIN")
    );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        var request = exchange.getRequest();
        var path = request.getURI().getPath();

        var requiredRoles = findRequiredRoles(path);
        if (requiredRoles.isEmpty()) {
            return chain.filter(exchange);
        }

        var rolesHeader = request.getHeaders().getFirst("X-User-Roles");
        if (rolesHeader == null || rolesHeader.isBlank()) {
            return Mono.error(new GatewayException(
                HttpStatus.FORBIDDEN, "FORBIDDEN", "No roles found for authorization"));
        }

        var userRoles = List.of(rolesHeader.split(","));
        var hasRole = requiredRoles.stream().anyMatch(userRoles::contains);
        if (!hasRole) {
            log.warn("Authorization denied for path {}: required roles={}, user roles={}",
                path, requiredRoles, userRoles);
            return Mono.error(new GatewayException(
                HttpStatus.FORBIDDEN, "FORBIDDEN", "Insufficient permissions"));
        }

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 3;
    }

    private List<String> findRequiredRoles(String path) {
        return PATH_ROLES.entrySet().stream()
            .filter(e -> path.startsWith(e.getKey()))
            .map(Map.Entry::getValue)
            .findFirst()
            .orElse(List.of());
    }
}
