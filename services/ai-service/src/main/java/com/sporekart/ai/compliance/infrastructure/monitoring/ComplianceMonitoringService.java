package com.sporekart.ai.compliance.infrastructure.monitoring;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
public class ComplianceMonitoringService {

    private final MeterRegistry meterRegistry;

    private Counter validationTotal;
    private Counter passedCounter;
    private Counter failedCounter;
    private Counter violationsCounter;
    private Timer assessmentLatency;
    private AtomicInteger pendingAssessments;

    public ComplianceMonitoringService(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    @PostConstruct
    public void init() {
        validationTotal = Counter.builder("compliance.validation.total")
            .description("Total number of validations performed")
            .register(meterRegistry);

        passedCounter = Counter.builder("compliance.passed")
            .description("Number of validations that passed")
            .register(meterRegistry);

        failedCounter = Counter.builder("compliance.failed")
            .description("Number of validations that failed")
            .register(meterRegistry);

        violationsCounter = Counter.builder("compliance.violations")
            .description("Total number of violations detected")
            .register(meterRegistry);

        assessmentLatency = Timer.builder("compliance.assessment.latency")
            .description("Latency of compliance assessments")
            .register(meterRegistry);

        pendingAssessments = new AtomicInteger(0);
        Gauge.builder("compliance.pending.assessments", pendingAssessments, AtomicInteger::get)
            .description("Number of pending assessments")
            .register(meterRegistry);

        log.info("Compliance monitoring metrics initialized");
    }

    public void recordValidation(boolean passed) {
        validationTotal.increment();
        if (passed) {
            passedCounter.increment();
        } else {
            failedCounter.increment();
        }
    }

    public void recordViolation() {
        violationsCounter.increment();
    }

    public void recordAssessmentLatency(long millis) {
        assessmentLatency.record(millis, TimeUnit.MILLISECONDS);
    }

    public void setPendingAssessments(int count) {
        pendingAssessments.set(count);
    }

    public void incrementPendingAssessments() {
        pendingAssessments.incrementAndGet();
    }

    public void decrementPendingAssessments() {
        pendingAssessments.decrementAndGet();
    }

    public double getValidationTotal() {
        return validationTotal.count();
    }

    public double getPassedCount() {
        return passedCounter.count();
    }

    public double getFailedCount() {
        return failedCounter.count();
    }

    public double getViolationsCount() {
        return violationsCounter.count();
    }

    public int getPendingAssessments() {
        return pendingAssessments.get();
    }
}
