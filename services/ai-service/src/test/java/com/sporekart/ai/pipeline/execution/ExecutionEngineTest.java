package com.sporekart.ai.pipeline.execution;

import com.sporekart.ai.pipeline.PipelineContext;
import com.sporekart.ai.pipeline.model.PipelineRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ExecutionEngineTest {
    private ExecutionEngine engine;

    @BeforeEach
    void setUp() {
        engine = new ExecutionEngine();
    }

    @Test
    void shouldExecuteWithValidContext() {
        var context = createValidContext();
        engine.execute(context);
        assertNotNull(context.response());
        assertTrue(context.response().success());
        assertEquals("OPENAI", context.response().provider());
    }

    @Test
    void shouldFailOnNullProvider() {
        var request = PipelineRequest.builder()
                .requestId("r").tenantId("t").userId("u").module("m")
                .correlationId("c").context(Map.of("prompt", "Hi"))
                .build();
        var context = new PipelineContext(request);
        engine.execute(context);
        assertTrue(context.failed());
        assertTrue(context.failureReason().contains("No provider"));
    }

    @Test
    void shouldTrackLatency() {
        var context = createValidContext();
        engine.execute(context);
        var latency = context.<Long>getAttribute("executionLatencyMs");
        assertNotNull(latency);
        assertTrue(latency >= 0);
    }

    @Test
    void shouldIncludeTokenUsage() {
        var context = createValidContext();
        engine.execute(context);
        var response = context.response();
        assertNotNull(response.tokenUsage());
        assertTrue(response.tokenUsage().totalTokens() > 0);
    }

    @Test
    void shouldCalculateCost() {
        var context = createValidContext();
        engine.execute(context);
        assertTrue(context.response().cost() >= 0);
    }

    private PipelineContext createValidContext() {
        var request = PipelineRequest.builder()
                .requestId("r").tenantId("t").userId("u").module("chat")
                .correlationId("c").context(Map.of("prompt", "Hello, world!"))
                .build();
        var context = new PipelineContext(request);
        context.selectProvider("OPENAI", "gpt-4");
        context.setAttribute("compiledPrompt", "Hello, world!");
        return context;
    }
}
