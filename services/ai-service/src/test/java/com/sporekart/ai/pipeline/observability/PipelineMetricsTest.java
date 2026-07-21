package com.sporekart.ai.pipeline.observability;

import com.sporekart.ai.pipeline.PipelineContext;
import com.sporekart.ai.pipeline.model.PipelineRequest;
import com.sporekart.ai.pipeline.model.PipelineResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PipelineMetricsTest {
    private PipelineMetrics metrics;
    private PipelineContext context;

    @BeforeEach
    void setUp() {
        metrics = new PipelineMetrics();
        var request = PipelineRequest.builder()
                .requestId("r").tenantId("t").userId("u").module("chat")
                .correlationId("c").context(Map.of("prompt", "Hi")).build();
        context = new PipelineContext(request);
    }

    @Test
    void shouldRecordStart() {
        metrics.recordPipelineStart(context);
        var snap = metrics.getSnapshot();
        assertEquals(1, snap.totalRequests());
    }

    @Test
    void shouldRecordSuccess() {
        metrics.recordPipelineStart(context);
        context.setResponse(PipelineResponse.success("resp", "req", "OPENAI", "gpt-4",
                "output", null, Duration.ofMillis(100)));
        metrics.recordPipelineCompletion(context);
        var snap = metrics.getSnapshot();
        assertEquals(1, snap.successfulRequests());
        assertEquals(1, snap.totalRequests());
        assertEquals(1.0, snap.successRate());
    }

    @Test
    void shouldRecordFailure() {
        metrics.recordPipelineStart(context);
        context.fail("error occurred");
        metrics.recordPipelineCompletion(context);
        var snap = metrics.getSnapshot();
        assertEquals(1, snap.failedRequests());
        assertEquals(0.0, snap.successRate());
    }

    @Test
    void shouldRecordTimeout() {
        metrics.recordTimeout();
        var snap = metrics.getSnapshot();
        assertEquals(1, snap.timeoutCount());
    }

    @Test
    void shouldTrackModuleCounts() {
        metrics.recordPipelineStart(context);
        metrics.recordPipelineStart(context);
        var snap = metrics.getSnapshot();
        assertEquals(2, snap.moduleCounts().get("chat"));
    }

    @Test
    void shouldTrackProviderCounts() {
        metrics.recordPipelineStart(context);
        context.setResponse(PipelineResponse.success("resp", "req", "GEMINI", "gemini-pro",
                "output", null, Duration.ofMillis(50)));
        metrics.recordPipelineCompletion(context);
        var snap = metrics.getSnapshot();
        assertEquals(1, snap.providerCounts().get("GEMINI"));
    }

    @Test
    void shouldReset() {
        metrics.recordPipelineStart(context);
        metrics.reset();
        var snap = metrics.getSnapshot();
        assertEquals(0, snap.totalRequests());
    }
}
