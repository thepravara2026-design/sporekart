package com.sporekart.gateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.Instant;

@Component
public class LoggingFilter implements GlobalFilter, Ordered {

    private static final Logger log = LoggerFactory.getLogger(LoggingFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        var request = exchange.getRequest();
        var startTime = Instant.now();

        log.info("--> {} {} [{}]",
            request.getMethod(),
            request.getURI().getPath(),
            request.getHeaders().getFirst("X-Correlation-Id"));

        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            var response = exchange.getResponse();
            var duration = Instant.now().toEpochMilli() - startTime.toEpochMilli();
            log.info("<-- {} {} [{}] status={} duration={}ms",
                request.getMethod(),
                request.getURI().getPath(),
                request.getHeaders().getFirst("X-Correlation-Id"),
                response.getStatusCode(),
                duration);
        }));
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 1;
    }
}
