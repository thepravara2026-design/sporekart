package com.sporekart.ai.pipeline;

import com.sporekart.ai.pipeline.context.ContextBuilder;
import com.sporekart.ai.pipeline.error.ErrorTranslator;
import com.sporekart.ai.pipeline.error.PipelineError;
import com.sporekart.ai.pipeline.execution.ExecutionEngine;
import com.sporekart.ai.pipeline.middleware.*;
import com.sporekart.ai.pipeline.model.PipelineRequest;
import com.sporekart.ai.pipeline.model.PipelineResponse;
import com.sporekart.ai.pipeline.observability.PipelineAudit;
import com.sporekart.ai.pipeline.observability.PipelineMetrics;
import com.sporekart.ai.pipeline.prompt.PromptCompiler;
import com.sporekart.ai.pipeline.retry.RetryManager;
import com.sporekart.ai.pipeline.retry.RetryPolicy;
import com.sporekart.ai.pipeline.timeout.TimeoutManager;
import com.sporekart.ai.pipeline.validation.RequestValidator;
import com.sporekart.ai.pipeline.normalization.ResponseNormalizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;

public class PipelineOrchestrator {
    private static final Logger log = LoggerFactory.getLogger(PipelineOrchestrator.class);

    private final RequestValidator requestValidator;
    private final ContextBuilder contextBuilder;
    private final PromptCompiler promptCompiler;
    private final ExecutionEngine executionEngine;
    private final ResponseNormalizer responseNormalizer;
    private final RetryManager retryManager;
    private final TimeoutManager timeoutManager;
    private final ErrorTranslator errorTranslator;
    private final PipelineMetrics metrics;
    private final PipelineAudit audit;
    private final MiddlewareChain middlewareChain;

    public PipelineOrchestrator() {
        this.requestValidator = new RequestValidator();
        this.contextBuilder = new ContextBuilder();
        this.promptCompiler = new PromptCompiler();
        this.executionEngine = new ExecutionEngine();
        this.responseNormalizer = new ResponseNormalizer();
        this.retryManager = new RetryManager(RetryPolicy.DEFAULT);
        this.timeoutManager = new TimeoutManager();
        this.errorTranslator = new ErrorTranslator();
        this.metrics = new PipelineMetrics();
        this.audit = new PipelineAudit();
        this.middlewareChain = buildDefaultMiddlewareChain();
    }

    public PipelineOrchestrator(RequestValidator requestValidator, ContextBuilder contextBuilder,
            PromptCompiler promptCompiler, ExecutionEngine executionEngine,
            ResponseNormalizer responseNormalizer, RetryManager retryManager,
            TimeoutManager timeoutManager, ErrorTranslator errorTranslator,
            PipelineMetrics metrics, PipelineAudit audit) {
        this.requestValidator = requestValidator;
        this.contextBuilder = contextBuilder;
        this.promptCompiler = promptCompiler;
        this.executionEngine = executionEngine;
        this.responseNormalizer = responseNormalizer;
        this.retryManager = retryManager;
        this.timeoutManager = timeoutManager;
        this.errorTranslator = errorTranslator;
        this.metrics = metrics;
        this.audit = audit;
        this.middlewareChain = buildDefaultMiddlewareChain();
    }

    public PipelineResult execute(PipelineRequest request) {
        var context = new PipelineContext(request);
        log.debug("Pipeline [{}] orchestrating request {}", context.pipelineId(), request.requestId());

        try {
            metrics.recordPipelineStart(context);
            var auditRef = audit.recordRequestReceived(context);

            middlewareChain.execute(context);

            if (!context.failed()) {
                contextBuilder.build(context);
                promptCompiler.compile(context);
                timeoutManager.scheduleCancellation(context,
                        () -> handleTimeout(context));

                executeWithRetryAndTimeout(context);
            }

            metrics.recordPipelineCompletion(context);
            audit.recordPipelineCompleted(context);

            if (context.failed()) {
                var error = errorTranslator.translate(
                        new PipelineException(request.requestId(),
                                PipelineException.ErrorCode.UNKNOWN, context.failureReason()));
                log.warn("Pipeline [{}] failed: {}", context.pipelineId(), error.message());
                return PipelineResult.failure(context.pipelineId(), request.requestId(),
                        error.message(), context.elapsed(), context.retryCount());
            }

            var response = context.response();
            if (response == null) {
                return PipelineResult.failure(context.pipelineId(), request.requestId(),
                        "No response produced", context.elapsed(), context.retryCount());
            }

            return PipelineResult.success(context.pipelineId(), response,
                    context.elapsed(), context.selectedProvider(), context.selectedModel());

        } catch (PipelineException e) {
            metrics.recordPipelineCompletion(context);
            audit.recordFailure(context);
            var error = errorTranslator.translate(e);
            return PipelineResult.failure(context.pipelineId(), request.requestId(),
                    error.message(), context.elapsed(), context.retryCount());
        } catch (Exception e) {
            metrics.recordPipelineCompletion(context);
            audit.recordFailure(context);
            var error = errorTranslator.translate(e);
            log.error("Pipeline [{}] unexpected error: {}", context.pipelineId(), error.message(), e);
            return PipelineResult.failure(context.pipelineId(), request.requestId(),
                    error.message(), context.elapsed(), context.retryCount());
        }
    }

