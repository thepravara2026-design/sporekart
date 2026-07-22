package com.sporekart.gateway.filter;

import org.slf4j.MDC;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
public class CorrelationIdFilter implements GlobalFilter, Ordered {

    public static final String CORRELATION_ID_HEADER = "X-Correlation-Id";
    public static final String REQUEST_ID_HEADER = "X-Request-Id";
    public static final String CORRELATION_ID_MDC = "correlationId";
    public static final String REQUEST_ID_MDC = "requestId";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        var request = exchange.getRequest();
        var correlationId = request.getHeaders()
            .getFirst(CORRELATION_ID_HEADER);
        if (correlationId == null || correlationId.isBlank()) {
            correlationId = UUID.randomUUID().toString();
        }

        var requestId = UUID.randomUUID().toString();

        MDC.put(CORRELATION_ID_MDC, correlationId);
        MDC.put(REQUEST_ID_MDC, requestId);

        var mutatedRequest = request.mutate()
            .header(CORRELATION_ID_HEADER, correlationId)
            .header(REQUEST_ID_HEADER, requestId)
            .build();

        var mutatedExchange = exchange.mutate()
            .request(mutatedRequest)
            .build();

        return chain.filter(mutatedExchange)
            .doFinally(signalType -> {
                MDC.remove(CORRELATION_ID_MDC);
                MDC.remove(REQUEST_ID_MDC);
            });
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
