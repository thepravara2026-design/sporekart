package com.sporekart.ai.integration;

import com.sporekart.ai.governance.api.GovernanceEngine;
import com.sporekart.ai.governance.domain.GovernanceDecision;
import com.sporekart.ai.governance.domain.GovernanceRequest;
import com.sporekart.ai.governance.domain.GovernanceResponse;
import com.sporekart.ai.policy.api.PolicyEngine;
import com.sporekart.ai.policy.domain.EvaluationRequest;
import com.sporekart.ai.policy.domain.EvaluationResult;
import com.sporekart.ai.policy.domain.PolicyDecision;
import com.sporekart.ai.decision.api.DecisionEngine;
import com.sporekart.ai.decision.domain.DecisionAction;
import com.sporekart.ai.decision.domain.DecisionConfidence;
import com.sporekart.ai.decision.domain.DecisionRequest;
import com.sporekart.ai.decision.domain.DecisionResult;
import com.sporekart.ai.decision.domain.DecisionStatus;
import com.sporekart.ai.approval.api.ApprovalEngine;
import com.sporekart.ai.approval.domain.ApprovalRequest;
import com.sporekart.ai.approval.domain.ApprovalStatus;
import com.sporekart.ai.compliance.api.ComplianceEngine;
import com.sporekart.ai.compliance.engine.ComplianceRequest;
import com.sporekart.ai.compliance.engine.ComplianceResult;
import com.sporekart.ai.compliance.domain.ComplianceStatus;
import com.sporekart.ai.risk.api.RiskEngine;
import com.sporekart.ai.risk.engine.RiskAssessmentRequest;
import com.sporekart.ai.risk.engine.RiskAssessmentResult;
import com.sporekart.ai.risk.domain.RiskLevel;
import com.sporekart.ai.analytics.api.GovernanceAnalyticsService;
import com.sporekart.ai.analytics.api.MetricsAggregationService;
import com.sporekart.ai.analytics.api.KPIService;
import com.sporekart.ai.analytics.api.TrendAnalysisService;
import com.sporekart.ai.analytics.domain.GovernanceMetric;
import com.sporekart.ai.analytics.domain.GovernanceKPI;
import com.sporekart.ai.analytics.domain.GovernanceTrend;
import com.sporekart.ai.analytics.domain.GovernanceSummary;
import com.sporekart.ai.analytics.domain.MetricType;
import com.sporekart.ai.analytics.domain.TrendDirection;
import com.sporekart.ai.admin.api.AdministrationService;
import com.sporekart.ai.admin.domain.AdminOperation;
import com.sporekart.ai.admin.domain.AdminOperationType;
import com.sporekart.ai.automation.api.AutomationEngine;
import com.sporekart.ai.automation.api.AutomationAuditService;
import com.sporekart.ai.automation.domain.AutomationJob;
import com.sporekart.ai.automation.domain.AutomationStatus;
import com.sporekart.ai.automation.domain.JobType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GovernancePipelineIntegrationTest {

    @Mock private GovernanceEngine governanceEngine;
    @Mock private PolicyEngine policyEngine;
    @Mock private DecisionEngine decisionEngine;
    @Mock private ApprovalEngine approvalEngine;
    @Mock private ComplianceEngine complianceEngine;
    @Mock private RiskEngine riskEngine;
    @Mock private GovernanceAnalyticsService analyticsService;
    @Mock private MetricsAggregationService metricsAggregationService;
    @Mock private KPIService kpiService;
    @Mock private TrendAnalysisService trendAnalysisService;
    @Mock private AdministrationService administrationService;
    @Mock private AutomationEngine automationEngine;
    @Mock private AutomationAuditService automationAuditService;

    @Test
    void testFullGovernancePipelineEndToEnd() {
        GovernanceRequest request = new GovernanceRequest(
            UUID.randomUUID(), "catalog", "create_product",
            Map.of("name", "product1", "price", 100.0),
            Map.of("source", "web"), "user1", List.of("admin"),
            OffsetDateTime.now()
        );

        GovernanceResponse governanceResponse = new GovernanceResponse(
            UUID.randomUUID(), request.id(), GovernanceDecision.ALLOW,
            List.of(), Map.of("validated", true), 10L, OffsetDateTime.now()
        );
        when(governanceEngine.validate(request)).thenReturn(governanceResponse);

        EvaluationResult policyResult = new EvaluationResult(
            request.id(), PolicyDecision.ALLOW, List.of(), List.of(),
            5L, true, OffsetDateTime.now()
        );
        when(policyEngine.evaluate(any(EvaluationRequest.class))).thenReturn(policyResult);

        DecisionResult decisionResult = new DecisionResult(
            UUID.randomUUID(), request.id(), DecisionAction.ALLOW,
            DecisionStatus.ALLOWED, DecisionConfidence.HIGH, "allowed",
            List.of(), List.of(), null, 8L, false, false,
            OffsetDateTime.now()
        );
        when(decisionEngine.evaluate(any(DecisionRequest.class))).thenReturn(decisionResult);

        ApprovalRequest approvalRequest = new ApprovalRequest(
            UUID.randomUUID(), request.module(), request.action(),
            request.payload(), request.metadata(), request.userId(),
            request.roles(), "auto-approved", "low",
            decisionResult.id(), Map.of(), OffsetDateTime.now().plusDays(1),
            ApprovalStatus.APPROVED, OffsetDateTime.now()
        );
        when(approvalEngine.submit(any(ApprovalRequest.class))).thenReturn(approvalRequest);

        ComplianceResult complianceResult = new ComplianceResult(
            true, ComplianceStatus.PASSED, List.of(), List.of(),
            UUID.randomUUID(), UUID.randomUUID(), Map.of("checked", true)
        );
        when(complianceEngine.validate(any(ComplianceRequest.class))).thenReturn(complianceResult);

        RiskAssessmentResult riskResult = new RiskAssessmentResult(
            UUID.randomUUID(), true, RiskLevel.LOW, 0.2, 0.95, 0.9, null,
            Map.of("riskScore", 0.2, "trustScore", 0.95, "confidenceScore", 0.9)
        );
        when(riskEngine.assess(any(RiskAssessmentRequest.class))).thenReturn(riskResult);

        GovernanceSummary summary = new GovernanceSummary(
            UUID.randomUUID(), "Pipeline Summary",
            Map.of("totalRequests", 1), Map.of("successRate", 100.0),
            Map.of("riskTrend", "stable"), Instant.now()
        );
        when(analyticsService.getSummary()).thenReturn(summary);

        GovernanceMetric metric = new GovernanceMetric(
            UUID.randomUUID(), "risk_score", "catalog",
            MetricType.SCORE, 0.2, Map.of("assessment", "ok"), null
        );
        when(metricsAggregationService.recordMetric(
            anyString(), anyString(), any(MetricType.class), anyDouble(), any()
        )).thenReturn(metric);

        GovernanceKPI kpi = new GovernanceKPI(
            UUID.randomUUID(), "approval_rate", "Approval rate", "catalog",
            95.0, 90.0, 5.0, null, Map.of(), null
        );
        when(kpiService.calculateKPI(anyString(), anyString(), anyDouble(), anyDouble(), anyDouble()))
            .thenReturn(kpi);

        GovernanceTrend trend = new GovernanceTrend(
            UUID.randomUUID(), "risk_trend", "catalog",
            List.of(0.2, 0.3, 0.25), List.of(), TrendDirection.STABLE, 0.0, null
        );
        when(trendAnalysisService.calculateTrend(anyString(), anyString(), anyList(), anyList()))
            .thenReturn(trend);

        AdminOperation adminOp = new AdminOperation(
            UUID.randomUUID(), AdminOperationType.CONFIG_UPDATE,
            "Verified governance config", Map.of("status", "ok"),
            UUID.randomUUID(), "127.0.0.1", true, null
        );
        when(administrationService.performOperation(
            any(AdminOperationType.class), anyString(), anyMap(), any(), anyString()
        )).thenReturn(adminOp);

        AutomationJob job = new AutomationJob(
            UUID.randomUUID(), JobType.HEALTH_CHECK, "post-governance-check",
            Map.of("requestId", request.id().toString()), AutomationStatus.COMPLETED,
            0, 3, null, null, null, null
        );
        when(automationEngine.executeJob(any(JobType.class), anyString(), anyMap())).thenReturn(job);

        doNothing().when(automationAuditService).recordAudit(
            anyString(), anyString(), any(), any(), anyMap(), anyString()
        );

        GovernanceResponse govResult = governanceEngine.validate(request);
        assertNotNull(govResult);
        assertEquals(GovernanceDecision.ALLOW, govResult.decision());

        EvaluationRequest evalRequest = new EvaluationRequest(
            request.id(), request.module(), request.action(),
            request.payload(), request.metadata(), request.userId(),
            request.roles(), Map.of(), request.timestamp()
        );
        EvaluationResult evalResult = policyEngine.evaluate(evalRequest);
        assertNotNull(evalResult);
        assertTrue(evalResult.passed());

        DecisionRequest decRequest = new DecisionRequest(
            request.id(), request.module(), request.action(),
            request.payload(), request.metadata(), request.userId(),
            request.roles(), List.of(), List.of(),
            Map.of("policyDecision", evalResult.finalDecision().name()), request.timestamp()
        );
        DecisionResult decResult = decisionEngine.evaluate(decRequest);
        assertNotNull(decResult);
        assertEquals(DecisionAction.ALLOW, decResult.action());

        ApprovalRequest appPayload = new ApprovalRequest(
            UUID.randomUUID(), request.module(), request.action(),
            request.payload(), request.metadata(), request.userId(),
            request.roles(), "auto", "low",
            decResult.id(), Map.of(), OffsetDateTime.now().plusDays(1),
            ApprovalStatus.PENDING, OffsetDateTime.now()
        );
        ApprovalRequest appResult = approvalEngine.submit(appPayload);
        assertNotNull(appResult);
        assertEquals(ApprovalStatus.APPROVED, appResult.status());

        ComplianceRequest compRequest = new ComplianceRequest(
            request.module(), request.action(),
            Map.of("decisionId", decResult.id().toString()),
            request.userId(), request.roles()
        );
        ComplianceResult compResult = complianceEngine.validate(compRequest);
        assertNotNull(compResult);
        assertTrue(compResult.compliant());

        RiskAssessmentRequest riskReq = new RiskAssessmentRequest(
            UUID.randomUUID(), request.module(), request.action(),
            Map.of("complianceResult", compResult.compliant())
        );
        RiskAssessmentResult riskEval = riskEngine.assess(riskReq);
        assertNotNull(riskEval);
        assertEquals(RiskLevel.LOW, riskEval.riskLevel());
        assertTrue(riskEval.trustScore() > 0.9);
        assertTrue(riskEval.confidenceScore() > 0.8);

        GovernanceSummary analyticsSummary = analyticsService.getSummary();
        assertNotNull(analyticsSummary);
        assertNotNull(analyticsSummary.metrics());

        GovernanceMetric recordedMetric = metricsAggregationService.recordMetric(
            "pipeline_complete", "catalog", MetricType.COUNT, 1.0, Map.of()
        );
        assertNotNull(recordedMetric);

        GovernanceKPI calculatedKpi = kpiService.calculateKPI(
            "pipeline_success", "catalog", 100.0, 95.0, 5.0
        );
        assertNotNull(calculatedKpi);

        GovernanceTrend calculatedTrend = trendAnalysisService.calculateTrend(
            "throughput", "catalog", List.of(1.0, 2.0), List.of("t1", "t2")
        );
        assertNotNull(calculatedTrend);

        AdminOperation verifiedOp = administrationService.performOperation(
            AdminOperationType.CONFIG_UPDATE, "Pipeline complete",
            Map.of("requestId", request.id().toString()),
            UUID.randomUUID(), "127.0.0.1"
        );
        assertNotNull(verifiedOp);
        assertTrue(verifiedOp.successful());

        AutomationJob scheduledJob = automationEngine.executeJob(
            JobType.HEALTH_CHECK, "post-pipeline-validation",
            Map.of("requestId", request.id().toString())
        );
        assertNotNull(scheduledJob);
        assertEquals(AutomationStatus.COMPLETED, scheduledJob.status());

        automationAuditService.recordAudit(
            "PIPELINE_COMPLETE", "GovernanceRequest", request.id(),
            UUID.randomUUID(), Map.of("finalStatus", "success"), "127.0.0.1"
        );

        verify(governanceEngine).validate(request);
        verify(policyEngine).evaluate(any(EvaluationRequest.class));
        verify(decisionEngine).evaluate(any(DecisionRequest.class));
        verify(approvalEngine).submit(any(ApprovalRequest.class));
        verify(complianceEngine).validate(any(ComplianceRequest.class));
        verify(riskEngine).assess(any(RiskAssessmentRequest.class));
        verify(analyticsService).getSummary();
        verify(metricsAggregationService).recordMetric(anyString(), anyString(), any(MetricType.class), anyDouble(), any());
        verify(kpiService).calculateKPI(anyString(), anyString(), anyDouble(), anyDouble(), anyDouble());
        verify(trendAnalysisService).calculateTrend(anyString(), anyString(), anyList(), anyList());
        verify(administrationService).performOperation(any(AdminOperationType.class), anyString(), anyMap(), any(), anyString());
        verify(automationEngine).executeJob(any(JobType.class), anyString(), anyMap());
        verify(automationAuditService).recordAudit(anyString(), anyString(), any(), any(), anyMap(), anyString());
    }
}
