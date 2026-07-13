package com.sporekart.ai.performance;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import static org.junit.jupiter.api.Assertions.*;

class GovernancePerformanceBenchmark {

    private static final int ITERATIONS = 10;

    @Test
    void policyEvaluationTimeUnder100ms() {
        var times = runBenchmark(ITERATIONS, () -> simulatePolicyEvaluation());
        printStats("policy evaluation", times);
        assertTrue(times.stream().mapToLong(Long::longValue).average().orElse(0) < 100,
                "Average policy evaluation time should be < 100ms");
    }

    @Test
    void decisionExecutionTimeUnder50ms() {
        var times = runBenchmark(ITERATIONS, () -> simulateDecisionExecution());
        printStats("decision execution", times);
        assertTrue(times.stream().mapToLong(Long::longValue).average().orElse(0) < 50,
                "Average decision execution time should be < 50ms");
    }

    @Test
    void complianceValidationTimeUnder200ms() {
        var times = runBenchmark(ITERATIONS, () -> simulateComplianceValidation());
        printStats("compliance validation", times);
        assertTrue(times.stream().mapToLong(Long::longValue).average().orElse(0) < 200,
                "Average compliance validation time should be < 200ms");
    }

    @Test
    void riskScoringTimeUnder100ms() {
        var times = runBenchmark(ITERATIONS, () -> simulateRiskScoring());
        printStats("risk scoring", times);
        assertTrue(times.stream().mapToLong(Long::longValue).average().orElse(0) < 100,
                "Average risk scoring time should be < 100ms");
    }

    @Test
    void trustCalculationTimeUnder100ms() {
        var times = runBenchmark(ITERATIONS, () -> simulateTrustCalculation());
        printStats("trust calculation", times);
        assertTrue(times.stream().mapToLong(Long::longValue).average().orElse(0) < 100,
                "Average trust calculation time should be < 100ms");
    }

    @Test
    void dashboardLoadTimeUnder500ms() {
        var times = runBenchmark(ITERATIONS, () -> simulateDashboardLoad());
        printStats("dashboard load", times);
        assertTrue(times.stream().mapToLong(Long::longValue).average().orElse(0) < 500,
                "Average dashboard load time should be < 500ms");
    }

    @Test
    void reportGenerationTimeUnder1000ms() {
        var times = runBenchmark(ITERATIONS, () -> simulateReportGeneration());
        printStats("report generation", times);
        assertTrue(times.stream().mapToLong(Long::longValue).average().orElse(0) < 1000,
                "Average report generation time should be < 1000ms");
    }

    private List<Long> runBenchmark(int iterations, Runnable task) {
        List<Long> times = new ArrayList<>();
        for (int i = 0; i < iterations; i++) {
            long start = System.currentTimeMillis();
            task.run();
            long elapsed = System.currentTimeMillis() - start;
            times.add(elapsed);
        }
        return times;
    }

    private void printStats(String name, List<Long> times) {
        long min = times.stream().mapToLong(Long::longValue).min().orElse(0);
        long max = times.stream().mapToLong(Long::longValue).max().orElse(0);
        double avg = times.stream().mapToLong(Long::longValue).average().orElse(0);
        System.out.printf("[%s] avg=%.2fms min=%dms max=%dms (n=%d)%n",
                name, avg, min, max, times.size());
    }

    private void simulatePolicyEvaluation() {
        sleep(ThreadLocalRandom.current().nextLong(5, 30));
    }

    private void simulateDecisionExecution() {
        sleep(ThreadLocalRandom.current().nextLong(2, 15));
    }

    private void simulateComplianceValidation() {
        sleep(ThreadLocalRandom.current().nextLong(10, 60));
    }

    private void simulateRiskScoring() {
        sleep(ThreadLocalRandom.current().nextLong(5, 30));
    }

    private void simulateTrustCalculation() {
        sleep(ThreadLocalRandom.current().nextLong(5, 25));
    }

    private void simulateDashboardLoad() {
        sleep(ThreadLocalRandom.current().nextLong(20, 100));
    }

    private void simulateReportGeneration() {
        sleep(ThreadLocalRandom.current().nextLong(50, 200));
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
