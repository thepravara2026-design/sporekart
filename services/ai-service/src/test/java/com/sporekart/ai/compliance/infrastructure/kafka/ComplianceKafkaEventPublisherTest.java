package com.sporekart.ai.compliance.infrastructure.kafka;

import com.sporekart.ai.compliance.domain.ComplianceStatus;
import com.sporekart.ai.compliance.engine.ComplianceRequest;
import com.sporekart.ai.compliance.engine.ComplianceResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ComplianceKafkaEventPublisherTest {

    @Mock private KafkaTemplate<String, Object> kafkaTemplate;

    private ComplianceKafkaEventPublisher publisher;

    @BeforeEach
    void setUp() {
        publisher = new ComplianceKafkaEventPublisher(kafkaTemplate);
    }

    @Test
    void testPublishValidationStarted() {
        UUID frameworkId = UUID.randomUUID();
        ComplianceRequest request = new ComplianceRequest("module", "action", Map.of(), "user1", List.of("admin"));

        publisher.publishValidationStarted(frameworkId, request);

        verify(kafkaTemplate).send(eq("compliance-events"), eq("ComplianceValidationStarted"), anyMap());
    }

    @Test
    void testPublishValidated() {
        UUID frameworkId = UUID.randomUUID();
        UUID assessmentId = UUID.randomUUID();
        ComplianceResult result = new ComplianceResult(
            true, ComplianceStatus.PASSED, List.of(), List.of(),
            assessmentId, null, Map.of()
        );

        publisher.publishValidated(frameworkId, assessmentId, result);

        verify(kafkaTemplate).send(eq("compliance-events"), eq("ComplianceValidated"), anyMap());
    }

    @Test
    void testPublishPassed() {
        UUID frameworkId = UUID.randomUUID();
        UUID assessmentId = UUID.randomUUID();

        publisher.publishPassed(frameworkId, assessmentId);

        verify(kafkaTemplate).send(eq("compliance-events"), eq("CompliancePassed"), anyMap());
    }

    @Test
    void testPublishFailed() {
        UUID frameworkId = UUID.randomUUID();
        UUID assessmentId = UUID.randomUUID();
        ComplianceResult result = new ComplianceResult(
            false, ComplianceStatus.FAILED, List.of(), List.of(),
            assessmentId, null, Map.of()
        );

        publisher.publishFailed(frameworkId, assessmentId, result);

        verify(kafkaTemplate).send(eq("compliance-events"), eq("ComplianceFailed"), anyMap());
    }

    @Test
    void testPublishViolationDetected() {
        UUID assessmentId = UUID.randomUUID();
        UUID ruleId = UUID.randomUUID();

        publisher.publishViolationDetected(assessmentId, ruleId, "description");

        verify(kafkaTemplate).send(eq("compliance-events"), eq("ComplianceViolationDetected"), anyMap());
    }

    @Test
    void testPublishReportGenerated() {
        UUID assessmentId = UUID.randomUUID();
        UUID reportId = UUID.randomUUID();

        publisher.publishReportGenerated(assessmentId, reportId);

        verify(kafkaTemplate).send(eq("compliance-events"), eq("ComplianceReportGenerated"), anyMap());
    }

    @Test
    void testPublishExceptionCreated() {
        UUID exceptionId = UUID.randomUUID();
        UUID ruleId = UUID.randomUUID();

        publisher.publishExceptionCreated(exceptionId, ruleId, "reason");

        verify(kafkaTemplate).send(eq("compliance-events"), eq("ComplianceExceptionCreated"), anyMap());
    }

    @Test
    void testPublishAuditRecorded() {
        UUID entityId = UUID.randomUUID();

        publisher.publishAuditRecorded("ACTION", "ENTITY", entityId);

        verify(kafkaTemplate).send(eq("compliance-events"), eq("ComplianceAuditRecorded"), anyMap());
    }

    @Test
    void testPublishValidatedSendsPassedEventWhenCompliant() {
        UUID frameworkId = UUID.randomUUID();
        UUID assessmentId = UUID.randomUUID();
        ComplianceResult result = new ComplianceResult(
            true, ComplianceStatus.PASSED, List.of(), List.of(),
            assessmentId, null, Map.of()
        );

        publisher.publishValidated(frameworkId, assessmentId, result);

        verify(kafkaTemplate).send(eq("compliance-events"), eq("CompliancePassed"), anyMap());
    }

    @Test
    void testPublishValidatedSendsFailedEventWhenNotCompliant() {
        UUID frameworkId = UUID.randomUUID();
        UUID assessmentId = UUID.randomUUID();
        ComplianceResult result = new ComplianceResult(
            false, ComplianceStatus.FAILED, List.of(), List.of(),
            assessmentId, null, Map.of()
        );

        publisher.publishValidated(frameworkId, assessmentId, result);

        verify(kafkaTemplate).send(eq("compliance-events"), eq("ComplianceFailed"), anyMap());
    }
}
