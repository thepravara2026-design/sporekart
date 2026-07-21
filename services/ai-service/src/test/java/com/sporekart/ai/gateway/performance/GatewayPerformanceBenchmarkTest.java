package com.sporekart.ai.gateway.performance;

import com.sporekart.ai.gateway.api.AiGateway;
import com.sporekart.ai.gateway.api.RequestValidator;
import com.sporekart.ai.gateway.application.*;
import com.sporekart.ai.gateway.config.GatewayConfigProperties;
import com.sporekart.ai.gateway.domain.*;
import com.sporekart.ai.gateway.infrastructure.DefaultRetryStrategy;
import com.sporekart.ai.gateway.infrastructure.InMemoryRateLimiter;
import com.sporekart.ai.gateway.pipeline.PipelineContext;
import com.sporekart.ai.gateway.pipeline.PipelineStage;
import com.sporekart.ai.gateway.contract.request.GatewayRequest;
import com.sporekart.ai.gateway.contract.response.GatewayResponse;
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
class GatewayPerformanceBenchmarkTest {

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

    private static final int WARMUP_ITERATIONS = 100;
    private static final int BENCHMARK_ITERATIONS = 1000;
    private static final Duration BENCHMARK_TIMEOUT = Duration.ofMinutes(2);

    @BeforeEach
    void setUp() {
        lenient().when(featureFlagService.isEnabled(FeatureFlagName.AI_PLATFORM_ENABLED)).thenReturn(true);
        lenient().when(featureFlagService.isEnabled(FeatureFlagName.AI_GATEWAY_ENABLED)).thenReturn(true);
        lenient().when(featureFlagService.isEnabled(FeatureFlagName.AI_RATE_LIMITING)).thenReturn(false);
        lenient().when(featureFlagService.isEnabled(FeatureFlagName.AI_REQUEST_LOGGING)).thenReturn(false);
        lenient().when(contextResolver.resolveUserId(any())).thenReturn("bench-user");
        lenient().when(contextResolver.resolveModule(any())).thenReturn("chat");
        lenient().when(contextResolver.resolveProvider(any())).thenReturn("MOCK");
        lenient().when(contextResolver.resolve(any(), any())).thenReturn(Map.of());
        lenient().when(providerResolver.resolve(anyString(), anyString())).thenReturn(Optional.of(AiProviderType.MOCK));
        lenient().when(timeoutStrategy.getTimeout(anyString())).thenReturn(Duration.ofSeconds(30));
        lenient().when(timeoutStrategy.isExpired(anyLong(), any())).thenReturn(false);
        lenient().when(retryStrategy.getMaxRetries()).thenReturn(3);
        lenient().when(requestValidator.validate(any())).thenReturn(true);

        pipeline = new GatewayPipeline(featureFlagService, new InMemoryRateLimiter(10000),
                requestValidator, aiGateway, contextResolver, providerResolver, retryStrategy,
                timeoutStrategy, auditService, metricsCollector);
    }

    @Test
    @Order(1)
    @Tag("benchmark")
    void benchmarkGatewayOverhead() {
        AiResponse mockResponse = new AiResponse("benchmark");
        when(aiGateway.route(any())).thenReturn(mockResponse);

        warmup();

        List<Long> latencies = new ArrayList<>();
        AiRequest request = new AiRequest("Benchmark request", Map.of());

        for (int i = 0; i < BENCHMARK_ITERATIONS; i++) {
            long start = System.nanoTime();
            pipeline.execute(request, CorrelationId.generate());
            long elapsed = System.nanoTime() - start;
            latencies.add(elapsed);
        }

        DoubleSummaryStatistics stats = latencies.stream()
            .mapToLong(Long::longValue)
            .summaryStatistics();

        double avgMicroseconds = stats.getAverage() / 1000.0;
        double p99 = calculatePercentile(latencies, 99) / 1000.0;
        double p95 = calculatePercentile(latencies, 95) / 1000.0;

        System.out.println("=== Gateway Performance Benchmark ===");
        System.out.println("Total iterations: " + BENCHMARK_ITERATIONS);
        System.out.println("Average latency: " + String.format("%.2f", avgMicroseconds) + " µs");
        System.out.println("P95 latency: " + String.format("%.2f", p95) + " µs");
        System.out.println("P99 latency: " + String.format("%.2f", p99) + " µs");
        System.out.println("Min latency: " + (stats.getMin() / 1000.0) + " µs");
        System.out.println("Max latency: " + (stats.getMax() / 1000.0) + " µs");
        System.out.println("Throughput: " + String.format("%.0f", BENCHMARK_ITERATIONS / (stats.getSum() / 1_000_000_000.0)) + " req/s");

        assertThat(avgMicroseconds).isLessThan(50_000);
    }