    private void executeWithRetryAndTimeout(PipelineContext context) {
        retryManager.executeWithRetry(context, () -> {
            if (context.failed()) return;
            timeoutManager.executeWithTimeout(context, () -> {
                executionEngine.execute(context);
                audit.recordExecutionCompleted(context);
                return null;
            });
        });
    }

    private void handleTimeout(PipelineContext context) {
        context.fail("Pipeline timeout exceeded");
        metrics.recordTimeout();
        audit.recordTimeout(context);
    }

    public PipelineMetrics getMetrics() {
        return metrics;
    }

    public PipelineAudit getAudit() {
        return audit;
    }

    public MiddlewareChain getMiddlewareChain() {
        return middlewareChain;
    }

    private MiddlewareChain buildDefaultMiddlewareChain() {
        return new MiddlewareChain(List.of(
                new LoggingMiddleware(),
                new TracingMiddleware(),
                new ValidationMiddleware(requestValidator),
                new AuthenticationMiddleware(),
                new AuthorizationMiddleware(),
                new RateLimitingMiddleware(),
                new ProviderSelectionMiddleware(),
                new MetricsMiddleware(),
                new ResponseFormattingMiddleware(responseNormalizer)
        ));
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private RequestValidator requestValidator;
        private ContextBuilder contextBuilder;
        private PromptCompiler promptCompiler;
        private ExecutionEngine executionEngine;
        private ResponseNormalizer responseNormalizer;
        private RetryManager retryManager;
        private TimeoutManager timeoutManager;
        private ErrorTranslator errorTranslator;
        private PipelineMetrics metrics;
        private PipelineAudit audit;
        private List<Middleware> customMiddlewares;

        public Builder requestValidator(RequestValidator v) { this.requestValidator = v; return this; }
        public Builder contextBuilder(ContextBuilder v) { this.contextBuilder = v; return this; }
        public Builder promptCompiler(PromptCompiler v) { this.promptCompiler = v; return this; }
        public Builder executionEngine(ExecutionEngine v) { this.executionEngine = v; return this; }
        public Builder responseNormalizer(ResponseNormalizer v) { this.responseNormalizer = v; return this; }
        public Builder retryManager(RetryManager v) { this.retryManager = v; return this; }
        public Builder timeoutManager(TimeoutManager v) { this.timeoutManager = v; return this; }
        public Builder errorTranslator(ErrorTranslator v) { this.errorTranslator = v; return this; }
        public Builder metrics(PipelineMetrics v) { this.metrics = v; return this; }
        public Builder audit(PipelineAudit v) { this.audit = v; return this; }
        public Builder middlewares(List<Middleware> v) { this.customMiddlewares = v; return this; }

        public PipelineOrchestrator build() {
            return new PipelineOrchestrator(
                    requestValidator != null ? requestValidator : new RequestValidator(),
                    contextBuilder != null ? contextBuilder : new ContextBuilder(),
                    promptCompiler != null ? promptCompiler : new PromptCompiler(),
                    executionEngine != null ? executionEngine : new ExecutionEngine(),
                    responseNormalizer != null ? responseNormalizer : new ResponseNormalizer(),
                    retryManager != null ? retryManager : new RetryManager(),
                    timeoutManager != null ? timeoutManager : new TimeoutManager(),
                    errorTranslator != null ? errorTranslator : new ErrorTranslator(),
                    metrics != null ? metrics : new PipelineMetrics(),
                    audit != null ? audit : new PipelineAudit());
        }
    }
}
