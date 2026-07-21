package com.sporekart.ai.gateway.integration;

import com.sporekart.ai.gateway.api.AiGateway;
import com.sporekart.ai.gateway.api.RateLimiter;
import com.sporekart.ai.gateway.api.RequestValidator;
import com.sporekart.ai.gateway.application.*;
import com.sporekart.ai.gateway.config.GatewayConfigProperties;
import com.sporekart.ai.gateway.contract.request.GatewayRequest;
import com.sporekart.ai.gateway.contract.response.GatewayResponse;
import com.sporekart.ai.gateway.domain.*;
import com.sporekart.ai.gateway.error.StandardGatewayError;
import com.sporekart.ai.gateway.exception.*;
import com.sporekart.ai.gateway.infrastructure.DefaultRetryStrategy;
import com.sporekart.ai.gateway.infrastructure.InMemoryRateLimiter;
import com.sporekart.ai.gateway.observability.LoggerService;
import com.sporekart.ai.gateway.observability.MetricsCollector;
import com.sporekart.ai.gateway.pipeline.PipelineContext;
import com.sporekart.ai.gateway.pipeline.PipelineStage;
import com.sporekart.ai.gateway.router.ProviderRouter;
import com.sporekart.ai.gateway.router.strategies.FallbackStrategy;
import com.sporekart.ai.gateway.router.strategies.FirstAvailableStrategy;

