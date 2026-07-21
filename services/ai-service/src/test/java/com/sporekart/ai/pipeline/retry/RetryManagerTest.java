package com.sporekart.ai.pipeline.retry;

import com.sporekart.ai.pipeline.PipelineContext;
import com.sporekart.ai.pipeline.PipelineException;
import com.sporekart.ai.pipeline.model.PipelineRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class RetryManagerTest {
    private PipelineContext context;

    @BeforeEach
    void setUp() {
        var request = PipelineRequest.builder()
                .requestId("r").tenantId("t").userId("u").module("m")
                .correlationId("c").context(Map.of("prompt", "Hi")).build();
        context = new PipelineContext(request);
    }

    @Test
    void shouldSucceedWithoutRetry() {
        var manager = new RetryManager(RetryPolicy.NO_RETRY);
        var result = manager.executeWithRetry(context, () -> "success");
        assertEquals("success", result);
    }

    @Test
    void shouldRetryOnFailure() {
        var manager = new RetryManager(new RetryPolicy(3,
                java.time.Duration.ofMillis(10),
                java.time.Duration.ofMillis(100), 2.0, true,
                java.util.Set.of(RuntimeException.class)));
        var attempts = new AtomicInteger(0);
        var result = manager.executeWithRetry(context, () -> {
            if (attempts.incrementAndGet() < 3) {
                throw new RuntimeException("Attempt " + attempts.get());
            }
            return "success";
        });
        assertEquals("success", result);
        assertEquals(3, attempts.get());
    }

    @Test
    void shouldThrowAfterExhaustingRetries() {
        var manager = new RetryManager(new RetryPolicy(2,
                java.time.Duration.ofMillis(5),
                java.time.Duration.ofMillis(50), 2.0, true,
                java.util.Set.of(RuntimeException.class)));
        assertThrows(PipelineException.class, () ->
                manager.executeWithRetry(context, () -> {
                    throw new RuntimeException("Always fails");
                }));
        assertEquals(2, context.retryCount());
    }

    @Test
    void shouldNotRetryNonRetryableExceptions() {
        var manager = new RetryManager(new RetryPolicy(3,
                java.time.Duration.ofMillis(10),
                java.time.Duration.ofMillis(100), 2.0, true,
                java.util.Set.of()));
        assertThrows(PipelineException.class, () ->
                manager.executeWithRetry(context, () -> {
                    throw new RuntimeException("Not retryable");
                }));
        assertEquals(1, context.retryCount());
    }

    @Test
    void retryPolicyShouldCalculateDelay() {
        var policy = new RetryPolicy(5,
                java.time.Duration.ofMillis(1000),
                java.time.Duration.ofSeconds(30),
                2.0, true, java.util.Set.of());
        assertEquals(1000, policy.calculateDelay(1).toMillis());
        assertEquals(2000, policy.calculateDelay(2).toMillis());
        assertEquals(4000, policy.calculateDelay(3).toMillis());
    }

    @Test
    void retryPolicyShouldCapDelay() {
        var policy = new RetryPolicy(10,
                java.time.Duration.ofMillis(1000),
                java.time.Duration.ofMillis(5000),
                3.0, true, java.util.Set.of());
        assertTrue(policy.calculateDelay(5).toMillis() <= 5000);
    }

    @Test
    void noRetryPolicyShouldNotRetry() {
        assertFalse(RetryPolicy.NO_RETRY.shouldRetry(0, null));
    }
}
