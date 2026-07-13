package com.sporekart.ai.compliance.infrastructure.kafka;

import com.sporekart.ai.compliance.engine.ComplianceRequest;
import com.sporekart.ai.compliance.engine.ComplianceResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ComplianceKafkaEventPublisher {

    private static final String TOPIC = "compliance-events";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publishValidationStarted(UUID frameworkId, ComplianceRequest request) {
        Map<String, Object> event = Map.of(
            "eventType", "ComplianceValidationStarted",
            "frameworkId", frameworkId.toString(),
            "module", request.module(),
            "action", request.action(),
            "userId", request.userId(),
            "timestamp", Instant.now().toString()
        );
        kafkaTemplate.send(TOPIC, event.get("eventType").toString(), event);
        log.info("Published ComplianceValidationStarted for framework {}", frameworkId);
    }

    public void publishValidated(UUID frameworkId, UUID assessmentId, ComplianceResult result) {
        Map<String, Object> event = Map.of(
            "eventType", "ComplianceValidated",
            "frameworkId", frameworkId.toString(),
            "assessmentId", assessmentId.toString(),
            "compliant", result.compliant(),
            "status", result.status().name(),
            "timestamp", Instant.now().toString()
        );
        kafkaTemplate.send(TOPIC, event.get("eventType").toString(), event);
        log.info("Published ComplianceValidated for assessment {}", assessmentId);

        if (result.compliant()) {
            publishPassed(frameworkId, assessmentId);
        } else {
            publishFailed(frameworkId, assessmentId, result);
        }
    }

    public void publishPassed(UUID frameworkId, UUID assessmentId) {
        Map<String, Object> event = Map.of(
            "eventType", "CompliancePassed",
            "frameworkId", frameworkId.toString(),
            "assessmentId", assessmentId.toString(),
            "timestamp", Instant.now().toString()
        );
        kafkaTemplate.send(TOPIC, event.get("eventType").toString(), event);
        log.info("Published CompliancePassed for assessment {}", assessmentId);
    }

    public void publishFailed(UUID frameworkId, UUID assessmentId, ComplianceResult result) {
        Map<String, Object> event = Map.of(
            "eventType", "ComplianceFailed",
            "frameworkId", frameworkId.toString(),
            "assessmentId", assessmentId.toString(),
            "violationsCount", result.violations() != null ? result.violations().size() : 0,
            "timestamp", Instant.now().toString()
        );
        kafkaTemplate.send(TOPIC, event.get("eventType").toString(), event);
        log.info("Published ComplianceFailed for assessment {}", assessmentId);
    }

    public void publishViolationDetected(UUID assessmentId, UUID ruleId, String description) {
        Map<String, Object> event = Map.of(
            "eventType", "ComplianceViolationDetected",
            "assessmentId", assessmentId.toString(),
            "ruleId", ruleId.toString(),
            "description", description,
            "timestamp", Instant.now().toString()
        );
        kafkaTemplate.send(TOPIC, event.get("eventType").toString(), event);
        log.info("Published ComplianceViolationDetected for assessment {} rule {}", assessmentId, ruleId);
    }

    public void publishReportGenerated(UUID assessmentId, UUID reportId) {
        Map<String, Object> event = Map.of(
            "eventType", "ComplianceReportGenerated",
            "assessmentId", assessmentId.toString(),
            "reportId", reportId.toString(),
            "timestamp", Instant.now().toString()
        );
        kafkaTemplate.send(TOPIC, event.get("eventType").toString(), event);
        log.info("Published ComplianceReportGenerated for assessment {} report {}", assessmentId, reportId);
    }

    public void publishExceptionCreated(UUID exceptionId, UUID ruleId, String reason) {
        Map<String, Object> event = Map.of(
            "eventType", "ComplianceExceptionCreated",
            "exceptionId", exceptionId.toString(),
            "ruleId", ruleId.toString(),
            "reason", reason,
            "timestamp", Instant.now().toString()
        );
        kafkaTemplate.send(TOPIC, event.get("eventType").toString(), event);
        log.info("Published ComplianceExceptionCreated for exception {}", exceptionId);
    }

    public void publishAuditRecorded(String action, String entityType, UUID entityId) {
        Map<String, Object> event = Map.of(
            "eventType", "ComplianceAuditRecorded",
            "action", action,
            "entityType", entityType,
            "entityId", entityId.toString(),
            "timestamp", Instant.now().toString()
        );
        kafkaTemplate.send(TOPIC, event.get("eventType").toString(), event);
        log.info("Published ComplianceAuditRecorded for {} {}", entityType, entityId);
    }
}
