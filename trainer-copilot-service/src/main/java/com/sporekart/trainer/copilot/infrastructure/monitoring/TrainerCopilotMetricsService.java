package com.sporekart.trainer.copilot.infrastructure.monitoring;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.DoubleAdder;
import org.springframework.stereotype.Component;

@Component
public class TrainerCopilotMetricsService {

    private final AtomicInteger trainingQueryCount = new AtomicInteger(0);
    private final AtomicLong trainingQueryTotalLatency = new AtomicLong(0);

    private final AtomicInteger lessonGenerationCount = new AtomicInteger(0);
    private final AtomicLong lessonGenerationTotalLatency = new AtomicLong(0);

    private final AtomicInteger assessmentGenerationCount = new AtomicInteger(0);
    private final AtomicLong assessmentGenerationTotalLatency = new AtomicLong(0);

    private final AtomicInteger knowledgeRetrievalCount = new AtomicInteger(0);
    private final AtomicLong knowledgeRetrievalTotalLatency = new AtomicLong(0);
    private final AtomicInteger totalResultsRetrieved = new AtomicInteger(0);

    private final AtomicInteger promptUsageCount = new AtomicInteger(0);
    private final AtomicLong totalPromptTokens = new AtomicLong(0);
    private final AtomicLong totalCompletionTokens = new AtomicLong(0);
    private final DoubleAdder totalCost = new DoubleAdder();

    private final ConcurrentHashMap<String, LatencyStats> operationLatencies = new ConcurrentHashMap<>();

    private static class LatencyStats {
        final AtomicLong totalDuration = new AtomicLong(0);
        final AtomicInteger count = new AtomicInteger(0);
        final AtomicLong minDuration = new AtomicLong(Long.MAX_VALUE);
        final AtomicLong maxDuration = new AtomicLong(Long.MIN_VALUE);

        void record(long durationMs) {
            totalDuration.addAndGet(durationMs);
            count.incrementAndGet();
            minDuration.updateAndGet(v -> Math.min(v, durationMs));
            maxDuration.updateAndGet(v -> Math.max(v, durationMs));
        }
    }

    public void recordQuery(String type, long durationMs) {
        trainingQueryCount.incrementAndGet();
        trainingQueryTotalLatency.addAndGet(durationMs);
    }

    public void recordLessonGeneration(String topic, long durationMs) {
        lessonGenerationCount.incrementAndGet();
        lessonGenerationTotalLatency.addAndGet(durationMs);
    }

    public void recordAssessmentGeneration(String type, String difficulty, long durationMs) {
        assessmentGenerationCount.incrementAndGet();
        assessmentGenerationTotalLatency.addAndGet(durationMs);
    }

    public void recordKnowledgeRetrieval(String query, int resultsCount, long durationMs) {
        knowledgeRetrievalCount.incrementAndGet();
        knowledgeRetrievalTotalLatency.addAndGet(durationMs);
        totalResultsRetrieved.addAndGet(resultsCount);
    }

    public void recordTokenUsage(String provider, int promptTokens, int completionTokens, double cost) {
        promptUsageCount.incrementAndGet();
        totalPromptTokens.addAndGet(promptTokens);
        totalCompletionTokens.addAndGet(completionTokens);
        totalCost.add(cost);
    }

    public void recordLatency(String operation, long durationMs) {
        operationLatencies.computeIfAbsent(operation, k -> new LatencyStats()).record(durationMs);
    }

    public Map<String, Object> getMetricsSummary() {
        Map<String, Object> summary = new HashMap<>();

        summary.put("trainingQueryCount", trainingQueryCount.get());
        summary.put("trainingQueryAvgLatencyMs", avg(trainingQueryCount.get(), trainingQueryTotalLatency.get()));

        summary.put("lessonGenerationCount", lessonGenerationCount.get());
        summary.put("lessonGenerationAvgLatencyMs", avg(lessonGenerationCount.get(), lessonGenerationTotalLatency.get()));

        summary.put("assessmentGenerationCount", assessmentGenerationCount.get());
        summary.put("assessmentGenerationAvgLatencyMs", avg(assessmentGenerationCount.get(), assessmentGenerationTotalLatency.get()));

        summary.put("knowledgeRetrievalCount", knowledgeRetrievalCount.get());
        summary.put("knowledgeRetrievalAvgLatencyMs", avg(knowledgeRetrievalCount.get(), knowledgeRetrievalTotalLatency.get()));
        summary.put("totalResultsRetrieved", totalResultsRetrieved.get());

        summary.put("promptUsageCount", promptUsageCount.get());
        summary.put("totalPromptTokens", totalPromptTokens.get());
        summary.put("totalCompletionTokens", totalCompletionTokens.get());
        summary.put("totalCost", totalCost.sum());

        Map<String, Object> opLatencies = new HashMap<>();
        for (Map.Entry<String, LatencyStats> entry : operationLatencies.entrySet()) {
            Map<String, Object> stats = new HashMap<>();
            LatencyStats ls = entry.getValue();
            stats.put("count", ls.count.get());
            stats.put("avgMs", avg(ls.count.get(), ls.totalDuration.get()));
            stats.put("minMs", ls.minDuration.get() == Long.MAX_VALUE ? 0 : ls.minDuration.get());
            stats.put("maxMs", ls.maxDuration.get() == Long.MIN_VALUE ? 0 : ls.maxDuration.get());
            opLatencies.put(entry.getKey(), stats);
        }
        summary.put("operationLatencies", opLatencies);

        return summary;
    }

    public void resetMetrics() {
        trainingQueryCount.set(0);
        trainingQueryTotalLatency.set(0);
        lessonGenerationCount.set(0);
        lessonGenerationTotalLatency.set(0);
        assessmentGenerationCount.set(0);
        assessmentGenerationTotalLatency.set(0);
        knowledgeRetrievalCount.set(0);
        knowledgeRetrievalTotalLatency.set(0);
        totalResultsRetrieved.set(0);
        promptUsageCount.set(0);
        totalPromptTokens.set(0);
        totalCompletionTokens.set(0);
        totalCost.reset();
        operationLatencies.clear();
    }

    private double avg(int count, long total) {
        return count == 0 ? 0.0 : (double) total / count;
    }
}
