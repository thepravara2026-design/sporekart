package com.sporekart.gateway.observability;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.Timer;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;

@Component
public class MetricsRecorder {

    private final MeterRegistry meterRegistry;

    public MetricsRecorder(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    public void recordRequest(String method, String path, String route, HttpStatus status, long durationMs) {
        var tags = List.of(
            Tag.of("method", method),
            Tag.of("path", path),
            Tag.of("route", route != null ? route : "unknown"),
            Tag.of("status", String.valueOf(status.value())),
            Tag.of("status_group", status.is2xxSuccessful() ? "2xx" :
                status.is4xxClientError() ? "4xx" : "5xx")
        );

        meterRegistry.counter("gateway.requests.total", tags).increment();
        meterRegistry.counter("gateway.requests.status", List.of(Tag.of("status", String.valueOf(status.value())))).increment();

        Timer.builder("gateway.request.duration")
            .tags(tags)
            .description("Request processing duration")
            .register(meterRegistry)
            .record(Duration.ofMillis(durationMs));
    }

    public void recordError(String errorCode) {
        meterRegistry.counter("gateway.errors.total",
            List.of(Tag.of("error_code", errorCode))).increment();
    }

    public void recordActiveConnection(String route) {
        meterRegistry.gauge("gateway.connections.active",
            List.of(Tag.of("route", route)), 1);
    }

    public void recordServiceCall(String serviceName, long durationMs, boolean success) {
        var tags = List.of(
            Tag.of("service", serviceName),
            Tag.of("success", String.valueOf(success))
        );
        Timer.builder("gateway.service.call.duration")
            .tags(tags)
            .register(meterRegistry)
            .record(Duration.ofMillis(durationMs));
    }

    public void recordRateLimitHit(String route) {
        meterRegistry.counter("gateway.ratelimit.hits",
            List.of(Tag.of("route", route))).increment();
    }
}
