package com.sporekart.ai.gateway.application;

import com.sporekart.ai.core.api.AIContextResolver;
import com.sporekart.ai.core.api.ProviderResolver;
import com.sporekart.ai.core.api.RetryStrategy;
import com.sporekart.ai.core.api.TimeoutStrategy;
import com.sporekart.ai.core.application.exception.AIValidationException;
import com.sporekart.ai.core.application.exception.FeatureDisabledException;
import com.sporekart.ai.core.application.featureflag.FeatureFlagName;
import com.sporekart.ai.core.application.featureflag.FeatureFlagService;
import com.sporekart.ai.core.domain.*;
import com.sporekart.ai.core.api.AIExecutionPipeline;
import com.sporekart.ai.gateway.api.AiGateway;
import com.sporekart.ai.gateway.api.RateLimiter;
import com.sporekart.ai.gateway.api.RequestValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GatewayPipelineTest {

    @Mock private FeatureFlagService featureFlagService;
    @Mock private RateLimiter rateLimiter;
    @Mock private RequestValidator requestValidator;
    @Mock private AiGateway aiGateway;
    @Mock private AIContextResolver contextResolver;
    @Mock private ProviderResolver providerResolver;
    @Mock private RetryStrategy retryStrategy;
    @Mock private TimeoutStrategy timeoutStrategy;
    @Mock private GatewayAuditService auditService;
    @Mock private GatewayMetricsCollector metricsCollector;

    private GatewayPipeline pipeline;
    private AiRequest validRequest;
    private CorrelationId correlationId;

    @BeforeEach
    void setUp() {
        pipeline = new GatewayPipeline(featureFlagService, rateLimiter, requestValidator,
                aiGateway, contextResolver, providerResolver, retryStrategy,
                timeoutStrategy, auditService, metricsCollector);
        validRequest = new AiRequest("What is my order status?", Map.of());
        correlationId = CorrelationId.generate();

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
        lenient().when(timeoutStrategy.getTimeout(anyString())).thenReturn(java.time.Duration.ofSeconds(30));
        lenient().when(timeoutStrategy.isExpired(anyLong(), any())).thenReturn(false);
        lenient().when(retryStrategy.getMaxRetries()).thenReturn(1);
    }

    @Test
    void shouldExecuteRequestSuccessfully() {
        AiResponse mockResponse = new AiResponse("Mock answer");
        when(aiGateway.route(any())).thenReturn(mockResponse);

        AiResponse response = pipeline.execute(validRequest, correlationId);

        assertThat(response.success()).isTrue();
        assertThat(response.content()).isEqualTo("Mock answer");
        verify(auditService).recordRequestReceived(any());
        verify(auditService).recordRequestCompleted(any(), eq(true), anyLong());
        verify(metricsCollector).recordExecution(eq("chat"), anyLong(), eq(true));
    }

    @Test
    void shouldThrowWhenPlatformDisabled() {
        when(featureFlagService.isEnabled(FeatureFlagName.AI_PLATFORM_ENABLED)).thenReturn(false);

        assertThrows(FeatureDisabledException.class,
                () -> pipeline.execute(validRequest, correlationId));
    }

    @Test
    void shouldThrowWhenGatewayDisabled() {
        when(featureFlagService.isEnabled(FeatureFlagName.AI_GATEWAY_ENABLED)).thenReturn(false);

        assertThrows(FeatureDisabledException.class,
                () -> pipeline.execute(validRequest, correlationId));
    }

    @Test
    void shouldPropagateValidationErrors() {
        doThrow(new AIValidationException("Prompt must not be blank"))
                .when(requestValidator).validate(any());

        assertThrows(AIValidationException.class,
                () -> pipeline.execute(validRequest, correlationId));
    }

    @Test
    void shouldAcceptValidRequestDuringValidation() {
        assertThat(pipeline.validate(validRequest)).isEqualTo(AIExecutionPipeline.PipelineResult.ACCEPTED);
    }

    @Test
    void shouldRejectInvalidRequestDuringValidation() {
        doThrow(new AIValidationException("Invalid"))
                .when(requestValidator).validate(any());

        assertThat(pipeline.validate(validRequest)).isEqualTo(AIExecutionPipeline.PipelineResult.REJECTED);
    }

    @Test
    void shouldReturnCompletedStatus() {
        assertThat(pipeline.getStatus("test-id")).isEqualTo(AIExecutionPipeline.PipelineStatus.COMPLETED);
    }
}
