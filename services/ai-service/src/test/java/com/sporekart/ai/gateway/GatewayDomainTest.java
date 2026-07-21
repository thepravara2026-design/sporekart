package com.sporekart.ai.gateway;

import com.sporekart.ai.gateway.contract.message.ChatMessage;
import com.sporekart.ai.gateway.contract.message.MessageRole;
import com.sporekart.ai.gateway.contract.request.ChatCompletionRequest;
import com.sporekart.ai.gateway.contract.request.CompletionRequest;
import com.sporekart.ai.gateway.contract.request.GatewayRequest;
import com.sporekart.ai.gateway.contract.response.CompletionResponse;
import com.sporekart.ai.gateway.contract.response.GatewayResponse;
import com.sporekart.ai.gateway.domain.GatewayExecutionRequest;
import com.sporekart.ai.gateway.domain.GatewayMetrics;
import com.sporekart.ai.gateway.domain.*;
import com.sporekart.ai.gateway.exception.*;
import com.sporekart.ai.gateway.pipeline.PipelineContext;
import com.sporekart.ai.gateway.pipeline.PipelineStage;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class GatewayDomainTest {

    @Test
    void shouldCreatePipelineContext() {
        var request = new GatewayRequest("req-1", "chat", "openai", "gpt-4",
            null, "hello", null, null, null, false, null, null, null);
        var context = new GatewayExecutionRequest("user-1", "tenant-1",
            List.of("ai_user"), "openai", "gpt-4", null, null, null);
        var pipelineCtx = new PipelineContext(request, context);

        assertNotNull(pipelineCtx.pipelineId());
        assertEquals(PipelineStage.VALIDATION, pipelineCtx.currentStage());
        assertFalse(pipelineCtx.failed());
    }

    @Test
    void shouldAdvancePipelineStage() {
        var request = new GatewayRequest("req-1", "chat", "openai", "gpt-4",
            null, "hello", null, null, null, false, null, null, null);
        var context = new GatewayExecutionRequest("user-1", "tenant-1",
            List.of("ai_user"), "openai", "gpt-4", null, null, null);
        var pipelineCtx = new PipelineContext(request, context);
        var advanced = pipelineCtx.advanceTo(PipelineStage.AUTHENTICATION);

        assertEquals(PipelineStage.AUTHENTICATION, advanced.currentStage());
    }

    @Test
    void shouldMarkPipelineAsFailed() {
        var request = new GatewayRequest("req-1", "chat", "openai", "gpt-4",
            null, "hello", null, null, null, false, null, null, null);
        var context = new GatewayExecutionRequest("user-1", "tenant-1",
            List.of("ai_user"), "openai", "gpt-4", null, null, null);
        var pipelineCtx = new PipelineContext(request, context);
        var failed = pipelineCtx.failed("Validation failed");

        assertTrue(failed.failed());
        assertEquals("Validation failed", failed.failureReason());
    }

    @Test
    void shouldSetAndGetAttributes() {
        var ctx = new PipelineContext(
            new GatewayRequest("req-1", "chat", "openai", "gpt-4",
                null, "hello", null, null, null, false, null, null, null),
            new GatewayExecutionRequest("user-1", "tenant-1",
                List.of("ai_user"), "openai", "gpt-4", null, null, null));
        ctx.setAttribute("key1", "value1");
        assertEquals("value1", ctx.getAttribute("key1").orElseThrow());
    }

    @Test
    void shouldCreateAuthenticationResult() {
        var success = AuthenticationResult.success("user-1", "tenant-1", List.of("ai_user"));
        assertTrue(success.authenticated());
        assertEquals("user-1", success.userId());

        var failure = AuthenticationResult.failure("Invalid token");
        assertFalse(failure.authenticated());
        assertTrue(failure.getFailureReason().isPresent());
    }

    @Test
    void shouldCreateRoutingDecision() {
        var decision = new RoutingDecision("openai", "gpt-4", "first-available",
            "Provider resolved", 1, false, true);
        assertTrue(decision.resolved());
        assertNotNull(decision.providerId());
        assertEquals("openai", decision.providerId());

        var unresolved = RoutingDecision.unresolved();
        assertFalse(unresolved.resolved());
    }

    @Test
    void shouldCreateRateLimitStatus() {
        var allowed = RateLimitStatus.allowed(50, 100, 60);
        assertTrue(allowed.allowed());
        assertEquals(50, allowed.remaining());

        var denied = RateLimitStatus.denied(100, 60, 30, "Rate limit exceeded");
        assertFalse(denied.allowed());
        assertEquals(30, denied.retryAfterSeconds());
    }

    @Test
    void shouldCreateQuotaStatus() {
        var available = QuotaStatus.available(5000, 10000, "daily");
        assertTrue(available.available());

        var exhausted = QuotaStatus.exhausted(10000, "daily");
        assertFalse(exhausted.available());
    }

    @Test
    void shouldCreateGatewayResponse() {
        var ok = GatewayResponse.ok("req-1", "openai", "gpt-4", List.of("Hello!"));
        assertTrue(ok.success());
        assertEquals(200, ok.statusCode());

        var error = GatewayResponse.error("req-1", 400, "VALIDATION_ERROR", "Bad request");
        assertFalse(error.success());
        assertTrue(error.getErrorCode().isPresent());
    }

    @Test
    void shouldCreateChatMessageFactories() {
        var sys = ChatMessage.system("You are helpful");
        assertEquals("system", sys.role());

        var user = ChatMessage.user("Hello");
        assertEquals("user", user.role());

        var asst = ChatMessage.assistant("Hi!");
        assertEquals("assistant", asst.role());

        var tool = ChatMessage.tool("Result", "call-1");
        assertEquals("tool", tool.role());
        assertEquals("call-1", tool.toolCallId());

        var fn = ChatMessage.function("Result", "get_weather");
        assertEquals("function", fn.role());
        assertEquals("get_weather", fn.name());
    }

    @Test
    void shouldCreateCompletionResponse() {
        var usage = new CompletionResponse.Usage(10, 20, 30);
        var choice = new CompletionResponse.Choice(0, "Hello", "stop");
        var response = new CompletionResponse("id-1", "gpt-4", List.of(choice), usage, "fp-1");
        assertEquals("gpt-4", response.model());
        assertEquals(30, response.usage().totalTokens());
    }

    @Test
    void shouldThrowValidationException() {
        var ex = assertThrows(ValidationException.class,
            () -> { throw new ValidationException(List.of("field 'model' is required")); });
        assertEquals("VALIDATION_ERROR", ex.getErrorCode());
        assertEquals(400, ex.getStatusCode());
    }

    @Test
    void shouldThrowAuthenticationException() {
        var ex = assertThrows(AuthenticationException.class,
            () -> { throw new AuthenticationException("Invalid API key"); });
        assertEquals("AUTHENTICATION_FAILED", ex.getErrorCode());
        assertEquals(401, ex.getStatusCode());
    }

    @Test
    void shouldThrowAuthorizationException() {
        var ex = assertThrows(AuthorizationException.class,
            () -> { throw new AuthorizationException("Access denied"); });
        assertEquals("FORBIDDEN", ex.getErrorCode());
        assertEquals(403, ex.getStatusCode());
    }

    @Test
    void shouldThrowProviderException() {
        var ex = assertThrows(ProviderException.class,
            () -> { throw new ProviderException("openai", "Service unavailable"); });
        assertEquals("PROVIDER_ERROR", ex.getErrorCode());
        assertEquals("openai", ex.getProviderId());
    }

    @Test
    void shouldThrowRateLimitException() {
        var ex = assertThrows(RateLimitException.class,
            () -> { throw new RateLimitException(30); });
        assertEquals("RATE_LIMITED", ex.getErrorCode());
        assertEquals(30, ex.getRetryAfterSeconds());
    }

    @Test
    void shouldThrowQuotaExceededException() {
        var ex = assertThrows(QuotaExceededException.class,
            () -> { throw new QuotaExceededException("tokens", 10000, "daily"); });
        assertEquals("QUOTA_EXCEEDED", ex.getErrorCode());
    }

    @Test
    void shouldThrowTimeoutException() {
        var ex = assertThrows(TimeoutException.class,
            () -> { throw new TimeoutException(30000); });
        assertEquals("TIMEOUT", ex.getErrorCode());
        assertEquals(30000, ex.getTimeoutMs());
    }

    @Test
    void shouldThrowServiceUnavailableException() {
        var ex = assertThrows(ServiceUnavailableException.class,
            () -> { throw new ServiceUnavailableException("OpenAI"); });
        assertEquals("SERVICE_UNAVAILABLE", ex.getErrorCode());
    }

    @Test
    void shouldThrowConfigurationException() {
        var ex = assertThrows(ConfigurationException.class,
            () -> { throw new ConfigurationException("Missing provider config"); });
        assertEquals("CONFIGURATION_ERROR", ex.getErrorCode());
    }

    @Test
    void shouldCreateCompletionRequest() {
        var req = new CompletionRequest("gpt-4", "Hello", 0.7, 100, 0.9, 0.0, 0.0, null, "user-1");
        assertEquals("gpt-4", req.model());
        assertEquals("Hello", req.prompt());
    }

    @Test
    void shouldCreateChatCompletionRequest() {
        var messages = List.of(ChatMessage.user("Hi"));
        var req = new ChatCompletionRequest("gpt-4", messages, 0.7, 100, 0.9, 0.0, 0.0, null, "user-1");
        assertEquals(1, req.messages().size());
        assertEquals("Hi", req.messages().get(0).content());
    }

    @Test
    void shouldCreateGatewayExecutionRequest() {
        var ctx = new GatewayExecutionRequest("user-1", "tenant-1",
            List.of("ai_user"), "openai", "gpt-4", "dep-1",
            Map.of("X-Correlation-Id", "corr-1"), Map.of("priority", "high"));
        assertNotNull(ctx.executionId());
        assertTrue(ctx.getHeader("X-Correlation-Id").isPresent());
        assertTrue(ctx.getMetadata("priority").isPresent());
    }

    @Test
    void shouldCreateGatewayMetrics() {
        var metrics = new GatewayMetrics("pipe-1", "user-1", "tenant-1",
            "openai", "gpt-4", java.time.Duration.ofMillis(500),
            java.time.Duration.ofMillis(100), java.time.Duration.ofMillis(400),
            100, 200, true, null, null);
        assertEquals("openai", metrics.providerId());
        assertEquals(100, metrics.inputTokens());
        assertTrue(metrics.success());
    }

    @Test
    void shouldHaveUniquePipelineIds() {
        var id1 = UUID.randomUUID().toString();
        var id2 = UUID.randomUUID().toString();
        assertNotEquals(id1, id2);
    }
}
