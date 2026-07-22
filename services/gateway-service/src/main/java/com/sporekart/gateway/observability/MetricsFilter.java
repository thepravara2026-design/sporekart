package com.sporekart.gateway.observability;

import com.sporekart.gateway.filter.CorrelationIdFilter;
import com.sporekart.gateway.security.SecurityHeaderFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.Instant;

@Component
public class MetricsFilter implements GlobalFilter, Ordered {

    private static final Logger log = LoggerFactory.getLogger(MetricsFilter.class);

    private final MetricsRecorder metricsRecorder;
    private final SecurityHeaderFilter securityHeaderFilter;

    public MetricsFilter(MetricsRecorder metricsRecorder, SecurityHeaderFilter securityHeaderFilter) {
        this.metricsRecorder = metricsRecorder;
        this.securityHeaderFilter = securityHeaderFilter;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        var startTime = Instant.now();
        var request = exchange.getRequest();
        var method = request.getMethod() != null ? request.getMethod().name() : "UNKNOWN";
        var path = request.getURI().getPath();

        securityHeaderFilter.applySecurityHeaders(exchange);

        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            var response = exchange.getResponse();
            var statusCode = response.getStatusCode() != null ? response.getStatusCode() : HttpStatus.OK;
            var status = HttpStatus.resolve(statusCode.value());
            if (status == null) status = HttpStatus.INTERNAL_SERVER_ERROR;
            var durationMs = Instant.now().toEpochMilli() - startTime.toEpochMilli();

            metricsRecorder.recordRequest(method, path, extractRoute(path), status, durationMs);

            if (status.isError()) {
                metricsRecorder.recordError(status.value() + "_" + status.getReasonPhrase());
            }
        }));
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE - 1;
    }

    private String extractRoute(String path) {
        var parts = path.split("/");
        return parts.length > 2 ? parts[1] + "/" + parts[2] : "unknown";
    }
}