    @Test
    @Order(2)
    @Tag("benchmark")
    void benchmarkSerializationOverhead() {
        GatewayRequest request = new GatewayRequest("req-1", "chat", "openai", "gpt-4",
            "user-1", "Hello world this is a benchmark test prompt for serialization",
            List.of(), Map.of(), Map.of(), false, List.of(), "tenant-1", List.of("role"));

        GatewayResponse response = GatewayResponse.ok("req-1", "openai", "gpt-4",
            List.of("This is a benchmark response text for testing serialization performance"));

        warmup();

        List<Long> serializationTimes = new ArrayList<>();
        List<Long> deserializationTimes = new ArrayList<>();

        for (int i = 0; i < BENCHMARK_ITERATIONS; i++) {
            long s1 = System.nanoTime();
            String serialized = request.toString() + "|" + response.toString();
            long e1 = System.nanoTime() - s1;
            serializationTimes.add(e1);

            long s2 = System.nanoTime();
            @SuppressWarnings("unused")
            boolean parsed = serialized.contains("Hello");
            long e2 = System.nanoTime() - s2;
            deserializationTimes.add(e2);
        }

        double avgSerialization = serializationTimes.stream().mapToLong(Long::longValue).average().orElse(0) / 1000.0;
        double avgDeserialization = deserializationTimes.stream().mapToLong(Long::longValue).average().orElse(0) / 1000.0;

        System.out.println("=== Serialization Benchmark ===");
        System.out.println("Average serialization: " + String.format("%.2f", avgSerialization) + " µs");
        System.out.println("Average deserialization: " + String.format("%.2f", avgDeserialization) + " µs");
    }

    @Test
    @Order(3)
    @Tag("benchmark")
    void benchmarkProviderRouting() {
        long start = System.nanoTime();
        int routingDecisions = 10000;
        for (int i = 0; i < routingDecisions; i++) {
            new RoutingDecision("provider-" + (i % 7), "model-" + (i % 3),
                "round-robin", "benchmark", 1, false, true);
        }
        long elapsed = System.nanoTime() - start;
        double avgNs = (double) elapsed / routingDecisions;

        System.out.println("=== Provider Routing Benchmark ===");
        System.out.println("Routing decisions: " + routingDecisions);
        System.out.println("Average per decision: " + String.format("%.2f", avgNs) + " ns");
        System.out.println("Total time: " + (elapsed / 1000) + " µs");

        assertThat(avgNs).isLessThan(10_000);
    }

    @Test
    @Order(4)
    @Tag("benchmark")
    void benchmarkMemoryAllocation() {
        Runtime runtime = Runtime.getRuntime();
        System.gc();
        long beforeMemory = runtime.totalMemory() - runtime.freeMemory();

        List<AIExecutionResponse> responses = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            responses.add(AIExecutionResponse.success(
                "req-" + i, "corr-" + i, "content-" + i,
                "openai", "gpt-4", i));
        }

        long afterMemory = runtime.totalMemory() - runtime.freeMemory();
        long memoryPerResponse = (afterMemory - beforeMemory) / responses.size();

        System.out.println("=== Memory Allocation Benchmark ===");
        System.out.println("Responses created: " + responses.size());
        System.out.println("Memory used: " + (afterMemory - beforeMemory) / 1024 + " KB");
        System.out.println("Memory per response: " + memoryPerResponse + " bytes");

        assertThat(memoryPerResponse).isLessThan(10_000);
    }

    @Test
    @Order(5)
    @Tag("benchmark")
    void benchmarkPipelineStageExecution() {
        AiResponse mockResponse = new AiResponse("stages");
        when(aiGateway.route(any())).thenReturn(mockResponse);

        warmup();

        List<Long> stageTimes = new ArrayList<>();
        for (PipelineStage stage : PipelineStage.values()) {
            long start = System.nanoTime();
            for (int i = 0; i < 1000; i++) {
                new PipelineContext(
                    new GatewayRequest("req", "chat", "mock", "mock",
                        null, "test", null, null, null, false, null, null, null),
                    new GatewayExecutionRequest("user", "tenant", List.of("role"),
                        "mock", "mock", null, null, null))
                    .advanceTo(stage);
            }
            long elapsed = System.nanoTime() - start;
            stageTimes.add(elapsed / 1000);
        }

        System.out.println("=== Pipeline Stage Benchmark ===");
        for (int i = 0; i < PipelineStage.values().length; i++) {
            System.out.println(PipelineStage.values()[i].name() + ": " + stageTimes.get(i) + " µs (1000 iterations)");
        }
    }

    private void warmup() {
        for (int i = 0; i < WARMUP_ITERATIONS; i++) {
            AiResponse resp = new AiResponse("warmup-" + i);
            when(aiGateway.route(any())).thenReturn(resp);
            pipeline.execute(new AiRequest("warmup", Map.of()), CorrelationId.generate());
        }
    }

    private double calculatePercentile(List<Long> sortedLatencies, int percentile) {
        List<Long> sorted = new ArrayList<>(sortedLatencies);
        Collections.sort(sorted);
        int index = (int) Math.ceil(percentile / 100.0 * sorted.size());
        return sorted.get(Math.min(index, sorted.size() - 1));
    }
}
