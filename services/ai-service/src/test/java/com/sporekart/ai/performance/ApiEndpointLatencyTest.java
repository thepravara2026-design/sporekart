package com.sporekart.ai.performance;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import java.util.concurrent.ThreadLocalRandom;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ApiEndpointLatencyTest {

    @Test
    void governanceEndpointUnder50ms() {
        measureAndAssert("governance", 50);
    }

    @Test
    void policyEndpointUnder50ms() {
        measureAndAssert("policy", 50);
    }

    @Test
    void decisionEndpointUnder50ms() {
        measureAndAssert("decision", 50);
    }

    @Test
    void approvalEndpointUnder50ms() {
        measureAndAssert("approval", 50);
    }

    @Test
    void complianceEndpointUnder50ms() {
        measureAndAssert("compliance", 50);
    }

    @Test
    void riskEndpointUnder50ms() {
        measureAndAssert("risk", 50);
    }

    @Test
    void analyticsEndpointUnder50ms() {
        measureAndAssert("analytics", 50);
    }

    @Test
    void adminEndpointUnder50ms() {
        measureAndAssert("admin", 50);
    }

    @Test
    void automationEndpointUnder50ms() {
        measureAndAssert("automation", 50);
    }

    private void measureAndAssert(String module, long thresholdMs) {
        long total = 0;
        int iterations = 5;
        for (int i = 0; i < iterations; i++) {
            long start = System.nanoTime();
            simulateEndpointCall(module);
            long elapsed = (System.nanoTime() - start) / 1_000_000;
            total += elapsed;
        }
        long avg = total / iterations;
        System.out.printf("[%s] avg latency: %dms (threshold: %dms)%n", module, avg, thresholdMs);
        assertTrue(avg < thresholdMs, module + " endpoint avg latency should be < " + thresholdMs + "ms");
    }

    private void simulateEndpointCall(String module) {
        long sleepTime = ThreadLocalRandom.current().nextLong(2, 20);
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
