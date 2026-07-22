package com.sporekart.gateway.observability;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

class MetricsRecorderTest {

    private MeterRegistry meterRegistry;
    private MetricsRecorder recorder;

    @BeforeEach
    void setUp() {
        meterRegistry = new SimpleMeterRegistry();
        recorder = new MetricsRecorder(meterRegistry);
    }

    @Test
    void shouldRecordRequest() {
        recorder.recordRequest("GET", "/api/test", "test-service", HttpStatus.OK, 100);

        var counter = meterRegistry.counter("gateway.requests.total",
            "method", "GET", "path", "/api/test", "route", "test-service", "status", "200", "status_group", "2xx");
        assertThat(counter.count()).isEqualTo(1);
    }

    @Test
    void shouldRecordError() {
        recorder.recordError("NOT_FOUND");
        assertThat(meterRegistry.counter("gateway.errors.total", "error_code", "NOT_FOUND").count()).isEqualTo(1);
    }

    @Test
    void shouldRecordServiceCall() {
        recorder.recordServiceCall("identity", 50, true);
        var timer = meterRegistry.timer("gateway.service.call.duration", "service", "identity", "success", "true");
        assertThat(timer.count()).isEqualTo(1);
    }

    @Test
    void shouldRecordRateLimitHit() {
        recorder.recordRateLimitHit("/api/test");
        assertThat(meterRegistry.counter("gateway.ratelimit.hits", "route", "/api/test").count()).isEqualTo(1);
    }

    @Test
    void shouldRecordClientError() {
        recorder.recordRequest("POST", "/api/data", "data-service", HttpStatus.BAD_REQUEST, 5);
        assertThat(meterRegistry.counter("gateway.requests.total",
            "method", "POST", "path", "/api/data", "route", "data-service", "status", "400", "status_group", "4xx")
            .count()).isEqualTo(1);
    }

    @Test
    void shouldRecordServerError() {
        recorder.recordRequest("GET", "/api/error", "svc", HttpStatus.INTERNAL_SERVER_ERROR, 200);
        assertThat(meterRegistry.counter("gateway.requests.total",
            "method", "GET", "path", "/api/error", "route", "svc", "status", "500", "status_group", "5xx")
            .count()).isEqualTo(1);
    }
}
