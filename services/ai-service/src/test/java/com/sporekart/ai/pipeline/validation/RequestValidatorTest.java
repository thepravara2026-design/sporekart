package com.sporekart.ai.pipeline.validation;

import com.sporekart.ai.pipeline.model.PipelineRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class RequestValidatorTest {
    private RequestValidator validator;

    @BeforeEach
    void setUp() {
        validator = new RequestValidator();
    }

    @Test
    void shouldAcceptValidRequest() {
        var request = createValidRequest();
        var result = validator.validate(request);
        assertTrue(result.valid());
    }

    @Test
    void shouldRejectMissingRequestId() {
        var request = createValidRequest();
        request = PipelineRequest.builder().requestId("").tenantId(request.tenantId())
                .userId(request.userId()).module(request.module())
                .correlationId(request.correlationId()).context(request.context()).build();
        var result = validator.validate(request);
        assertFalse(result.valid());
    }

    @Test
    void shouldRejectMissingTenantId() {
        var request = createValidRequest();
        request = PipelineRequest.builder()
                .requestId(request.requestId()).tenantId("")
                .userId(request.userId()).module(request.module())
                .correlationId(request.correlationId()).context(request.context()).build();
        var result = validator.validate(request);
        assertFalse(result.valid());
    }

    @Test
    void shouldRejectMissingUserId() {
        var request = PipelineRequest.builder()
                .requestId("r").tenantId("t").userId("").module("m")
                .correlationId("c").context(Map.of("prompt", "Hi")).build();
        var result = validator.validate(request);
        assertFalse(result.valid());
    }

    @Test
    void shouldRejectMissingModule() {
        var request = PipelineRequest.builder()
                .requestId("r").tenantId("t").userId("u").module("")
                .correlationId("c").context(Map.of("prompt", "Hi")).build();
        var result = validator.validate(request);
        assertFalse(result.valid());
    }

    @Test
    void shouldRejectMissingCorrelationId() {
        var request = PipelineRequest.builder()
                .requestId("r").tenantId("t").userId("u").module("m")
                .correlationId("").context(Map.of("prompt", "Hi")).build();
        var result = validator.validate(request);
        assertFalse(result.valid());
    }

    @Test
    void shouldRejectMissingPrompt() {
        var request = PipelineRequest.builder()
                .requestId("r").tenantId("t").userId("u").module("m")
                .correlationId("c").context(Map.of()).build();
        var result = validator.validate(request);
        assertFalse(result.valid());
    }

    @Test
    void shouldRejectEmptyPrompt() {
        var request = PipelineRequest.builder()
                .requestId("r").tenantId("t").userId("u").module("m")
                .correlationId("c").context(Map.of("prompt", "")).build();
        var result = validator.validate(request);
        assertFalse(result.valid());
    }

    @Test
    void shouldAcceptPromptInInputField() {
        var request = PipelineRequest.builder()
                .requestId("r").tenantId("t").userId("u").module("m")
                .correlationId("c").context(Map.of("input", "Hello")).build();
        var result = validator.validate(request);
        assertTrue(result.valid());
    }

    @Test
    void shouldAcceptPromptInQueryField() {
        var request = PipelineRequest.builder()
                .requestId("r").tenantId("t").userId("u").module("m")
                .correlationId("c").context(Map.of("query", "Hello")).build();
        var result = validator.validate(request);
        assertTrue(result.valid());
    }

    @Test
    void shouldRejectNegativeMaxTokens() {
        var request = PipelineRequest.builder()
                .requestId("r").tenantId("t").userId("u").module("m")
                .correlationId("c").context(Map.of("prompt", "Hi"))
                .maxTokens(-1).build();
        var result = validator.validate(request);
        assertFalse(result.valid());
    }

    @Test
    void shouldRejectExcessiveMaxTokens() {
        var request = PipelineRequest.builder()
                .requestId("r").tenantId("t").userId("u").module("m")
                .correlationId("c").context(Map.of("prompt", "Hi"))
                .maxTokens(200000).build();
        var result = validator.validate(request);
        assertFalse(result.valid());
    }

    @Test
    void shouldRejectExcessiveTemperature() {
        var request = PipelineRequest.builder()
                .requestId("r").tenantId("t").userId("u").module("m")
                .correlationId("c").context(Map.of("prompt", "Hi"))
                .temperature(3.0).build();
        var result = validator.validate(request);
        assertFalse(result.valid());
    }

    @Test
    void shouldRejectInvalidTopP() {
        var request = PipelineRequest.builder()
                .requestId("r").tenantId("t").userId("u").module("m")
                .correlationId("c").context(Map.of("prompt", "Hi"))
                .topP(1.5).build();
        var result = validator.validate(request);
        assertFalse(result.valid());
    }

    @Test
    void shouldWarnOnUnknownProvider() {
        var request = PipelineRequest.builder()
                .requestId("r").tenantId("t").userId("u").module("m")
                .correlationId("c").context(Map.of("prompt", "Hi"))
                .providerPreference("UNKNOWN_PROVIDER").build();
        var result = validator.validate(request);
        assertTrue(result.valid());
        assertFalse(result.warnings().isEmpty());
    }

    @Test
    void shouldRejectOversizedContext() {
        var largeContext = new java.util.HashMap<String, Object>();
        largeContext.put("prompt", "H");
        largeContext.put("data", "X".repeat(60000));
        var request = PipelineRequest.builder()
                .requestId("r").tenantId("t").userId("u").module("m")
                .correlationId("c").context(largeContext).build();
        var result = validator.validate(request);
        assertFalse(result.valid());
    }

    private PipelineRequest createValidRequest() {
        return PipelineRequest.builder()
                .requestId("req-1")
                .tenantId("tenant-1")
                .userId("user-1")
                .module("chat")
                .correlationId("corr-1")
                .context(Map.of("prompt", "Hello, world!"))
                .build();
    }
}
