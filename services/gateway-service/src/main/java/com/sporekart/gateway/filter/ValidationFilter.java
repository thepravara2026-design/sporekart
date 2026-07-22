package com.sporekart.gateway.filter;

import com.sporekart.gateway.error.GatewayException;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class ValidationFilter implements GlobalFilter, Ordered {

    private static final long MAX_CONTENT_LENGTH = 10 * 1024 * 1024;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        var request = exchange.getRequest();
        var path = request.getURI().getPath();

        validateMethod(request);
        validateContentLength(request);

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 4;
    }

    private void validateMethod(ServerHttpRequest request) {
        var method = request.getMethod();
        if (method == null) {
            throw new GatewayException(HttpStatus.BAD_REQUEST, "INVALID_METHOD", "HTTP method is required");
        }
    }

    private void validateContentLength(ServerHttpRequest request) {
        var contentLength = request.getHeaders().getContentLength();
        if (contentLength > MAX_CONTENT_LENGTH) {
            throw new GatewayException(HttpStatus.REQUEST_ENTITY_TOO_LARGE,
                "PAYLOAD_TOO_LARGE", "Request body exceeds maximum allowed size of 10MB");
        }
    }
}
