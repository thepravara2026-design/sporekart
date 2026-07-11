package com.sporekart.ai.gateway.application;

import com.sporekart.ai.core.api.AIExecutionPipeline;
import com.sporekart.ai.core.api.AIContextResolver;
import com.sporekart.ai.core.api.ProviderResolver;
import com.sporekart.ai.core.api.RetryStrategy;
import com.sporekart.ai.core.api.TimeoutStrategy;
import com.sporekart.ai.core.application.exception.AIExecutionException;
import com.sporekart.ai.core.application.exception.AIValidationException;
import com.sporekart.ai.core.application.exception.FeatureDisabledException;
import com.sporekart.ai.core.application.exception.GatewayUnavailableException;
import com.sporekart.ai.core.application.featureflag.FeatureFlagName;
import com.sporekart.ai.core.application.featureflag.FeatureFlagService;
import com.sporekart.ai.core.domain.AiProviderType;
import com.sporekart.ai.core.domain.AiRequest;
import com.sporekart.ai.core.domain.AiResponse;
import com.sporekart.ai.core.domain.CorrelationId;
import com.sporekart.ai.gateway.api.AiGateway;
import com.sporekart.ai.gateway.api.RateLimiter;
import com.sporekart.ai.gateway.api.RequestValidator;
import com.sporekart.ai.gateway.domain.AIExecutionResult;
import com.sporekart.ai.gateway.domain.GatewayExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class GatewayPipeline implements AIExecutionPipeline {
    private static final Logger log = LoggerFactory.getLogger(GatewayPipeline.class);

    private final FeatureFlagService featureFlagService;
    private final RateLimiter rateLimiter;
    private final RequestValidator requestValidator;
    private final AiGateway aiGateway;
    private final AIContextResolver contextResolver;
    private final ProviderResolver providerResolver;
    private final RetryStrategy retryStrategy;
    private final TimeoutStrategy timeoutStrategy;
    private final GatewayAuditService auditService;
    private final GatewayMetricsCollector metricsCollector;

    public GatewayPipeline(FeatureFlagService featureFlagService, RateLimiter rateLimiter,
            RequestValidator requestValidator, AiGateway aiGateway,
            AIContextResolver contextResolver, ProviderResolver providerResolver,
            RetryStrategy retryStrategy, TimeoutStrategy timeoutStrategy,
            GatewayAuditService auditService, GatewayMetricsCollector metricsCollector) {
        this.featureFlagService = featureFlagService;
        this.rateLimiter = rateLimiter;
        this.requestValidator = requestValidator;
        this.aiGateway = aiGateway;
        this.contextResolver = contextResolver;
        this.providerResolver = providerResolver;
        this.retryStrategy = retryStrategy;
        this.timeoutStrategy = timeoutStrategy;
        this.auditService = auditService;
        this.metricsCollector = metricsCollector;
    }

    @Override
    public AiResponse execute(AiRequest request, CorrelationId correlationId) {
        long startNanos = System.nanoTime();
        String executionId = UUID.randomUUID().toString();

        if (!featureFlagService.isEnabled(FeatureFlagName.AI_PLATFORM_ENABLED)
                || !featureFlagService.isEnabled(FeatureFlagName.AI_GATEWAY_ENABLED)) {
            throw new FeatureDisabledException("AI Gateway");
        }

        requestValidator.validate(request);

        if (featureFlagService.isEnabled(FeatureFlagName.AI_RATE_LIMITING)) {
            if (!rateLimiter.tryAcquire(request.module())) {
                metricsCollector.incrementError(request.module(), "rate_limit");
                throw new com.sporekart.ai.core.application.exception.AIRateLimitException(request.module());
            }
        }

        GatewayExecutionContext context = buildContext(request, correlationId, executionId);
        auditService.recordRequestReceived(context);

        if (featureFlagService.isEnabled(FeatureFlagName.AI_REQUEST_LOGGING)) {
            log.info("AI Request [{}] module={} userId={}", executionId, request.module(), context.userId());
        }

        Optional<AiProviderType> resolvedProvider = providerResolver.resolve(
                request.module(), context.provider());
        if (resolvedProvider.isEmpty()) {
            metricsCollector.incrementError(request.module(), "no_provider");
            throw new AIExecutionException("AI-003", "No provider available for module: " + request.module());
        }

        Duration timeout = timeoutStrategy.getTimeout(request.module());
        if (timeoutStrategy.isExpired(startNanos, timeout)) {
            throw new AIExecutionException("AI-004", "Request timed out before execution");
        }

        AiResponse response = executeWithRetry(request, executionId);

        long durationMs = Duration.ofNanos(System.nanoTime() - startNanos).toMillis();
        metricsCollector.recordExecution(request.module(), durationMs, response.success());
        auditService.recordRequestCompleted(context, response.success(), durationMs);

        if (featureFlagService.isEnabled(FeatureFlagName.AI_REQUEST_LOGGING)) {
            log.info("AI Response [{}] success={} durationMs={}", executionId, response.success(), durationMs);
        }

        return response;
    }

    private AiResponse executeWithRetry(AiRequest request, String executionId) {
        int maxRetries = retryStrategy.getMaxRetries();
        AiResponse lastResponse = null;

        for (int attempt = 1; attempt <= maxRetries; attempt++) {
            try {
                lastResponse = aiGateway.route(request);
                if (lastResponse.success()) {
                    return lastResponse;
                }
                if (!retryStrategy.shouldRetry(request, lastResponse, attempt)) {
                    return lastResponse;
                }
                if (attempt < maxRetries) {
                    long delayMs = retryStrategy.getDelayMs(attempt);
                    Thread.sleep(delayMs);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new AIExecutionException("AI-999", "Execution interrupted");
            } catch (Exception e) {
                if (attempt >= maxRetries) {
                    throw new AIExecutionException("AI-014", "Execution failed after " + maxRetries + " attempts", e);
                }
                try {
                    Thread.sleep(retryStrategy.getDelayMs(attempt));
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new AIExecutionException("AI-999", "Execution interrupted");
                }
            }
        }
        return lastResponse;
    }

    private GatewayExecutionContext buildContext(AiRequest request, CorrelationId correlationId, String executionId) {
        return new GatewayExecutionContext(
                executionId,
                correlationId,
                contextResolver.resolveUserId(request),
                contextResolver.resolveModule(request),
                contextResolver.resolveProvider(request),
                OffsetDateTime.now(),
                contextResolver.resolve(request, correlationId));
    }

    @Override
    public PipelineResult validate(AiRequest request) {
        try {
            requestValidator.validate(request);
            return PipelineResult.ACCEPTED;
        } catch (AIValidationException e) {
            return PipelineResult.REJECTED;
        }
    }

    @Override
    public PipelineStatus getStatus(String executionId) {
        return PipelineStatus.COMPLETED;
    }
}
