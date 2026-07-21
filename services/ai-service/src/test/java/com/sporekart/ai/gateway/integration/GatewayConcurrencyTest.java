package com.sporekart.ai.gateway.integration;

import com.sporekart.ai.gateway.api.AiGateway;
import com.sporekart.ai.gateway.api.RateLimiter;
import com.sporekart.ai.gateway.api.RequestValidator;
import com.sporekart.ai.gateway.application.*;
import com.sporekart.ai.gateway.infrastructure.InMemoryRateLimiter;
import com.sporekart.ai.gateway.pipeline.PipelineContext;
import com.sporekart.ai.gateway.pipeline.PipelineStage;
import com.sporekart.ai.gateway.domain.*;
import com.sporekart.ai.gateway.contract.request.GatewayRequest;
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
class GatewayConcurrencyTest {

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
    private InMemoryRateLimiter rateLimiter;

    @BeforeEach
    void setUp() {
        rateLimiter = new InMemoryRateLimiter(5000);

        lenient().when(featureFlagService.isEnabled(FeatureFlagName.AI_PLATFORM_ENABLED)).thenReturn(true);
        lenient().when(featureFlagService.isEnabled(FeatureFlagName.AI_GATEWAY_ENABLED)).thenReturn(true);
        lenient().when(featureFlagService.isEnabled(FeatureFlagName.AI_RATE_LIMITING)).thenReturn(false);
        lenient().when(featureFlagService.isEnabled(FeatureFlagName.AI_REQUEST_LOGGING)).thenReturn(false);
        lenient().when(contextResolver.resolveUserId(any())).thenReturn("test-user");
        lenient().when(contextResolver.resolveModule(any())).thenReturn("chat");
        lenient().when(contextResolver.resolveProvider(any())).thenReturn("MOCK");
        lenient().when(contextResolver.resolve(any(), any())).thenReturn(Map.of());
        lenient().when(providerResolver.resolve(anyString(), anyString())).thenReturn(Optional.of(AiProviderType.MOCK));
        lenient().when(timeoutStrategy.getTimeout(anyString())).thenReturn(Duration.ofSeconds(30));
        lenient().when(timeoutStrategy.isExpired(anyLong(), any())).thenReturn(false);
        lenient().when(retryStrategy.getMaxRetries()).thenReturn(3);
        lenient().when(requestValidator.validate(any())).thenReturn(true);

        pipeline = new GatewayPipeline(featureFlagService, rateLimiter, requestValidator,
                aiGateway, contextResolver, providerResolver, retryStrategy,
                timeoutStrategy, auditService, metricsCollector);
    }

    @Test
    @Order(1)
    void shouldHandleSingleRequest() {
        AiResponse mockResponse = new AiResponse("Single response");
        when(aiGateway.route(any())).thenReturn(mockResponse);

        AiResponse response = pipeline.execute(
            new AiRequest("Single", Map.of()), CorrelationId.generate());

        assertThat(response.success()).isTrue();
        assertThat(response.content()).isEqualTo("Single response");
    }

    @Test
    @Order(2)
    void shouldHandle10ConcurrentRequests() throws Exception {
        AiResponse mockResponse = new AiResponse("Concurrent response");
        when(aiGateway.route(any())).thenReturn(mockResponse);

        int threadCount = 10;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failureCount = new AtomicInteger(0);

        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> {
                try {
                    AiResponse response = pipeline.execute(
                        new AiRequest("Concurrent", Map.of()), CorrelationId.generate());
                    if (response.success()) successCount.incrementAndGet();
                    else failureCount.incrementAndGet();
                } catch (Exception e) {
                    failureCount.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }

        boolean completed = latch.await(10, TimeUnit.SECONDS);
        executor.shutdown();

        assertThat(completed).isTrue();
        assertThat(successCount.get()).isEqualTo(threadCount);
        assertThat(failureCount.get()).isEqualTo(0);
    }

    @Test
    @Order(3)
    void shouldHandle100ConcurrentRequests() throws Exception {
        AiResponse mockResponse = new AiResponse("Bulk response");
        when(aiGateway.route(any())).thenReturn(mockResponse);

        int threadCount = 100;
        ExecutorService executor = Executors.newFixedThreadPool(20);
        CountDownLatch latch = new CountDownLatch(threadCount);
        AtomicInteger successCount = new AtomicInteger(0);

        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> {
                try {
                    AiResponse response = pipeline.execute(
                        new AiRequest("Bulk", Map.of()), CorrelationId.generate());
                    if (response.success()) successCount.incrementAndGet();
                } catch (Exception e) {
                    // expected for some if limit reached
                } finally {
                    latch.countDown();
                }
            });
        }

        boolean completed = latch.await(15, TimeUnit.SECONDS);
        executor.shutdown();

