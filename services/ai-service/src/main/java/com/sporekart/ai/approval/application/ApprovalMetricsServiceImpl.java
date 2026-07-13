package com.sporekart.ai.approval.application;

import com.sporekart.ai.approval.api.ApprovalMetricsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

@Service
@Slf4j
public class ApprovalMetricsServiceImpl implements ApprovalMetricsService {

    private final AtomicLong totalRequests = new AtomicLong(0);
    private final AtomicLong pendingCount = new AtomicLong(0);
    private final AtomicLong approvedCount = new AtomicLong(0);
    private final AtomicLong rejectedCount = new AtomicLong(0);
    private final AtomicLong escalatedCount = new AtomicLong(0);
    private final AtomicLong delegatedCount = new AtomicLong(0);
    private final AtomicLong expiredCount = new AtomicLong(0);
    private final AtomicLong totalReviewTimeMs = new AtomicLong(0);
    private final AtomicLong reviewCount = new AtomicLong(0);

    @Override
    public void recordSubmission() {
        totalRequests.incrementAndGet();
        pendingCount.incrementAndGet();
        log.debug("Metric: submission recorded");
    }

    @Override
    public void recordApproval(long timeMs) {
        approvedCount.incrementAndGet();
        pendingCount.decrementAndGet();
        totalReviewTimeMs.addAndGet(timeMs);
        reviewCount.incrementAndGet();
        log.debug("Metric: approval recorded ({} ms)", timeMs);
    }

    @Override
    public void recordRejection(long timeMs) {
        rejectedCount.incrementAndGet();
        pendingCount.decrementAndGet();
        totalReviewTimeMs.addAndGet(timeMs);
        reviewCount.incrementAndGet();
        log.debug("Metric: rejection recorded ({} ms)", timeMs);
    }

    @Override
    public void recordEscalation() {
        escalatedCount.incrementAndGet();
        log.debug("Metric: escalation recorded");
    }

    @Override
    public void recordDelegation() {
        delegatedCount.incrementAndGet();
        log.debug("Metric: delegation recorded");
    }

    @Override
    public void recordExpiration() {
        expiredCount.incrementAndGet();
        pendingCount.decrementAndGet();
        log.debug("Metric: expiration recorded");
    }

    @Override
    public long getTotalRequests() {
        return totalRequests.get();
    }

    @Override
    public long getPendingCount() {
        return pendingCount.get();
    }

    @Override
    public long getApprovedCount() {
        return approvedCount.get();
    }

    @Override
    public long getRejectedCount() {
        return rejectedCount.get();
    }

    @Override
    public double getAverageReviewTimeMs() {
        long count = reviewCount.get();
        return count > 0 ? (double) totalReviewTimeMs.get() / count : 0.0;
    }

    @Override
    public Map<String, Object> getStatistics() {
        return Map.of(
            "totalRequests", totalRequests.get(),
            "pendingCount", pendingCount.get(),
            "approvedCount", approvedCount.get(),
            "rejectedCount", rejectedCount.get(),
            "escalatedCount", escalatedCount.get(),
            "delegatedCount", delegatedCount.get(),
            "expiredCount", expiredCount.get(),
            "averageReviewTimeMs", getAverageReviewTimeMs()
        );
    }
}
