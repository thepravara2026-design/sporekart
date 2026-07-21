package com.sporekart.ai.pipeline.timeout;

import com.sporekart.ai.pipeline.PipelineContext;
import com.sporekart.ai.pipeline.PipelineException;
import com.sporekart.ai.pipeline.model.PipelineRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TimeoutManagerTest {
    private TimeoutManager manager;

    @BeforeEach
    void setUp() {
        manager = new TimeoutManager(Duration.ofSeconds(5), Duration.ofSeconds(10));
    }

    @Test
    void shouldExecuteWithinTimeout() {
        var request = PipelineRequest.builder()
                .requestId("r").tenantId("t").userId("u").module("m")
                .correlationId("c").context(Map.of("prompt", "Hi")).build();
        var context = new PipelineContext(request);
        var result = manager.executeWithTimeout(context, () -> "success");
        assertEquals("success", result);
    }

    @Test
    void shouldThrowOnTimeout() {
        var request = PipelineRequest.builder()
                .requestId("r").tenantId("t").userId("u").module("m")
                .correlationId("c").context(Map.of("prompt", "Hi")).build();
        var context = new PipelineContext(request);
        assertThrows(PipelineException.class, () ->
                manager.executeWithTimeout(context, () -> {
                    Thread.sleep(100);
                    return "done";
                }));
    }

    @Test
    void shouldResolveProviderTimeout() {
        var request = PipelineRequest.builder()
                .requestId("r").tenantId("t").userId("u").module("m")
                .correlationId("c").context(Map.of("prompt", "Hi", "timeoutMs", 10000))
                .build();
        var context = new PipelineContext(request);
        var timeout = manager.resolveProviderTimeout(context);
        assertEquals(10000, timeout.toMillis());
    }

    @Test
    void shouldUseDefaultProviderTimeout() {
        var request = PipelineRequest.builder()
                .requestId("r").tenantId("t").userId("u").module("m")
                .correlationId("c").context(Map.of("prompt", "Hi"))
                .build();
        var context = new PipelineContext(request);
        var timeout = manager.resolveProviderTimeout(context);
        assertEquals(5000, timeout.toMillis());
    }

    @Test
    void shouldDetectExpired() {
        var request = PipelineRequest.builder()
                .requestId("r").tenantId("t").userId("u").module("m")
                .correlationId("c").context(Map.of("prompt", "Hi")).build();
        var context = new PipelineContext(request);
        assertFalse(manager.isExpired(context));
    }

    @Test
    void shouldScheduleCancellation() {
        var request = PipelineRequest.builder()
                .requestId("r").tenantId("t").userId("u").module("m")
                .correlationId("c").context(Map.of("prompt", "Hi")).build();
        var context = new PipelineContext(request);
        var cancelled = new boolean[]{false};
        manager.scheduleCancellation(context, () -> cancelled[0] = true);
        assertFalse(cancelled[0]);
        manager.shutdown();
    }
}
