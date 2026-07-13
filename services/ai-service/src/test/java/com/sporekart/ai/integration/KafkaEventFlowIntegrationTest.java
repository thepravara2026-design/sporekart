package com.sporekart.ai.integration;

import com.sporekart.ai.governance.infrastructure.kafka.GovernanceKafkaEventPublisher;
import com.sporekart.ai.policy.infrastructure.kafka.PolicyKafkaEventPublisher;
import com.sporekart.ai.decision.infrastructure.kafka.DecisionKafkaEventPublisher;
import com.sporekart.ai.approval.infrastructure.kafka.ApprovalKafkaEventPublisher;
import com.sporekart.ai.approval.domain.ApprovalRequest;
import com.sporekart.ai.approval.domain.ApprovalStatus;
import com.sporekart.ai.compliance.infrastructure.kafka.ComplianceKafkaEventPublisher;
import com.sporekart.ai.risk.infrastructure.kafka.RiskKafkaEventPublisher;
import com.sporekart.ai.analytics.infrastructure.kafka.AnalyticsKafkaEventPublisher;
import com.sporekart.ai.admin.infrastructure.kafka.AdminKafkaEventPublisher;
import com.sporekart.ai.automation.infrastructure.kafka.AutomationKafkaEventPublisher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KafkaEventFlowIntegrationTest {

    @Mock private KafkaTemplate<String, String> kafkaStringTemplate;
    @Mock private KafkaTemplate<String, Object> kafkaObjectTemplate;

    @Captor private ArgumentCaptor<String> topicCaptor;
    @Captor private ArgumentCaptor<String> keyCaptor;
    @Captor private ArgumentCaptor<String> messageCaptor;

    @Test
    void testAllGovernanceModulesPublishEvents() {
        GovernanceKafkaEventPublisher governancePublisher = new GovernanceKafkaEventPublisher(
            kafkaStringTemplate, new com.fasterxml.jackson.databind.ObjectMapper()
        );
        PolicyKafkaEventPublisher policyPublisher = new PolicyKafkaEventPublisher(
            kafkaStringTemplate, new com.fasterxml.jackson.databind.ObjectMapper()
        );
        DecisionKafkaEventPublisher decisionPublisher = new DecisionKafkaEventPublisher(
            kafkaStringTemplate, new com.fasterxml.jackson.databind.ObjectMapper()
        );
        ComplianceKafkaEventPublisher compliancePublisher = new ComplianceKafkaEventPublisher(
            kafkaObjectTemplate
        );
        RiskKafkaEventPublisher riskPublisher = new RiskKafkaEventPublisher(
            kafkaObjectTemplate
        );
        AnalyticsKafkaEventPublisher analyticsPublisher = new AnalyticsKafkaEventPublisher(
            kafkaStringTemplate, new com.fasterxml.jackson.databind.ObjectMapper()
        );
        AdminKafkaEventPublisher adminPublisher = new AdminKafkaEventPublisher(
            kafkaStringTemplate, new com.fasterxml.jackson.databind.ObjectMapper()
        );
        AutomationKafkaEventPublisher automationPublisher = new AutomationKafkaEventPublisher(
            kafkaStringTemplate, new com.fasterxml.jackson.databind.ObjectMapper()
        );

        governancePublisher.publishValidated("req-1", "ALLOW", 15L);
        verify(kafkaStringTemplate).send(eq("governance-events"), anyString(), anyString());

        policyPublisher.publishEvaluated("req-1", "ALLOW", 10L);
        verify(kafkaStringTemplate).send(eq("policy-events"), anyString(), anyString());

        decisionPublisher.publishEvaluated("req-1", "ALLOW", "ALLOWED", 8L);
        verify(kafkaStringTemplate, atLeast(2)).send(anyString(), anyString(), anyString());

        compliancePublisher.publishValidationStarted(
            UUID.randomUUID(),
            new com.sporekart.ai.compliance.engine.ComplianceRequest(
                "catalog", "create", Map.of(), "user1", List.of("admin")
            )
        );
        verify(kafkaObjectTemplate).send(eq("compliance-events"), anyString(), any());

        riskPublisher.publishRiskAssessmentStarted("assess-1", "catalog", "create");
        verify(kafkaObjectTemplate, atLeast(2)).send(anyString(), anyString(), any());

        analyticsPublisher.publishGovernanceMetricsUpdated("catalog", 5);
        verify(kafkaStringTemplate, atLeast(3)).send(anyString(), anyString(), anyString());

        adminPublisher.publishConfigurationUpdated(
            UUID.randomUUID(), "pricing.config", "catalog", "production"
        );
        verify(kafkaStringTemplate, atLeast(4)).send(anyString(), anyString(), anyString());

        automationPublisher.publishJobScheduled(
            UUID.randomUUID(), "daily-compliance-scan"
        );
        verify(kafkaStringTemplate, atLeast(5)).send(anyString(), anyString(), anyString());
    }

    @Test
    void testAllNineTopicsExist() {
        GovernanceKafkaEventPublisher governancePublisher = new GovernanceKafkaEventPublisher(
            kafkaStringTemplate, new com.fasterxml.jackson.databind.ObjectMapper()
        );
        PolicyKafkaEventPublisher policyPublisher = new PolicyKafkaEventPublisher(
            kafkaStringTemplate, new com.fasterxml.jackson.databind.ObjectMapper()
        );
        DecisionKafkaEventPublisher decisionPublisher = new DecisionKafkaEventPublisher(
            kafkaStringTemplate, new com.fasterxml.jackson.databind.ObjectMapper()
        );
        ComplianceKafkaEventPublisher compliancePublisher = new ComplianceKafkaEventPublisher(
            kafkaObjectTemplate
        );
        RiskKafkaEventPublisher riskPublisher = new RiskKafkaEventPublisher(
            kafkaObjectTemplate
        );
        AnalyticsKafkaEventPublisher analyticsPublisher = new AnalyticsKafkaEventPublisher(
            kafkaStringTemplate, new com.fasterxml.jackson.databind.ObjectMapper()
        );
        AdminKafkaEventPublisher adminPublisher = new AdminKafkaEventPublisher(
            kafkaStringTemplate, new com.fasterxml.jackson.databind.ObjectMapper()
        );
        AutomationKafkaEventPublisher automationPublisher = new AutomationKafkaEventPublisher(
            kafkaStringTemplate, new com.fasterxml.jackson.databind.ObjectMapper()
        );
        ApprovalKafkaEventPublisher approvalPublisher = new ApprovalKafkaEventPublisher(
            kafkaObjectTemplate
        );

        governancePublisher.publishInitialized(Map.of("status", "ok"));
        verify(kafkaStringTemplate).send(eq("governance-events"), anyString(), anyString());

        policyPublisher.publishCreated(UUID.randomUUID().toString(), "test-policy");
        verify(kafkaStringTemplate).send(eq("policy-events"), anyString(), anyString());

        decisionPublisher.publishAllowed("req-1");
        verify(kafkaStringTemplate, atLeast(2)).send(eq("decision-events"), anyString(), anyString());

        compliancePublisher.publishPassed(UUID.randomUUID(), UUID.randomUUID());
        verify(kafkaObjectTemplate).send(eq("compliance-events"), anyString(), any());

        riskPublisher.publishTrustScoreCalculated("assess-1", 0.95);
        verify(kafkaObjectTemplate).send(eq("risk-events"), anyString(), any());

        analyticsPublisher.publishGovernanceMetricsUpdated("catalog", 10);
        verify(kafkaStringTemplate, atLeast(3)).send(eq("analytics-events"), anyString(), anyString());

        adminPublisher.publishConfigurationUpdated(
            UUID.randomUUID(), "test.key", "test", "dev"
        );
        verify(kafkaStringTemplate, atLeast(3)).send(eq("admin-events"), anyString(), anyString());

        approvalPublisher.publishApprovalRequested(new ApprovalRequest(
            UUID.randomUUID(), "catalog", "create", Map.of(), Map.of(),
            "user1", List.of("admin"), "test", "low",
            UUID.randomUUID(), Map.of(), OffsetDateTime.now().plusDays(1),
            ApprovalStatus.PENDING, OffsetDateTime.now()
        ));
        verify(kafkaObjectTemplate).send(eq("approval-events"), anyString(), any());

        automationPublisher.publishWorkflowStarted(UUID.randomUUID(), "test-workflow");
        verify(kafkaStringTemplate, atLeast(4)).send(eq("automation-events"), anyString(), anyString());

        verify(kafkaStringTemplate, atLeast(4)).send(anyString(), anyString(), anyString());
        verify(kafkaObjectTemplate, times(3)).send(anyString(), anyString(), any());
    }
}
