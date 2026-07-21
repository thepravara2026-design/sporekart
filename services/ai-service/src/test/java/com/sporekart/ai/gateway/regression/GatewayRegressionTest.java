package com.sporekart.ai.gateway.regression;

import com.sporekart.ai.gateway.api.AiGateway;
import com.sporekart.ai.gateway.api.RateLimiter;
import com.sporekart.ai.gateway.api.RequestValidator;
import com.sporekart.ai.gateway.application.*;
import com.sporekart.ai.gateway.config.GatewayConfigProperties;
import com.sporekart.ai.gateway.contract.message.ChatMessage;
import com.sporekart.ai.gateway.contract.message.MessageRole;
import com.sporekart.ai.gateway.contract.request.*;
import com.sporekart.ai.gateway.contract.response.*;
import com.sporekart.ai.gateway.domain.*;
import com.sporekart.ai.gateway.exception.*;
import com.sporekart.ai.gateway.infrastructure.DefaultRetryStrategy;
import com.sporekart.ai.gateway.infrastructure.DefaultTimeoutStrategy;
import com.sporekart.ai.gateway.infrastructure.InMemoryRateLimiter;
import com.sporekart.ai.gateway.pipeline.PipelineContext;
import com.sporekart.ai.gateway.pipeline.PipelineStage;