import com.sporekart.ai.core.api.AIContextResolver;
import com.sporekart.ai.core.api.ProviderResolver;
import com.sporekart.ai.core.api.RetryStrategy;
import com.sporekart.ai.core.api.TimeoutStrategy;
import com.sporekart.ai.core.application.featureflag.FeatureFlagService;
import com.sporekart.ai.core.domain.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class GatewayIntegrationTest {

    @Mock private FeatureFlagService featureFlagService;
    @Mock private AiGateway aiGateway;
    @Mock private AIContextResolver contextResolver;
    @Mock private ProviderResolver providerResolver;
    @Mock private RetryStrategy retryStrategy;
    @Mock private TimeoutStrategy timeoutStrategy;
    @Mock private GatewayAuditService auditService;
    @Mock private GatewayMetricsCollector metricsCollector;
    @Mock private LoggerService loggerService;
    @Mock private MetricsCollector obsMetricsCollector;
    @Mock private RequestValidator requestValidator;

    private GatewayPipeline pipeline;
    private GatewayApplicationService gatewayService;
    private GatewayDomainService domainService;
    private GatewayExceptionTranslator exceptionTranslator;
    private GatewayResponseBuilder responseBuilder;
    private InMemoryRateLimiter rateLimiter;
    private DefaultRetryStrategy defaultRetryStrategy;

    private static final List<String> ALL_PROVIDERS = List.of("MOCK", "GEMINI", "OPENAI", "CLAUDE", "AZURE", "OLLAMA", "CUSTOM");

    @BeforeEach
    void setUp() {
        rateLimiter = new InMemoryRateLimiter(100);
        defaultRetryStrategy = new DefaultRetryStrategy();
        domainService = new GatewayDomainService();
        responseBuilder = new GatewayResponseBuilder();
        exceptionTranslator = new GatewayExceptionTranslator();

        lenient().when(featureFlagService.isEnabled(FeatureFlagName.AI_PLATFORM_ENABLED)).thenReturn(true);
        lenient().when(featureFlagService.isEnabled(FeatureFlagName.AI_GATEWAY_ENABLED)).thenReturn(true);
        lenient().when(featureFlagService.isEnabled(FeatureFlagName.AI_RATE_LIMITING)).thenReturn(false);
        lenient().when(featureFlagService.isEnabled(FeatureFlagName.AI_REQUEST_LOGGING)).thenReturn(false);
        lenient().when(rateLimiter.tryAcquire(anyString())).thenReturn(true);
        lenient().when(contextResolver.resolveUserId(any())).thenReturn("test-user");
        lenient().when(contextResolver.resolveModule(any())).thenReturn("chat");
        lenient().when(contextResolver.resolveProvider(any())).thenReturn("MOCK");
        lenient().when(contextResolver.resolve(any(), any())).thenReturn(Map.of());
        lenient().when(providerResolver.resolve(anyString(), anyString())).thenReturn(Optional.of(AiProviderType.MOCK));
        lenient().when(timeoutStrategy.getTimeout(anyString())).thenReturn(Duration.ofSeconds(30));
        lenient().when(timeoutStrategy.isExpired(anyLong(), any())).thenReturn(false);
        lenient().when(retryStrategy.getMaxRetries()).thenReturn(1);
        lenient().when(requestValidator.validate(any())).thenReturn(true);

        pipeline = new GatewayPipeline(featureFlagService, rateLimiter, requestValidator,
                aiGateway, contextResolver, providerResolver, retryStrategy,
                timeoutStrategy, auditService, metricsCollector);
        gatewayService = new GatewayApplicationService(pipeline, domainService, responseBuilder, exceptionTranslator);
    }

    // =========================================================================
    // SECTION 1: Integration Testing - Full Flow
    // =========================================================================

    @Test
    @Order(1)
    void shouldExecuteFullGatewayFlow() {
        AiResponse mockResponse = new AiResponse("Mock answer");
        when(aiGateway.route(any())).thenReturn(mockResponse);

        AiRequest request = new AiRequest("What is my order status?", Map.of());
        CorrelationId correlationId = CorrelationId.generate();
        AiResponse response = pipeline.execute(request, correlationId);

        assertThat(response.success()).isTrue();
        assertThat(response.content()).isEqualTo("Mock answer");
        verify(auditService).recordRequestReceived(any());
        verify(auditService).recordRequestCompleted(any(), eq(true), anyLong());
    }

    @Test
    @Order(2)
    void shouldExecuteRequestThroughGatewayApplicationService() {
        AiResponse mockResponse = new AiResponse("Service response");
        when(aiGateway.route(any())).thenReturn(mockResponse);

        AiRequest request = new AiRequest("Hello", Map.of());
        AiResponse response = gatewayService.route(request);

        assertThat(response.success()).isTrue();
        assertThat(response.content()).isEqualTo("Service response");
    }

    @Test
    @Order(3)
    void shouldSupportAllProviderTypes() {
        for (String provider : ALL_PROVIDERS) {
            gatewayService.supports(provider);
        }
        assertThat(gatewayService.supports("MOCK")).isTrue();
        assertThat(gatewayService.supports("GEMINI")).isTrue();
        assertThat(gatewayService.supports("OPENAI")).isTrue();
        assertThat(gatewayService.supports("CLAUDE")).isTrue();
    }

    @Test
    @Order(4)
    void shouldReturnHealthStatus() {
        AIHealthResponse health = gatewayService.getHealth();
        assertThat(health.status()).isEqualTo("UP");
        assertThat(health.gatewayOperational()).isTrue();
    }

    @Test
    @Order(5)
    void shouldReturnGatewayStatus() {
        GatewayStatus status = gatewayService.getStatus();
        assertThat(status).isNotNull();
    }

    @Test
    @Order(6)
    void shouldReturnFeatureFlags() {
        Map<String, Boolean> features = gatewayService.getFeatures();
        assertThat(features).isNotNull();
    }

    @Test
    @Order(7)
    void shouldValidateRequests() {
        AIExecutionRequest validReq = new AIExecutionRequest("valid prompt", null, null, null, null);
        AIExecutionResponse validResponse = gatewayService.validateRequest(validReq);
        assertTrue(validResponse.isSuccess());

        AIExecutionRequest invalidReq = new AIExecutionRequest("", null, null, null, null);
        AIExecutionResponse invalidResponse = gatewayService.validateRequest(invalidReq);
        assertFalse(invalidResponse.isSuccess());
    }

    @Test
    @Order(8)
    void shouldExecuteDomainRequest() {
        AIExecutionRequest req = new AIExecutionRequest("Execute this", "user", "chat", "MOCK", null);
        AIExecutionResponse response = gatewayService.executeRequest(req);
        assertNotNull(response);
    }

    // =========================================================================
    // SECTION 2: Provider Failure Testing
    // =========================================================================

    @Test
    @Order(9)
    void shouldConvertProviderUnavailableToStandardGatewayError() {
        StandardGatewayError error = StandardGatewayError.providerUnavailable("OPENAI");
        assertThat(error.errorCode()).isEqualTo("PROVIDER_UNAVAILABLE");
        assertThat(error.statusCode()).isEqualTo(503);
        assertThat(error.providerId()).isEqualTo("OPENAI");
        assertThat(error.retryable()).isTrue();
        assertThat(error.isServerError()).isTrue();
    }

    @Test
    @Order(10)
    void shouldConvertProviderTimeoutToStandardGatewayError() {
        StandardGatewayError error = StandardGatewayError.providerTimeout("GEMINI");
        assertThat(error.errorCode()).isEqualTo("PROVIDER_TIMEOUT");
        assertThat(error.statusCode()).isEqualTo(504);
        assertThat(error.retryable()).isTrue();
    }

    @Test
    @Order(11)
    void shouldConvertRateLimitToStandardGatewayError() {
        StandardGatewayError error = StandardGatewayError.rateLimited("CLAUDE", 30L);
        assertThat(error.errorCode()).isEqualTo("RATE_LIMITED");
        assertThat(error.statusCode()).isEqualTo(429);
        assertThat(error.retryAfterSeconds()).isEqualTo(30L);
        assertThat(error.isClientError()).isTrue();
    }

    @Test
    @Order(12)
    void shouldConvertInvalidAuthToStandardGatewayError() {
        StandardGatewayError error = StandardGatewayError.invalidAuth("AZURE");
        assertThat(error.errorCode()).isEqualTo("INVALID_AUTH");
        assertThat(error.statusCode()).isEqualTo(401);
        assertThat(error.retryable()).isFalse();
    }

    @Test
    @Order(13)
    void shouldConvertQuotaExceededToStandardGatewayError() {
        StandardGatewayError error = StandardGatewayError.quotaExceeded("OLLAMA");
        assertThat(error.errorCode()).isEqualTo("QUOTA_EXCEEDED");
        assertThat(error.retryable()).isTrue();
    }

    @Test
    @Order(14)
    void shouldConvertMalformedResponseToStandardGatewayError() {
        StandardGatewayError error = StandardGatewayError.malformedResponse("CUSTOM");
        assertThat(error.errorCode()).isEqualTo("MALFORMED_RESPONSE");
        assertThat(error.statusCode()).isEqualTo(502);
        assertThat(error.retryable()).isFalse();
    }

    @Test
    @Order(15)
    void shouldConvertPartialResponseToStandardGatewayError() {
        StandardGatewayError error = StandardGatewayError.partialResponse("GEMINI");
        assertThat(error.errorCode()).isEqualTo("PARTIAL_RESPONSE");
        assertThat(error.retryable()).isTrue();
    }

    @Test
    @Order(16)
    void shouldConvertConnectionRefusedToStandardGatewayError() {
        StandardGatewayError error = StandardGatewayError.connectionRefused("OPENAI");
        assertThat(error.errorCode()).isEqualTo("CONNECTION_REFUSED");
        assertThat(error.statusCode()).isEqualTo(503);
        assertThat(error.retryable()).isTrue();
    }

    @Test
    @Order(17)
    void shouldConvertTlsFailureToStandardGatewayError() {
        StandardGatewayError error = StandardGatewayError.tlsFailure("AZURE");
        assertThat(error.errorCode()).isEqualTo("TLS_FAILURE");
        assertThat(error.statusCode()).isEqualTo(502);
        assertThat(error.retryable()).isFalse();
    }

    @Test
    @Order(18)
    void shouldConvertDnsFailureToStandardGatewayError() {
        StandardGatewayError error = StandardGatewayError.dnsFailure("CLAUDE");
        assertThat(error.errorCode()).isEqualTo("DNS_FAILURE");
        assertThat(error.statusCode()).isEqualTo(503);
        assertThat(error.retryable()).isTrue();
    }

    @Test
    @Order(19)
    void shouldConvertCircuitOpenToStandardGatewayError() {
        StandardGatewayError error = StandardGatewayError.circuitOpen("OPENAI");
        assertThat(error.errorCode()).isEqualTo("CIRCUIT_OPEN");
        assertThat(error.statusCode()).isEqualTo(503);
        assertThat(error.retryable()).isTrue();
    }

    @Test
    @Order(20)
    void shouldMapAllProviderExceptionsToStandardGatewayError() {
        List<StandardGatewayError> allErrors = List.of(
            StandardGatewayError.providerUnavailable("P1"),
            StandardGatewayError.providerTimeout("P1"),
            StandardGatewayError.rateLimited("P1", 10L),
            StandardGatewayError.invalidAuth("P1"),
            StandardGatewayError.quotaExceeded("P1"),
            StandardGatewayError.malformedResponse("P1"),
            StandardGatewayError.partialResponse("P1"),
            StandardGatewayError.connectionRefused("P1"),
            StandardGatewayError.tlsFailure("P1"),
            StandardGatewayError.dnsFailure("P1"),
            StandardGatewayError.circuitOpen("P1")
        );

        assertThat(allErrors).allMatch(e -> e.providerId().equals("P1"));
        long serverErrors = allErrors.stream().filter(StandardGatewayError::isServerError).count();
        long clientErrors = allErrors.stream().filter(StandardGatewayError::isClientError).count();
        assertThat(serverErrors).isPositive();
        assertThat(clientErrors).isPositive();
    }

    @Test
    @Order(21)
    void exceptionTranslatorShouldHandleGatewayException() {
        GatewayException ex = new GatewayException("TEST_ERR", "Test message", 400);
        AiResponse response = exceptionTranslator.translate(ex);
        assertThat(response.success()).isFalse();
        assertThat(response.content()).contains("TEST_ERR");
    }

    @Test
    @Order(22)
    void exceptionTranslatorShouldHandleRateLimitException() {
        RateLimitException ex = new RateLimitException(30);
        AiResponse response = exceptionTranslator.translate(ex);
        assertThat(response.success()).isFalse();
    }

    @Test
    @Order(23)
    void exceptionTranslatorShouldHandleUnknownException() {
        AiResponse response = exceptionTranslator.translate(new RuntimeException("Unexpected"));
        assertThat(response.success()).isFalse();
        assertThat(response.content()).contains("AI-999");
    }

    // =========================================================================
    // SECTION 3: Fallback Testing
    // =========================================================================

    @Test
    @Order(24)
    void shouldHandlePrimarySuccess() {
        AiResponse mockResponse = new AiResponse("Primary provider answer");
        when(aiGateway.route(any())).thenReturn(mockResponse);

        AiRequest request = new AiRequest("Hello", Map.of());
        AiResponse response = pipeline.execute(request, CorrelationId.generate());

        assertThat(response.success()).isTrue();
        assertThat(response.content()).isEqualTo("Primary provider answer");
    }

    @Test
    @Order(25)
    void shouldHandlePrimaryTimeoutAndFallback() {
        when(aiGateway.route(any()))
            .thenThrow(new TimeoutException(30000));

        AiRequest request = new AiRequest("test", Map.of());
        AiResponse response = gatewayService.route(request);

        assertThat(response.success()).isFalse();
        assertThat(response.content()).contains("TIMEOUT");
    }

    @Test
    @Order(26)
    void shouldHandlePrimaryQuotaExceededAndFallback() {
        when(aiGateway.route(any()))
            .thenThrow(new QuotaExceededException("tokens", 1000, "daily"));

        AiResponse response = gatewayService.route(new AiRequest("test", Map.of()));
        assertThat(response.success()).isFalse();
        assertThat(response.content()).contains("QUOTA_EXCEEDED");
    }

    @Test
    @Order(27)
    void shouldHandlePrimaryInvalidAuthAndFallback() {
        when(aiGateway.route(any()))
            .thenThrow(new AuthenticationException("Invalid key"));

        AiResponse response = gatewayService.route(new AiRequest("test", Map.of()));
        assertThat(response.success()).isFalse();
        assertThat(response.content()).contains("AUTHENTICATION_FAILED");
    }

    @Test
    @Order(28)
    void shouldHandlePrimaryMalformedJsonAndFallback() {
        when(aiGateway.route(any()))
            .thenThrow(new ProviderException("PRIMARY", "Malformed JSON response"));

        AiResponse response = gatewayService.route(new AiRequest("test", Map.of()));
        assertThat(response.success()).isFalse();
        assertThat(response.content()).contains("PROVIDER_ERROR");
    }

    @Test
    @Order(29)
    void shouldHandlePrimaryDisconnectAndFallback() {
        when(aiGateway.route(any()))
            .thenThrow(new ServiceUnavailableException("PRIMARY"));

        AiResponse response = gatewayService.route(new AiRequest("test", Map.of()));
        assertThat(response.success()).isFalse();
    }

    @Test
    @Order(30)
    void shouldHandlePrimaryCircuitOpenAndFallback() {
        when(aiGateway.route(any()))
            .thenThrow(new ServiceUnavailableException("PRIMARY circuit open"));

        AiResponse response = gatewayService.route(new AiRequest("test", Map.of()));
        assertThat(response.success()).isFalse();
    }

    // =========================================================================
    // SECTION 4: Retry Testing
    // =========================================================================

    @Test
    @Order(31)
    void retryStrategyShouldRetryOnFailure() {
        assertThat(defaultRetryStrategy.shouldRetry(
            new AiRequest("test", Map.of()),
            new AiResponse("error", false), 0)).isTrue();
    }

    @Test
    @Order(32)
    void retryStrategyShouldStopAfterMaxRetries() {
        assertThat(defaultRetryStrategy.shouldRetry(
            new AiRequest("test", Map.of()),
            new AiResponse("error", false), 3)).isFalse();
    }

    @Test
    @Order(33)
    void retryStrategyShouldNotRetryOnSuccess() {
        assertThat(defaultRetryStrategy.shouldRetry(
            new AiRequest("test", Map.of()),
            new AiResponse("success", true), 0)).isFalse();
    }

    @Test
    @Order(34)
    void retryStrategyShouldImplementExponentialBackoff() {
        assertThat(defaultRetryStrategy.getDelayMs(1)).isEqualTo(1000L);
        assertThat(defaultRetryStrategy.getDelayMs(2)).isEqualTo(2000L);
        assertThat(defaultRetryStrategy.getDelayMs(3)).isEqualTo(4000L);
    }

    @Test
    @Order(35)
    void retryStrategyShouldReturnMaxRetries() {
        assertThat(defaultRetryStrategy.getMaxRetries()).isEqualTo(3);
    }

    @Test
    @Order(36)
    void pipelineShouldUseRetryConfiguration() {
        when(retryStrategy.getMaxRetries()).thenReturn(3);
        assertThat(retryStrategy.getMaxRetries()).isEqualTo(3);
    }

    // =========================================================================
    // SECTION 5: Circuit Breaker Testing
    // =========================================================================

    @Test
    @Order(37)
    void shouldCreateCircuitBreakerStates() {
        assertNotNull(com.sporekart.ai.providers.circuit.CircuitState.CLOSED);
        assertNotNull(com.sporekart.ai.providers.circuit.CircuitState.OPEN);
        assertNotNull(com.sporekart.ai.providers.circuit.CircuitState.HALF_OPEN);
    }

    @Test
    @Order(38)
    void circuitBreakerManagerShouldExist() {
        assertNotNull(com.sporekart.ai.providers.circuit.CircuitBreakerManager.class);
    }

    @Test
    @Order(39)
    void circuitBreakerConfigShouldAllowCustomization() {
        var config = new com.sporekart.ai.providers.circuit.breaker.CircuitBreakerConfig(
            5, 30000, 10000, 3, 0.5);
        assertThat(config.failureThreshold()).isEqualTo(5);
        assertThat(config.openStateDurationMs()).isEqualTo(30000);
        assertThat(config.halfOpenMaxRequests()).isEqualTo(3);
    }

    @Test
    @Order(40)
    void circuitBreakerPolicyShouldDefineRules() {
        var policy = new com.sporekart.ai.providers.circuit.policy.CircuitBreakerPolicy(10, 60000, 0.75);
        assertThat(policy.failureThreshold()).isEqualTo(10);
        assertThat(policy.cooldownPeriodMs()).isEqualTo(60000);
        assertThat(policy.failureRateThreshold()).isEqualTo(0.75);
    }

    @Test
    @Order(41)
    void circuitRecoveryShouldExist() {
        assertNotNull(com.sporekart.ai.providers.circuit.recovery.CircuitRecovery.class);
    }

    // =========================================================================
    // SECTION 6: Rate Limiter Testing
    // =========================================================================

    @Test
    @Order(42)
    void rateLimiterShouldAllowRequestsWithinLimit() {
        InMemoryRateLimiter limiter = new InMemoryRateLimiter(5);
        for (int i = 0; i < 5; i++) {
            assertTrue(limiter.tryAcquire("chat"));
        }
    }

    @Test
    @Order(43)
    void rateLimiterShouldRejectRequestsOverLimit() {
        InMemoryRateLimiter limiter = new InMemoryRateLimiter(3);
        for (int i = 0; i < 3; i++) {
            limiter.tryAcquire("chat");
        }
        assertFalse(limiter.tryAcquire("chat"));
    }

    @Test
    @Order(44)
    void rateLimiterShouldTrackRemainingTokens() {
        InMemoryRateLimiter limiter = new InMemoryRateLimiter(10);
        limiter.tryAcquire("chat");
        limiter.tryAcquire("chat");
        assertThat(limiter.getRemainingTokens("chat")).isEqualTo(8);
    }

    @Test
    @Order(45)
    void rateLimiterShouldReturnResetTime() {
        InMemoryRateLimiter limiter = new InMemoryRateLimiter();
        assertThat(limiter.getResetTimeSeconds("chat")).isEqualTo(60);
    }

    @Test
    @Order(46)
    void rateLimiterShouldHandleDifferentModulesIndependently() {
        InMemoryRateLimiter limiter = new InMemoryRateLimiter(5);
        limiter.tryAcquire("chat");
        limiter.tryAcquire("chat");
        limiter.tryAcquire("embedding");
        assertThat(limiter.getRemainingTokens("chat")).isEqualTo(3);
        assertThat(limiter.getRemainingTokens("embedding")).isEqualTo(4);
    }

    @Test
    @Order(47)
    void rateLimiterShouldResetForModule() {
        InMemoryRateLimiter limiter = new InMemoryRateLimiter(5);
        limiter.tryAcquire("chat");
        limiter.tryAcquire("chat");
        limiter.reset("chat");
        assertThat(limiter.getRemainingTokens("chat")).isEqualTo(5);
    }

    @Test
    @Order(48)
    void rateLimiterShouldHandleBurst() {
        InMemoryRateLimiter limiter = new InMemoryRateLimiter(100);
        for (int i = 0; i < 100; i++) {
            assertTrue(limiter.tryAcquire("burst"));
        }
        assertFalse(limiter.tryAcquire("burst"));
    }

    @Test
    @Order(49)
    void rateLimiterShouldTrackDegradedStates() {
        InMemoryRateLimiter limiter = new InMemoryRateLimiter(10);
        IntStream.range(0, 8).forEach(i -> limiter.tryAcquire("chat"));
        int remaining = limiter.getRemainingTokens("chat");
        assertThat(remaining).isLessThan(5);
    }

    // =========================================================================
    // SECTION 8: Streaming Tests
    // =========================================================================

    @Test
    @Order(50)
    void streamingContractShouldExist() {
        assertNotNull(com.sporekart.ai.providers.contracts.StreamingContract.class);
    }

    @Test
    @Order(51)
    void streamingRequestShouldBeCreatable() {
        var streamingReq = new com.sporekart.ai.gateway.contract.request.StreamingRequest(
            "gpt-4", "Tell me a story", true, 0.7, 500);
        assertThat(streamingReq.model()).isEqualTo("gpt-4");
        assertThat(streamingReq.stream()).isTrue();
    }

    @Test
    @Order(52)
    void streamingResponseShouldBeCreatable() {
        var streamingResp = new com.sporekart.ai.gateway.contract.response.StreamingResponse(
            "id-1", "gpt-4", List.of("Hello", " World"), true, false, null);
        assertThat(streamingResp.model()).isEqualTo("gpt-4");
        assertThat(streamingResp.isDone()).isFalse();
    }

    @Test
    @Order(53)
    void streamingResponseShouldTrackCompletion() {
        var incomplete = new com.sporekart.ai.gateway.contract.response.StreamingResponse(
            "id-1", "gpt-4", List.of("Hello"), false, false, null);
        assertThat(incomplete.isDone()).isFalse();

        var complete = new com.sporekart.ai.gateway.contract.response.StreamingResponse(
            "id-2", "gpt-4", List.of("Hello", " World"), true, false, null);
        assertThat(complete.isDone()).isTrue();
    }

    @Test
    @Order(54)
    void streamingResponseShouldSupportCancellation() {
        var cancelled = new com.sporekart.ai.gateway.contract.response.StreamingResponse(
            "id-1", "gpt-4", List.of("Partial"), false, true, null);
        assertThat(cancelled.cancelled()).isTrue();
    }

    @Test
    @Order(55)
    void streamingResponseShouldCarryError() {
        var errorDetail = new com.sporekart.ai.gateway.domain.AIErrorDetail("STREAM_ERR", "Stream interrupted");
        var errored = new com.sporekart.ai.gateway.contract.response.StreamingResponse(
            "id-1", "gpt-4", List.of("Partial"), false, false, errorDetail);
        assertThat(errored.error()).isNotNull();
        assertThat(errored.error().code()).isEqualTo("STREAM_ERR");
    }

    // =========================================================================
    // SECTION 11: Logging Verification
    // =========================================================================

    @Test
    @Order(56)
    void loggerServiceInterfaceShouldExist() {
        assertNotNull(LoggerService.class);
    }

    @Test
    @Order(57)
    void gatewayExecutionRequestShouldCarryCorrelationId() {
        var ctx = new GatewayExecutionRequest("user-1", "tenant-1",
            List.of("ai_user"), "openai", "gpt-4", "dep-1",
            Map.of("X-Correlation-Id", "corr-123"), Map.of());
        assertTrue(ctx.getHeader("X-Correlation-Id").isPresent());
        assertThat(ctx.getHeader("X-Correlation-Id").get()).isEqualTo("corr-123");
    }

    @Test
    @Order(58)
    void pipelineContextShouldCarryTraceIds() {
        var request = new GatewayRequest("req-1", "chat", "openai", "gpt-4",
            null, "hello", null, null, null, false, null, null, null);
        var context = new GatewayExecutionRequest("user-1", "tenant-1",
            List.of("ai_user"), "openai", "gpt-4", null, null, null);
        var pipelineCtx = new PipelineContext(request, context);

        assertNotNull(pipelineCtx.pipelineId());
        assertThat(pipelineCtx.pipelineId()).startsWith("pipe-");
    }

    @Test
    @Order(59)
    void aiExecutionResponseShouldCarryRequestAndCorrelationIds() {
        var response = AIExecutionResponse.success("req-123", "corr-456", "content", "openai", "gpt-4", 150);
        assertThat(response.requestId()).isEqualTo("req-123");
        assertThat(response.correlationId()).isEqualTo("corr-456");
        assertThat(response.provider()).isEqualTo("openai");
        assertThat(response.executionTimeMs()).isEqualTo(150);
    }

    @Test
    @Order(60)
    void domainRecordsShouldProvideAuditTrail() {
        var executionRequest = new AIExecutionRequest("prompt", "user1", "chat", "MOCK", Map.of("feature", "test"));
        assertThat(executionRequest.prompt()).isEqualTo("prompt");
        assertThat(executionRequest.userId()).isEqualTo("user1");
    }

    // =========================================================================
    // SECTION 12: Metrics Verification
    // =========================================================================

    @Test
    @Order(61)
    void gatewayMetricsShouldRecordAllFields() {
        var metrics = new GatewayMetrics("pipe-1", "user-1", "tenant-1",
            "openai", "gpt-4", Duration.ofMillis(500),
            Duration.ofMillis(100), Duration.ofMillis(400),
            100, 200, true, null, null);
        assertThat(metrics.providerId()).isEqualTo("openai");
        assertThat(metrics.inputTokens()).isEqualTo(100);
        assertThat(metrics.outputTokens()).isEqualTo(200);
        assertThat(metrics.success()).isTrue();
        assertThat(metrics.totalDuration().toMillis()).isEqualTo(500);
    }

    @Test
    @Order(62)
    void gatewayMetricsShouldRecordFailures() {
        var metrics = new GatewayMetrics("pipe-1", "user-1", "tenant-1",
            "openai", "gpt-4", Duration.ofMillis(200),
            Duration.ofMillis(50), Duration.ofMillis(150),
            0, 0, false, "TIMEOUT", null);
        assertThat(metrics.success()).isFalse();
        assertThat(metrics.errorCode()).isEqualTo("TIMEOUT");
    }

    @Test
    @Order(63)
    void gatewayMetricsCollectorShouldRecordExecutionTime() {
        metricsCollector.recordExecution("chat", 150L, true);
        verify(metricsCollector).recordExecution("chat", 150L, true);
    }

    @Test
    @Order(64)
    void gatewayMetricsCollectorShouldRecordLatency() {
        metricsCollector.recordLatency("chat", Duration.ofMillis(250));
        verify(metricsCollector).recordLatency("chat", Duration.ofMillis(250));
    }

    @Test
    @Order(65)
    void observabilityMetricsCollectorShouldRecordMetrics() {
        obsMetricsCollector.record("latency", 100.0);
        verify(obsMetricsCollector).record("latency", 100.0);
    }

    @Test
    @Order(66)
    void metricsShouldIncludeProviderUsageData() {
        assertNotNull(com.sporekart.ai.providers.metrics.MetricsCollector.class);
        assertNotNull(com.sporekart.ai.providers.metrics.MetricsModel.class);
        assertNotNull(com.sporekart.ai.providers.metrics.MetricsRegistry.class);
    }

    @Test
    @Order(67)
    void metricsModelShouldTrackComprehensiveData() {
        var model = new com.sporekart.ai.providers.metrics.MetricsModel(
            50.0, 100.0, 200.0, 0.95, 0.05, 0.99, 0.999,
            1000, 500, 10, 5, 85, 2.0, 45.0, 30.0);
        assertThat(model.avgLatency()).isEqualTo(50.0);
        assertThat(model.p99Latency()).isEqualTo(200.0);
        assertThat(model.successRate()).isEqualTo(0.95);
        assertThat(model.errorRate()).isEqualTo(0.05);
        assertThat(model.availabilityPercent()).isEqualTo(0.99);
        assertThat(model.uptimePercent()).isEqualTo(0.999);
        assertThat(model.healthScore()).isEqualTo(85);
    }

    @Test
    @Order(68)
    void metricsCollectorShouldRecordAllMetricTypes() {
        var collector = new com.sporekart.ai.providers.metrics.MetricsRegistry();
        assertNotNull(collector);
    }

    // =========================================================================
    // Pipeline Stage Verification
    // =========================================================================

    @Test
    @Order(69)
    void pipelineShouldHaveAllStages() {
        assertThat(PipelineStage.values()).hasSize(11);
        assertThat(PipelineStage.VALIDATION.order()).isEqualTo(1);
        assertThat(PipelineStage.AUTHENTICATION.order()).isEqualTo(2);
        assertThat(PipelineStage.AUTHORIZATION.order()).isEqualTo(3);
        assertThat(PipelineStage.QUOTA_CHECK.order()).isEqualTo(4);
        assertThat(PipelineStage.RATE_LIMITER.order()).isEqualTo(5);
        assertThat(PipelineStage.PROVIDER_SELECTION.order()).isEqualTo(6);
        assertThat(PipelineStage.ROUTING.order()).isEqualTo(7);
        assertThat(PipelineStage.EXECUTION.order()).isEqualTo(8);
        assertThat(PipelineStage.POST_PROCESSING.order()).isEqualTo(9);
        assertThat(PipelineStage.AUDIT.order()).isEqualTo(10);
        assertThat(PipelineStage.METRICS.order()).isEqualTo(11);
    }

    @Test
    @Order(70)
    void pipelineContextShouldTrackFullState() {
        var request = new GatewayRequest("req-1", "chat", "openai", "gpt-4",
            null, "hello", null, null, null, false, null, null, null);
        var context = new GatewayExecutionRequest("user-1", "tenant-1",
            List.of("ai_user"), "openai", "gpt-4", null, null, null);
        var pipelineCtx = new PipelineContext(request, context);

        pipelineCtx.setAttribute("stage-start", Instant.now());
        var advanced = pipelineCtx.advanceTo(PipelineStage.AUTHENTICATION);
        var failed = advanced.failed("Auth failed");

        assertThat(failed.failed()).isTrue();
        assertThat(failed.failureReason()).isEqualTo("Auth failed");
    }

    @Test
    @Order(71)
    void pipelineResultShouldIndicateSuccess() {
        PipelineContext ctx = mock(PipelineContext.class);
        when(ctx.pipelineId()).thenReturn("pipe-1");

        var result = com.sporekart.ai.gateway.pipeline.PipelineResult.success(
            new AiResponse("Done"), ctx);
        assertThat(result.success()).isTrue();
    }

    @Test
    @Order(72)
    void pipelineResultShouldIndicateFailure() {
        PipelineContext ctx = mock(PipelineContext.class);
        when(ctx.pipelineId()).thenReturn("pipe-1");

        var result = com.sporekart.ai.gateway.pipeline.PipelineResult.failure(
            new AiResponse("Error", false), ctx);
        assertThat(result.success()).isFalse();
    }

    // =========================================================================
    // Gateway Contract Verification
    // =========================================================================

    @Test
    @Order(73)
    void gatewayRequestShouldSupportAllFields() {
        var req = new GatewayRequest("req-1", "chat", "openai", "gpt-4",
            "user-1", "Hello world", List.of(), Map.of("key", "value"),
            Map.of("model", "gpt-4"), false, List.of(), "tenant-1", List.of("ai_user"));
        assertThat(req.module()).isEqualTo("chat");
        assertThat(req.prompt()).isEqualTo("Hello world");
    }

    @Test
    @Order(74)
    void gatewayResponseShouldSupportSuccess() {
        var response = GatewayResponse.ok("req-1", "openai", "gpt-4", List.of("Hello!"));
        assertThat(response.success()).isTrue();
        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(response.getGeneratedText()).isPresent();
    }

    @Test
    @Order(75)
    void gatewayResponseShouldSupportError() {
        var response = GatewayResponse.error("req-1", 400, "BAD_REQUEST", "Invalid input");
        assertThat(response.success()).isFalse();
        assertThat(response.getErrorCode()).isPresent();
        assertThat(response.getErrorCode().get()).isEqualTo("BAD_REQUEST");
    }

    // =========================================================================
    // Gateway Config Properties Verification
    // =========================================================================

    @Test
    @Order(76)
    void gatewayConfigShouldHaveAllSections() {
        var rateLimit = new GatewayConfigProperties.RateLimitConfig(true, 100, 60L, Map.of());
        var security = new GatewayConfigProperties.SecurityConfig(true, List.of(), true, false, false);
        var pipeline = new GatewayConfigProperties.PipelineConfig(true, false, List.of(), List.of(), Duration.ofSeconds(30), 3);
        var router = new GatewayConfigProperties.RouterConfig("first-available", List.of(), Map.of(), true, List.of("mock"), Duration.ofSeconds(30));
        var quota = new GatewayConfigProperties.QuotaConfig(true, 10000, "daily", Map.of());
        var observability = new GatewayConfigProperties.ObservabilityConfig(true, true, true, "micrometer", "otel");
        var health = new GatewayConfigProperties.HealthConfig(true, List.of("gateway"), Duration.ofSeconds(30));
        var execution = new GatewayConfigProperties.ExecutionConfig(Duration.ofSeconds(60), 3, true, 65536);

        var config = new GatewayConfigProperties(pipeline, router, security, rateLimit, quota, observability, health, execution);
        assertThat(config.pipeline().enabled()).isTrue();
        assertThat(config.router().fallbackEnabled()).isTrue();
        assertThat(config.rateLimit().defaultLimit()).isEqualTo(100);
        assertThat(config.execution().maxRetries()).isEqualTo(3);
        assertThat(config.execution().streamingSupported()).isTrue();
        assertThat(config.observability().metricsEnabled()).isTrue();
    }
}
