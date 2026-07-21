package com.sporekart.ai.pipeline;

import com.sporekart.ai.pipeline.model.PipelineRequest;
import com.sporekart.ai.pipeline.model.PipelineResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PipelineContextTest {
    private PipelineContext context;

    @BeforeEach
    void setUp() {
        var request = PipelineRequest.builder()
                .requestId("req-1").tenantId("t").userId("u")
                .module("m").correlationId("c")
                .context(Map.of("prompt", "Hello"))
                .build();
        context = new PipelineContext(request);
    }

    @Test
    void shouldHavePipelineId() {
        assertNotNull(context.pipelineId());
    }

    @Test
    void shouldStoreRequest() {
        assertEquals("req-1", context.request().requestId());
    }

    @Test
    void shouldSetAndGetAttributes() {
        context.setAttribute("key1", "value1");
        assertEquals("value1", context.getAttribute("key1"));
    }

    @Test
    void shouldGetAttributeWithDefault() {
        assertEquals("default", context.getAttribute("nonexistent", "default"));
    }

    @Test
    void shouldCheckAttributeExistence() {
        context.setAttribute("exists", true);
        assertTrue(context.hasAttribute("exists"));
        assertFalse(context.hasAttribute("missing"));
    }

    @Test
    void shouldFail() {
        context.fail("Something went wrong");
        assertTrue(context.failed());
        assertEquals("Something went wrong", context.failureReason());
        assertNotNull(context.completedAt());
    }

    @Test
    void shouldSetResponse() {
        var response = PipelineResponse.success("r", "req", "P", "M", "out",
                null, Duration.ZERO);
        context.setResponse(response);
        assertNotNull(context.response());
        assertNotNull(context.completedAt());
    }

    @Test
    void shouldIncrementRetry() {
        assertEquals(0, context.retryCount());
        context.incrementRetry();
        assertEquals(1, context.retryCount());
    }

    @Test
    void shouldSelectProvider() {
        context.selectProvider("OPENAI", "gpt-4");
        assertEquals("OPENAI", context.selectedProvider());
        assertEquals("gpt-4", context.selectedModel());
    }

    @Test
    void shouldRecordMiddleware() {
        context.recordMiddleware("Validation");
        assertTrue(context.middlewareExecuted().contains("Validation"));
    }

    @Test
    void shouldStoreCompiledPrompt() {
        context.setCompiledPrompt("compiled prompt");
        assertEquals("compiled prompt", context.compiledPrompt());
    }

    @Test
    void shouldCalculateElapsed() {
        var elapsed = context.elapsed();
        assertTrue(elapsed.toMillis() >= 0);
    }

    @Test
    void shouldReturnImmutableAttributes() {
        context.setAttribute("k", "v");
        var attrs = context.attributes();
        assertThrows(UnsupportedOperationException.class, () -> attrs.put("x", "y"));
    }
}
