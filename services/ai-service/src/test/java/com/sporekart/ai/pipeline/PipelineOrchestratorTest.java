package com.sporekart.ai.pipeline;

import com.sporekart.ai.pipeline.model.PipelineRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PipelineOrchestratorTest {
    private PipelineOrchestrator orchestrator;

    @BeforeEach
    void setUp() {
        orchestrator = new PipelineOrchestrator();
    }

    @Test
    void shouldExecuteCompletePipelineSuccessfully() {
        var request = PipelineRequest.builder()
                .requestId("req-1").tenantId("t-1").userId("u-1").module("chat")
                .correlationId("corr-1").context(Map.of("prompt", "Hello, world!"))
                .build();
        var result = orchestrator.execute(request);
        assertTrue(result.success());
        assertNotNull(result.response());
        assertTrue(result.response().success());
        assertNotNull(result.response().generatedOutput());
    }

    @Test
    void shouldRejectInvalidRequest() {
        var request = PipelineRequest.builder()
                .requestId("").tenantId("").userId("").module("")
                .correlationId("").context(Map.of())
                .build();
        var result = orchestrator.execute(request);
        assertFalse(result.success());
        assertTrue(result.errors().stream().anyMatch(e -> e.contains("validation")));
    }

    @Test
    void shouldSelectProvider() {
        var request = PipelineRequest.builder()
                .requestId("r").tenantId("t").userId("u").module("chat")
                .correlationId("c").context(Map.of("prompt", "Hi"))
                .providerPreference("GEMINI")
                .build();
        var result = orchestrator.execute(request);
        assertTrue(result.success());
        assertEquals("GEMINI", result.selectedProvider());
    }

    @Test
    void shouldIncludeTokenUsage() {
        var request = PipelineRequest.builder()
                .requestId("r").tenantId("t").userId("u").module("chat")
                .correlationId("c").context(Map.of("prompt", "Hi"))
                .build();
        var result = orchestrator.execute(request);
        assertTrue(result.success());
        assertNotNull(result.response().tokenUsage());
    }

    @Test
    void shouldTrackWarnings() {
        var request = PipelineRequest.builder()
                .requestId("r").tenantId("t").userId("u").module("chat")
                .correlationId("c").context(Map.of("prompt", "Hi"))
                .providerPreference("UNKNOWN")
                .build();
        var result = orchestrator.execute(request);
        assertTrue(result.success());
    }

    @Test
    void shouldSetPipelineId() {
        var request = PipelineRequest.builder()
                .requestId("r").tenantId("t").userId("u").module("chat")
                .correlationId("c").context(Map.of("prompt", "Hi"))
                .build();
        var result = orchestrator.execute(request);
        assertNotNull(result.pipelineId());
    }

    @Test
    void shouldCreateWithBuilder() {
        var custom = PipelineOrchestrator.builder().build();
        assertNotNull(custom);
    }

    @Test
    void shouldSupportDifferentModules() {
        for (var module : new String[]{"chat", "search", "content", "admin"}) {
            var request = PipelineRequest.builder()
                    .requestId("r").tenantId("t").userId("u").module(module)
                    .correlationId("c").context(Map.of("prompt", "Hi"))
                    .build();
            var result = orchestrator.execute(request);
            assertTrue(result.success(), "Module " + module + " should succeed");
        }
    }

    @Test
    void shouldExposeMetrics() {
        assertNotNull(orchestrator.getMetrics());
        orchestrator.execute(PipelineRequest.builder()
                .requestId("r").tenantId("t").userId("u").module("chat")
                .correlationId("c").context(Map.of("prompt", "Hi")).build());
        var snap = orchestrator.getMetrics().getSnapshot();
        assertTrue(snap.totalRequests() >= 1);
    }

    @Test
    void shouldExposeAudit() {
        assertNotNull(orchestrator.getAudit());
    }

    @Test
    void shouldExposeMiddlewareChain() {
        assertNotNull(orchestrator.getMiddlewareChain());
        assertFalse(orchestrator.getMiddlewareChain().getMiddlewares().isEmpty());
    }
}