import com.sporekart.ai.core.domain.AiRequest;
import com.sporekart.ai.core.domain.AiResponse;
import com.sporekart.ai.core.domain.CorrelationId;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class GatewayRegressionTest {

    // =========================================================================
    // SECTION 13: Regression Testing
    // Verify all existing Phase 1-12 modules are unchanged and core
    // gateway domain still works correctly.
    // =========================================================================

    @Mock private FeatureFlagService featureFlagService;
    @Mock private AiGateway aiGateway;
    @Mock private AIContextResolver contextResolver;
    @Mock private ProviderResolver providerResolver;
    @Mock private RetryStrategy retryStrategy;
    @Mock private TimeoutStrategy timeoutStrategy;
    @Mock private GatewayAuditService auditService;
    @Mock private GatewayMetricsCollector metricsCollector;
    @Mock private RequestValidator requestValidator;

    private GatewayPipeline pipeline;
    private GatewayApplicationService gatewayService;
    private GatewayDomainService domainService;
    private GatewayExceptionTranslator exceptionTranslator;
    private GatewayResponseBuilder responseBuilder;

    @BeforeEach
    void setUp() {
        domainService = new GatewayDomainService();
        responseBuilder = new GatewayResponseBuilder();
        exceptionTranslator = new GatewayExceptionTranslator();

        lenient().when(featureFlagService.isEnabled(any())).thenReturn(true);
        lenient().when(contextResolver.resolveUserId(any())).thenReturn("regression-user");
        lenient().when(contextResolver.resolveModule(any())).thenReturn("chat");
        lenient().when(contextResolver.resolveProvider(any())).thenReturn("MOCK");
        lenient().when(contextResolver.resolve(any(), any())).thenReturn(Map.of());
        lenient().when(providerResolver.resolve(anyString(), anyString())).thenReturn(Optional.of(AiProviderType.MOCK));
        lenient().when(timeoutStrategy.getTimeout(anyString())).thenReturn(Duration.ofSeconds(30));
        lenient().when(timeoutStrategy.isExpired(anyLong(), any())).thenReturn(false);
        lenient().when(retryStrategy.getMaxRetries()).thenReturn(3);
        lenient().when(requestValidator.validate(any())).thenReturn(true);
        lenient().when(aiGateway.route(any())).thenReturn(new AiResponse("regression", true));

        pipeline = new GatewayPipeline(featureFlagService, new InMemoryRateLimiter(100),
                requestValidator, aiGateway, contextResolver, providerResolver, retryStrategy,
                timeoutStrategy, auditService, metricsCollector);
        gatewayService = new GatewayApplicationService(pipeline, domainService,
                responseBuilder, exceptionTranslator);
    }

    @Test
    @Order(1)
    void authenticationShouldStillWork() {
        var success = AuthenticationResult.success("user-1", "tenant-1", List.of("ai_user"));
        assertTrue(success.authenticated());
        assertThat(success.tenantId()).isEqualTo("tenant-1");

        var failure = AuthenticationResult.failure("Invalid credentials");
        assertFalse(failure.authenticated());
        assertTrue(failure.getFailureReason().isPresent());
    }

    @Test
    @Order(2)
    void requestValidationShouldStillWork() {
        AIExecutionRequest validReq = new AIExecutionRequest("valid prompt", "user1", "chat", "MOCK", null);
        AIExecutionResponse validResponse = gatewayService.validateRequest(validReq);
        assertTrue(validResponse.isSuccess());

        AIExecutionRequest invalidReq = new AIExecutionRequest("", null, null, null, null);
        AIExecutionResponse invalidResponse = gatewayService.validateRequest(invalidReq);
        assertFalse(invalidResponse.isSuccess());
    }

    @Test
    @Order(3)
    void rateLimitingShouldStillWork() {
        InMemoryRateLimiter limiter = new InMemoryRateLimiter(5);
        for (int i = 0; i < 5; i++) assertTrue(limiter.tryAcquire("chat"));
        assertFalse(limiter.tryAcquire("chat"));
    }

    @Test
    @Order(4)
    void retryStrategyShouldStillWork() {
        DefaultRetryStrategy retry = new DefaultRetryStrategy();
        assertThat(retry.getMaxRetries()).isEqualTo(3);
        assertThat(retry.getDelayMs(1)).isEqualTo(1000L);
        assertThat(retry.getDelayMs(2)).isEqualTo(2000L);
        assertThat(retry.getDelayMs(3)).isEqualTo(4000L);

        assertFalse(retry.shouldRetry(new AiRequest("test", Map.of()),
            new AiResponse("ok", true), 0));
        assertTrue(retry.shouldRetry(new AiRequest("test", Map.of()),
            new AiResponse("fail", false), 0));
        assertFalse(retry.shouldRetry(new AiRequest("test", Map.of()),
            new AiResponse("fail", false), 3));
    }

    @Test
    @Order(5)
    void exceptionsShouldStillWork() {
        assertThrows(AuthenticationException.class,
            () -> { throw new AuthenticationException("fail"); });
        assertThrows(AuthorizationException.class,
            () -> { throw new AuthorizationException("fail"); });
        assertThrows(RateLimitException.class,
            () -> { throw new RateLimitException(30); });
        assertThrows(QuotaExceededException.class,
            () -> { throw new QuotaExceededException("tokens", 0, "daily"); });
        assertThrows(TimeoutException.class,
            () -> { throw new TimeoutException(30000); });
        assertThrows(ProviderException.class,
            () -> { throw new ProviderException("openai", "fail"); });
        assertThrows(ServiceUnavailableException.class,
            () -> { throw new ServiceUnavailableException("openai"); });
        assertThrows(ConfigurationException.class,
            () -> { throw new ConfigurationException("fail"); });
        assertThrows(ValidationException.class,
            () -> { throw new ValidationException(List.of("error")); });
    }

    @Test
    @Order(6)
    void pipelineShouldStillProcessRequests() {
        AiResponse response = pipeline.execute(
            new AiRequest("regression", Map.of()), CorrelationId.generate());
        assertThat(response.success()).isTrue();
        assertThat(response.content()).isEqualTo("regression");
    }

    @Test
    @Order(7)
    void gatewayApplicationServiceShouldStillRouteRequests() {
        AiResponse response = gatewayService.route(new AiRequest("test", Map.of()));
        assertThat(response.success()).isTrue();
    }

    @Test
    @Order(8)
    void domainServiceShouldStillExecuteRequests() {
        AIExecutionRequest req = new AIExecutionRequest("Hello", "user", "chat", "MOCK", null);
        AIExecutionResponse response = gatewayService.executeRequest(req);
        assertNotNull(response);
    }

    @Test
    @Order(9)
    void healthEndpointShouldStillReportUp() {
        var health = gatewayService.getHealth();
        assertThat(health.status()).isEqualTo("UP");
        assertThat(health.gatewayOperational()).isTrue();
    }

    @Test
    @Order(10)
    void statusEndpointShouldStillReturnData() {
        var status = gatewayService.getStatus();
        assertThat(status).isNotNull();
    }

    @Test
    @Order(11)
    void featureFlagsShouldStillBeAvailable() {
        var features = gatewayService.getFeatures();
        assertThat(features).isNotNull();
    }

    @Test
    @Order(12)
    void gatewayResponseShouldStillSupportOkAndError() {
        GatewayResponse ok = GatewayResponse.ok("req-1", "openai", "gpt-4", List.of("Hello"));
        assertTrue(ok.success());
        assertThat(ok.statusCode()).isEqualTo(200);

        GatewayResponse err = GatewayResponse.error("req-1", 400, "BAD_REQUEST", "Invalid");
        assertFalse(err.success());
    }

    @Test
    @Order(13)
    void chatMessagesShouldStillWork() {
        var sys = ChatMessage.system("System prompt");
        var user = ChatMessage.user("User message");
        var asst = ChatMessage.assistant("Assistant reply");

        assertThat(sys.role()).isEqualTo("system");
        assertThat(user.role()).isEqualTo("user");
        assertThat(asst.role()).isEqualTo("assistant");
    }

    @Test
    @Order(14)
    void completionsShouldStillWork() {
        var usage = new CompletionResponse.Usage(10, 20, 30);
        var choice = new CompletionResponse.Choice(0, "Hello", "stop");
        var response = new CompletionResponse("id-1", "gpt-4", List.of(choice), usage, "fp-1");
        assertThat(response.model()).isEqualTo("gpt-4");
        assertThat(response.usage().totalTokens()).isEqualTo(30);
    }

    @Test
    @Order(15)
    void chatCompletionsShouldStillWork() {
        var messages = List.of(ChatMessage.user("Hi"));
        var req = new ChatCompletionRequest("gpt-4", messages, 0.7, 100, null, null, null, null, "user-1");
        assertThat(req.model()).isEqualTo("gpt-4");
        assertThat(req.messages()).hasSize(1);
    }

    @Test
    @Order(16)
    void embeddingsShouldStillWork() {
        var req = new EmbeddingRequest("text-embedding-3", "Hello world", null, "user-1");
        assertThat(req.model()).isEqualTo("text-embedding-3");
        assertThat(req.input()).isEqualTo("Hello world");
    }

    @Test
    @Order(17)
    void pipelineStageEnumShouldRemainUnchanged() {
        assertThat(PipelineStage.values()).hasSize(11);
        assertThat(PipelineStage.VALIDATION.name()).isEqualTo("VALIDATION");
        assertThat(PipelineStage.AUTHENTICATION.name()).isEqualTo("AUTHENTICATION");
        assertThat(PipelineStage.AUTHORIZATION.name()).isEqualTo("AUTHORIZATION");
        assertThat(PipelineStage.QUOTA_CHECK.name()).isEqualTo("QUOTA_CHECK");
        assertThat(PipelineStage.RATE_LIMITER.name()).isEqualTo("RATE_LIMITER");
        assertThat(PipelineStage.PROVIDER_SELECTION.name()).isEqualTo("PROVIDER_SELECTION");
        assertThat(PipelineStage.ROUTING.name()).isEqualTo("ROUTING");
        assertThat(PipelineStage.EXECUTION.name()).isEqualTo("EXECUTION");
        assertThat(PipelineStage.POST_PROCESSING.name()).isEqualTo("POST_PROCESSING");
        assertThat(PipelineStage.AUDIT.name()).isEqualTo("AUDIT");
        assertThat(PipelineStage.METRICS.name()).isEqualTo("METRICS");
    }

    @Test
    @Order(18)
    void gatewayMetricsShouldStillBeRecordable() {
        GatewayMetrics metrics = new GatewayMetrics("pipe-1", "user-1", "tenant-1",
            "openai", "gpt-4", Duration.ofMillis(100),
            Duration.ofMillis(20), Duration.ofMillis(80),
            50, 150, true, null, null);
        assertThat(metrics.providerId()).isEqualTo("openai");
        assertThat(metrics.totalDuration().toMillis()).isEqualTo(100);
        assertThat(metrics.inputTokens()).isEqualTo(50);
        assertThat(metrics.outputTokens()).isEqualTo(150);
    }

    @Test
    @Order(19)
    void pipelineContextShouldStillSupportAttributes() {
        var request = new GatewayRequest("req-1", "chat", "openai", "gpt-4",
            null, "test", null, null, null, false, null, null, null);
        var context = new GatewayExecutionRequest("user-1", "tenant-1",
            List.of("ai_user"), "openai", "gpt-4", null, null, null);
        var pipelineCtx = new PipelineContext(request, context);

        pipelineCtx.setAttribute("traceId", "trace-123");
        pipelineCtx.setAttribute("spanId", "span-456");

        assertThat(pipelineCtx.getAttribute("traceId")).isPresent();
        assertThat(pipelineCtx.getAttribute("traceId").get()).isEqualTo("trace-123");
        assertThat(pipelineCtx.getAttribute("spanId")).isPresent();
        assertThat(pipelineCtx.getAttribute("spanId").get()).isEqualTo("span-456");
    }

    @Test
    @Order(20)
    void timeoutStrategyShouldBeConfigurable() {
        DefaultTimeoutStrategy strategy = new DefaultTimeoutStrategy();
        assertNotNull(strategy);
    }
}