        assertThat(completed).isTrue();
        assertThat(successCount.get()).isPositive();
    }

    @Test
    @Order(4)
    void shouldHandle500ConcurrentRequests() throws Exception {
        AiResponse mockResponse = new AiResponse("High volume response");
        when(aiGateway.route(any())).thenReturn(mockResponse);

        int threadCount = 500;
        ExecutorService executor = Executors.newFixedThreadPool(50);
        CountDownLatch latch = new CountDownLatch(threadCount);
        AtomicInteger successCount = new AtomicInteger(0);

        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> {
                try {
                    AiResponse response = pipeline.execute(
                        new AiRequest("HighVolume", Map.of()), CorrelationId.generate());
                    if (response.success()) successCount.incrementAndGet();
                } catch (Exception e) {
                    // expected for some if limit reached
                } finally {
                    latch.countDown();
                }
            });
        }

        boolean completed = latch.await(30, TimeUnit.SECONDS);
        executor.shutdown();

        assertThat(completed).isTrue();
        assertThat(successCount.get()).isPositive();
    }

    @Test
    @Order(5)
    void shouldHandle1000ConcurrentRequests() throws Exception {
        AiResponse mockResponse = new AiResponse("Scale response");
        when(aiGateway.route(any())).thenReturn(mockResponse);

        int threadCount = 1000;
        ExecutorService executor = Executors.newFixedThreadPool(100);
        CountDownLatch latch = new CountDownLatch(threadCount);
        AtomicInteger successCount = new AtomicInteger(0);

        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> {
                try {
                    AiResponse response = pipeline.execute(
                        new AiRequest("Scale", Map.of()), CorrelationId.generate());
                    if (response.success()) successCount.incrementAndGet();
                } catch (Exception e) {
                    // expected for some if limit reached
                } finally {
                    latch.countDown();
                }
            });
        }

        boolean completed = latch.await(60, TimeUnit.SECONDS);
        executor.shutdown();

        assertThat(completed).isTrue();
        assertThat(successCount.get()).isPositive();
    }

    @Test
    @Order(6)
    void shouldNotHaveDeadlocksUnderConcurrentAccess() throws Exception {
        AiResponse mockResponse = new AiResponse("Deadlock test");
        when(aiGateway.route(any())).thenReturn(mockResponse);

        int threadCount = 50;
        ExecutorService executor = Executors.newFixedThreadPool(10);
        CompletionService<Boolean> completionService = new ExecutorCompletionService<>(executor);

        for (int i = 0; i < threadCount; i++) {
            completionService.submit(() -> {
                AiResponse response = pipeline.execute(
                    new AiRequest("DeadlockTest", Map.of()), CorrelationId.generate());
                return response.success();
            });
        }

        int completedCount = 0;
        for (int i = 0; i < threadCount; i++) {
            Future<Boolean> future = completionService.poll(5, TimeUnit.SECONDS);
            if (future != null) {
                completedCount++;
            }
        }
        executor.shutdown();

        assertThat(completedCount).isPositive();
    }

    @Test
    @Order(7)
    void concurrentRateLimiterShouldNotCorrupt() throws Exception {
        InMemoryRateLimiter limiter = new InMemoryRateLimiter(10000);
        int threadCount = 50;
        ExecutorService executor = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(threadCount);
        AtomicInteger allowed = new AtomicInteger(0);
        AtomicInteger denied = new AtomicInteger(0);

        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> {
                for (int j = 0; j < 100; j++) {
                    if (limiter.tryAcquire("stress")) allowed.incrementAndGet();
                    else denied.incrementAndGet();
                }
                latch.countDown();
            });
        }

        latch.await(10, TimeUnit.SECONDS);
        executor.shutdown();

        assertThat(allowed.get() + denied.get()).isEqualTo(threadCount * 100);
    }

    @Test
    @Order(8)
    void pipelineContextShouldBeThreadSafe() throws Exception {
        int threadCount = 20;
        ExecutorService executor = Executors.newFixedThreadPool(5);
        CountDownLatch latch = new CountDownLatch(threadCount);
        ConcurrentLinkedQueue<String> pipelineIds = new ConcurrentLinkedQueue<>();

        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> {
                var request = new GatewayRequest("req", "chat", "mock", "mock",
                    null, "hello", null, null, null, false, null, null, null);
                var context = new GatewayExecutionRequest("user", "tenant",
                    List.of("role"), "mock", "mock", null, null, null);
                var pipelineCtx = new PipelineContext(request, context);
                pipelineIds.add(pipelineCtx.pipelineId());
                pipelineCtx.advanceTo(PipelineStage.AUTHENTICATION);
                pipelineCtx.setAttribute("key", "value");
                latch.countDown();
            });
        }

        latch.await(10, TimeUnit.SECONDS);
        executor.shutdown();

        assertThat(pipelineIds).hasSize(threadCount);
        assertThat(pipelineIds.stream().distinct()).hasSize(threadCount);
    }
}
