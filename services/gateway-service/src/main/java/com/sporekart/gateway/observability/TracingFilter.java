package com.sporekart.gateway.observability;

import io.micrometer.tracing.Tracer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class TracingFilter implements GlobalFilter, Ordered {

    private static final Logger log = LoggerFactory.getLogger(TracingFilter.class);
    private static final String TRACE_ID_HEADER = "X-Trace-Id";

    private final Tracer tracer;

    public TracingFilter(Tracer tracer) {
        this.tracer = tracer;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        var span = tracer.currentSpan();
        var traceId = span != null ? span.context().traceId() : "unknown";

        var request = exchange.getRequest().mutate()
            .header(TRACE_ID_HEADER, traceId)
            .build();
        var mutatedExchange = exchange.mutate().request(request).build();

        return chain.filter(mutatedExchange);
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 5;
    }
}
