package com.sporekart.ai.compliance.application;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Service
public class ComplianceMetricsServiceImpl implements ComplianceMetricsService {

    private final AtomicLong totalValidations = new AtomicLong(0);
    private final AtomicLong passCount = new AtomicLong(0);
    private final AtomicLong failureCount = new AtomicLong(0);
    private final AtomicLong violationCount = new AtomicLong(0);
    private final AtomicLong totalAssessmentLatencyMs = new AtomicLong(0);
    private final AtomicLong assessmentCount = new AtomicLong(0);

    @Override
    public void recordValidation(boolean passed) {
        totalValidations.incrementAndGet();
        if (passed) {
            passCount.incrementAndGet();
        } else {
            failureCount.incrementAndGet();
        }
    }

    @Override
    public void recordViolation() {
        violationCount.incrementAndGet();
    }

    @Override
    public void recordAssessmentLatency(long ms) {
        totalAssessmentLatencyMs.addAndGet(ms);
        assessmentCount.incrementAndGet();
    }

    @Override
    public long getTotalValidations() {
        return totalValidations.get();
    }

    @Override
    public long getPassCount() {
        return passCount.get();
    }

    @Override
    public long getFailureCount() {
        return failureCount.get();
    }

    @Override
    public long getViolationCount() {
        return violationCount.get();
    }

    @Override
    public double getPassRate() {
        long total = totalValidations.get();
        if (total == 0) {
            return 0.0;
        }
        return (double) passCount.get() / total;
    }

    @Override
    public double getAverageAssessmentLatencyMs() {
        long count = assessmentCount.get();
        if (count == 0) {
            return 0.0;
        }
        return (double) totalAssessmentLatencyMs.get() / count;
    }

    @Override
    public Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalValidations", totalValidations.get());
        stats.put("passCount", passCount.get());
        stats.put("failureCount", failureCount.get());
        stats.put("violationCount", violationCount.get());
        stats.put("passRate", getPassRate());
        stats.put("averageAssessmentLatencyMs", getAverageAssessmentLatencyMs());
        stats.put("assessmentCount", assessmentCount.get());
        return stats;
    }
}
