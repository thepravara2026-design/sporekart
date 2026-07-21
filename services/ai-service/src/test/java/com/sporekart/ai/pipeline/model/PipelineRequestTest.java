package com.sporekart.ai.pipeline.model;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PipelineRequestTest {
    @Test
    void shouldCreateValidRequest() {
        var request = PipelineRequest.builder()
                .requestId("req-1")
                .tenantId("tenant-1")
                .userId("user-1")
                .module("chat")
                .correlationId("corr-1")
                .context(Map.of("prompt", "Hello"))
                .build();
        assertNotNull(request);
        assertEquals("req-1", request.requestId());
    }

    @Test
    void shouldThrowOnBlankRequestId() {
        assertThrows(IllegalArgumentException.class, () ->
                PipelineRequest.builder()
                        .requestId("")
                        .tenantId("t")
                        .userId("u")
                        .module("m")
                        .correlationId("c")
                        .build());
    }

    @Test
    void shouldThrowOnBlankTenantId() {
        assertThrows(IllegalArgumentException.class, () ->
                PipelineRequest.builder()
                        .requestId("r")
                        .tenantId("")
                        .userId("u")
                        .module("m")
                        .correlationId("c")
                        .build());
    }

    @Test
    void shouldUseDefaults() {
        var request = PipelineRequest.builder()
                .requestId("r")
                .tenantId("t")
                .userId("u")
                .module("m")
                .correlationId("c")
                .context(Map.of("prompt", "Hi"))
                .build();
        assertEquals(0.7, request.temperature());
        assertEquals(2048, request.maxTokens());
        assertEquals(1.0, request.topP());
        assertFalse(request.stream());
        assertEquals(RequestSource.API, request.source());
        assertNotNull(request.timestamp());
    }

    @Test
    void shouldPreserveExplicitValues() {
        var request = PipelineRequest.builder()
                .requestId("r").tenantId("t").userId("u").module("m")
                .correlationId("c").context(Map.of("prompt", "Hi"))
                .temperature(1.5).maxTokens(4096).topP(0.8).stream(true)
                .source(RequestSource.COPILOT)
                .build();
        assertEquals(1.5, request.temperature());
        assertEquals(4096, request.maxTokens());
        assertEquals(0.8, request.topP());
        assertTrue(request.stream());
        assertEquals(RequestSource.COPILOT, request.source());
    }

    @Test
    void shouldMakeContextImmutable() {
        var mutableCtx = new java.util.HashMap<String, Object>();
        mutableCtx.put("prompt", "Hello");
        var request = PipelineRequest.builder()
                .requestId("r").tenantId("t").userId("u").module("m")
                .correlationId("c").context(mutableCtx)
                .build();
        mutableCtx.put("extra", "value");
        assertFalse(request.context().containsKey("extra"));
    }

    @Test
    void shouldHandleNullContext() {
        var request = PipelineRequest.builder()
                .requestId("r").tenantId("t").userId("u").module("m")
                .correlationId("c")
                .build();
        assertTrue(request.context().isEmpty());
    }
}
