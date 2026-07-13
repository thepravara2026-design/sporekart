package com.sporekart.ai.automation.application;

import com.sporekart.ai.automation.api.AutomationMetricsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Service
public class AutomationMetricsServiceImpl implements AutomationMetricsService {

    private final AtomicLong totalWorkflows = new AtomicLong(0);
    private final AtomicLong totalJobs = new AtomicLong(0);
    private final AtomicLong successfulJobs = new AtomicLong(0);
    private final AtomicLong failedJobs = new AtomicLong(0);
    private final AtomicLong retryCount = new AtomicLong(0);
    private final AtomicLong escalationCount = new AtomicLong(0);
    private final AtomicLong totalLatencyMs = new AtomicLong(0);
    private final AtomicLong latencySamples = new AtomicLong(0);

    public void recordWorkflow() {
        totalWorkflows.incrementAndGet();
    }

    public void recordJob(boolean success) {
        totalJobs.incrementAndGet();
        if (success) {
            successfulJobs.incrementAndGet();
        } else {
            failedJobs.incrementAndGet();
        }
    }

    public void recordRetry() {
        retryCount.incrementAndGet();
    }

    public void recordEscalation() {
        escalationCount.incrementAndGet();
    }

    public void recordLatency(long ms) {
        totalLatencyMs.addAndGet(ms);
        latencySamples.incrementAndGet();
    }

    @Override
    public long getTotalWorkflows() {
        return totalWorkflows.get();
    }

    @Override
    public long getTotalJobs() {
        return totalJobs.get();
    }

    @Override
    public double getJobSuccessRate() {
        var total = totalJobs.get();
        if (total == 0) {
            return 1.0;
        }
        return (double) successfulJobs.get() / total;
    }

    @Override
    public long getRetryCount() {
        return retryCount.get();
    }

    @Override
    public long getEscalationCount() {
        return escalationCount.get();
    }

    @Override
    public Map<String, Object> getStatistics() {
        var stats = new HashMap<String, Object>();
        stats.put("totalWorkflows", totalWorkflows.get());
        stats.put("totalJobs", totalJobs.get());
        stats.put("successfulJobs", successfulJobs.get());
        stats.put("failedJobs", failedJobs.get());
        stats.put("jobSuccessRate", getJobSuccessRate());
        stats.put("retryCount", retryCount.get());
        stats.put("escalationCount", escalationCount.get());
        stats.put("totalLatencyMs", totalLatencyMs.get());
        stats.put("latencySamples", latencySamples.get());
        stats.put("averageLatencyMs", latencySamples.get() > 0
            ? totalLatencyMs.get() / (double) latencySamples.get() : 0.0);
        return stats;
    }
}
