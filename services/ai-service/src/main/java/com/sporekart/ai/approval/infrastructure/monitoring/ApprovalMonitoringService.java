package com.sporekart.ai.approval.infrastructure.monitoring;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@Service
@Slf4j
public class ApprovalMonitoringService {

    private final MeterRegistry meterRegistry;

    private Counter totalRequests;
    private Counter approvedRequests;
    private Counter rejectedRequests;
    private Counter escalatedRequests;
    private Counter delegatedRequests;
    private Counter expiredRequests;
    private Timer reviewTime;
    private Gauge pendingGauge;

    public ApprovalMonitoringService(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    @PostConstruct
    public void init() {
        totalRequests = Counter.builder("approval.requests.total")
            .description("Total approval requests")
            .register(meterRegistry);

        approvedRequests = Counter.builder("approval.approved")
            .description("Approved approval requests")
            .register(meterRegistry);

        rejectedRequests = Counter.builder("approval.rejected")
            .description("Rejected approval requests")
            .register(meterRegistry);

        escalatedRequests = Counter.builder("approval.escalated")
            .description("Escalated approval requests")
            .register(meterRegistry);

        delegatedRequests = Counter.builder("approval.delegated")
            .description("Delegated approval requests")
            .register(meterRegistry);

        expiredRequests = Counter.builder("approval.expired")
            .description("Expired approval requests")
            .register(meterRegistry);

        reviewTime = Timer.builder("approval.review.time")
            .description("Approval review time")
            .register(meterRegistry);
    }

    public void registerPendingGauge(Supplier<Number> supplier) {
        pendingGauge = Gauge.builder("approval.pending.count", supplier)
            .description("Pending approval requests count")
            .register(meterRegistry);
    }

    public void incrementTotalRequests() {
        totalRequests.increment();
    }

    public void incrementApproved() {
        approvedRequests.increment();
    }

    public void incrementRejected() {
        rejectedRequests.increment();
    }

    public void incrementEscalated() {
        escalatedRequests.increment();
    }

    public void incrementDelegated() {
        delegatedRequests.increment();
    }

    public void incrementExpired() {
        expiredRequests.increment();
    }

    public void recordReviewTime(long timeMs) {
        reviewTime.record(timeMs, TimeUnit.MILLISECONDS);
    }
}
