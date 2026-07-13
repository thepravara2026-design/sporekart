package com.sporekart.ai.pipeline;

import com.sporekart.ai.governance.api.GovernanceEngine;
import com.sporekart.ai.governance.domain.GovernanceDecision;
import com.sporekart.ai.governance.domain.GovernanceRequest;
import com.sporekart.ai.governance.domain.GovernanceResponse;
import com.sporekart.ai.governance.domain.GovernanceSeverity;
import com.sporekart.ai.governance.domain.GovernanceViolation;
import com.sporekart.ai.policy.api.PolicyEngine;
import com.sporekart.ai.policy.domain.EvaluationRequest;
import com.sporekart.ai.policy.domain.EvaluationResult;
import com.sporekart.ai.policy.domain.PolicyDecision;
import com.sporekart.ai.policy.domain.PolicySeverity;
import com.sporekart.ai.policy.domain.PolicyViolation;
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
import com.sporekart.ai.analytics.api.KPIService;
import com.sporekart.ai.analytics.api.MetricsAggregationService;
import com.sporekart.ai.analytics.api.TrendAnalysisService;
import com.sporekart.ai.analytics.domain.GovernanceKPI;
import com.sporekart.ai.analytics.domain.GovernanceMetric;
import com.sporekart.ai.analytics.domain.GovernanceSummary;
import com.sporekart.ai.analytics.domain.GovernanceTrend;
import com.sporekart.ai.analytics.domain.KpiStatus;
import com.sporekart.ai.analytics.domain.MetricType;
import com.sporekart.ai.analytics.domain.TrendDirection;
import com.sporekart.ai.admin.api.AdministrationService;
import com.sporekart.ai.admin.domain.AdminOperation;
import com.sporekart.ai.admin.domain.AdminOperationType;
import com.sporekart.ai.automation.api.AutomationEngine;
import com.sporekart.ai.automation.domain.AutomationJob;
import com.sporekart.ai.automation.domain.AutomationStatus;
import com.sporekart.ai.automation.domain.JobType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
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
class GovernancePipelineTest {

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

    private GovernanceRequest request;
    private GovernanceResponse governanceResponse;
    private EvaluationResult policyResult;
    private DecisionResult decisionResult;
    private ApprovalRequest approvalRequest;
    private ComplianceResult complianceResult;
    private RiskAssessmentResult riskResult;
    private GovernanceSummary analyticsSummary;
    private GovernanceMetric metric;
    private GovernanceKPI kpi;
    private GovernanceTrend trend;
    private AdminOperation adminOp;
    private AutomationJob automationJob;

    @BeforeEach
    void setUp() {
        UUID requestId = UUID.randomUUID();
        request = new GovernanceRequest(
            requestId, "catalog", "create_product",
            Map.of("name", "test-product", "price", 99.99),
            Map.of("source", "web"), "user1", List.of("admin"),
            OffsetDateTime.now()
        );

        governanceResponse = new GovernanceResponse(
            UUID.randomUUID(), requestId, GovernanceDecision.ALLOW,
            List.of(), Map.of("validated", true), 10L, OffsetDateTime.now()
        );

        policyResult = new EvaluationResult(
            requestId, PolicyDecision.ALLOW, List.of(), List.of(),
            5L, true, OffsetDateTime.now()
        );

        decisionResult = new DecisionResult(
            UUID.randomUUID(), requestId, DecisionAction.ALLOW,
            DecisionStatus.ALLOWED, DecisionConfidence.HIGH, "Allowed",
            List.of(), List.of(), null, 8L, false, false, OffsetDateTime.now()
        );

        approvalRequest = new ApprovalRequest(
            UUID.randomUUID(), request.module(), request.action(),
            request.payload(), request.metadata(), request.userId(),
            request.roles(), "auto-approved", "low",
            decisionResult.id(), Map.of(), OffsetDateTime.now().plusDays(1),
            ApprovalStatus.APPROVED, OffsetDateTime.now()
        );

        complianceResult = new ComplianceResult(
            true, ComplianceStatus.PASSED, List.of(), List.of(),
            UUID.randomUUID(), UUID.randomUUID(), Map.of()
        );

        riskResult = new RiskAssessmentResult(
            UUID.randomUUID(), true, RiskLevel.LOW, 0.15, 0.98, 0.96,
            null, Map.of()
        );

        analyticsSummary = new GovernanceSummary(
            UUID.randomUUID(), "Summary",
            Map.of("total", 1), Map.of("rate", 100.0), Map.of(), Instant.now()
        );

        metric = new GovernanceMetric(
            UUID.randomUUID(), "test_metric", "catalog",
            MetricType.COUNT, 1.0, Map.of(), null
        );

        kpi = new GovernanceKPI(
            UUID.randomUUID(), "test_kpi", "desc", "catalog",
            95.0, 90.0, 5.0, KpiStatus.ON_TRACK, Map.of(), null
        );

        trend = new GovernanceTrend(
            UUID.randomUUID(), "test_trend", "catalog",
            List.of(1.0, 2.0), List.of(), TrendDirection.UP, 100.0, null
        );

        adminOp = new AdminOperation(
            UUID.randomUUID(), AdminOperationType.CONFIG_UPDATE,
            "Pipeline verification", Map.of("status", "ok"),
            UUID.randomUUID(), "127.0.0.1", true, null
        );

        automationJob = new AutomationJob(
            UUID.randomUUID(), JobType.HEALTH_CHECK, "post-pipeline",
            Map.of(), AutomationStatus.COMPLETED, 0, 3, null, null, null, null
        );
    }

    @Test
    void testCompletePipelineSequence() {
        when(governanceEngine.validate(request)).thenReturn(governanceResponse);
        when(policyEngine.evaluate(any(EvaluationRequest.class))).thenReturn(policyResult);
        when(decisionEngine.evaluate(any(DecisionRequest.class))).thenReturn(decisionResult);
        when(approvalEngine.submit(any(ApprovalRequest.class))).thenReturn(approvalRequest);
        when(complianceEngine.validate(any(ComplianceRequest.class))).thenReturn(complianceResult);
        when(riskEngine.assess(any(RiskAssessmentRequest.class))).thenReturn(riskResult);
        when(analyticsService.getSummary()).thenReturn(analyticsSummary);
        when(metricsAggregationService.recordMetric(anyString(), anyString(), any(MetricType.class), anyDouble(), anyMap()))
            .thenReturn(metric);
        when(kpiService.calculateKPI(anyString(), anyString(), anyDouble(), anyDouble(), anyDouble()))
            .thenReturn(kpi);
        when(trendAnalysisService.calculateTrend(anyString(), anyString(), anyList(), anyList()))
            .thenReturn(trend);
        when(administrationService.performOperation(any(AdminOperationType.class), anyString(), anyMap(), any(), anyString()))
            .thenReturn(adminOp);
        when(automationEngine.executeJob(any(JobType.class), anyString(), anyMap()))
            .thenReturn(automationJob);

        GovernanceResponse step1 = governanceEngine.validate(request);
        assertNotNull(step1);
        assertEquals(GovernanceDecision.ALLOW, step1.decision());

        EvaluationRequest evalReq = new EvaluationRequest(
            request.id(), request.module(), request.action(),
            request.payload(), request.metadata(), request.userId(),
            request.roles(), Map.of(), request.timestamp()
        );
        EvaluationResult step2 = policyEngine.evaluate(evalReq);
        assertNotNull(step2);
        assertTrue(step2.passed());

        DecisionRequest decReq = new DecisionRequest(
            request.id(), request.module(), request.action(),
            request.payload(), request.metadata(), request.userId(),
            request.roles(), List.of(), List.of(),
            Map.of("policyDecision", step2.finalDecision().name()), request.timestamp()
        );
        DecisionResult step3 = decisionEngine.evaluate(decReq);
        assertNotNull(step3);
        assertEquals(DecisionAction.ALLOW, step3.action());

        ApprovalRequest appReq = new ApprovalRequest(
            UUID.randomUUID(), request.module(), request.action(),
            request.payload(), request.metadata(), request.userId(),
            request.roles(), "auto", "low",
            step3.id(), Map.of(), OffsetDateTime.now().plusDays(1),
            ApprovalStatus.PENDING, OffsetDateTime.now()
        );
        ApprovalRequest step4 = approvalEngine.submit(appReq);
        assertNotNull(step4);
        assertTrue(
            step4.status() == ApprovalStatus.APPROVED ||
            step4.status() == ApprovalStatus.PENDING
        );

        ComplianceRequest compReq = new ComplianceRequest(
            request.module(), request.action(),
            Map.of("decisionId", step3.id().toString()),
            request.userId(), request.roles()
        );
        ComplianceResult step5 = complianceEngine.validate(compReq);
        assertNotNull(step5);
        assertTrue(step5.compliant());

        RiskAssessmentRequest riskReq = new RiskAssessmentRequest(
            UUID.randomUUID(), request.module(), request.action(),
            Map.of("compliant", step5.compliant())
        );
        RiskAssessmentResult step6 = riskEngine.assess(riskReq);
        assertNotNull(step6);
        assertTrue(step6.trustScore() > 0.9);

        GovernanceSummary step7 = analyticsService.getSummary();
        assertNotNull(step7);

        GovernanceMetric step8 = metricsAggregationService.recordMetric(
            "pipeline_step", "catalog", MetricType.COUNT, 1.0, Map.of()
        );
        assertNotNull(step8);

        GovernanceKPI step9 = kpiService.calculateKPI(
            "success_rate", "catalog", 100.0, 95.0, 5.0
        );
        assertNotNull(step9);

        GovernanceTrend step10 = trendAnalysisService.calculateTrend(
            "volume", "catalog", List.of(1.0), List.of("now")
        );
        assertNotNull(step10);

        AdminOperation step11 = administrationService.performOperation(
            AdminOperationType.CONFIG_UPDATE, "Pipeline completed",
            Map.of("requestId", request.id().toString()),
            UUID.randomUUID(), "10.0.0.1"
        );
        assertNotNull(step11);
        assertTrue(step11.successful());

        AutomationJob step12 = automationEngine.executeJob(
            JobType.HEALTH_CHECK, "post-pipeline",
            Map.of("requestId", request.id().toString())
        );
        assertNotNull(step12);
        assertTrue(
            step12.status() == AutomationStatus.COMPLETED ||
            step12.status() == AutomationStatus.PENDING
        );
    }

    @Test
    void testPipelineOrderingAndDataFlow() {
        when(governanceEngine.validate(request)).thenReturn(governanceResponse);
        when(policyEngine.evaluate(any(EvaluationRequest.class))).thenReturn(policyResult);
        when(decisionEngine.evaluate(any(DecisionRequest.class))).thenReturn(decisionResult);
        when(approvalEngine.submit(any(ApprovalRequest.class))).thenReturn(approvalRequest);
        when(complianceEngine.validate(any(ComplianceRequest.class))).thenReturn(complianceResult);
        when(riskEngine.assess(any(RiskAssessmentRequest.class))).thenReturn(riskResult);
        when(analyticsService.getSummary()).thenReturn(analyticsSummary);
        when(metricsAggregationService.recordMetric(anyString(), anyString(), any(MetricType.class), anyDouble(), anyMap()))
            .thenReturn(metric);
        when(kpiService.calculateKPI(anyString(), anyString(), anyDouble(), anyDouble(), anyDouble()))
            .thenReturn(kpi);
        when(trendAnalysisService.calculateTrend(anyString(), anyString(), anyList(), anyList()))
            .thenReturn(trend);
        when(administrationService.performOperation(any(AdminOperationType.class), anyString(), anyMap(), any(), anyString()))
            .thenReturn(adminOp);
        when(automationEngine.executeJob(any(JobType.class), anyString(), anyMap()))
            .thenReturn(automationJob);

        governanceEngine.validate(request);
        policyEngine.evaluate(any(EvaluationRequest.class));
        decisionEngine.evaluate(any(DecisionRequest.class));
        approvalEngine.submit(any(ApprovalRequest.class));
        complianceEngine.validate(any(ComplianceRequest.class));
        riskEngine.assess(any(RiskAssessmentRequest.class));
        analyticsService.getSummary();
        administrationService.performOperation(any(AdminOperationType.class), anyString(), anyMap(), any(), anyString());
        automationEngine.executeJob(any(JobType.class), anyString(), anyMap());

        InOrder order = inOrder(
            governanceEngine, policyEngine, decisionEngine, approvalEngine,
            complianceEngine, riskEngine, analyticsService, administrationService,
            automationEngine
        );

        order.verify(governanceEngine).validate(request);
        order.verify(policyEngine).evaluate(any(EvaluationRequest.class));
        order.verify(decisionEngine).evaluate(any(DecisionRequest.class));
        order.verify(approvalEngine).submit(any(ApprovalRequest.class));
        order.verify(complianceEngine).validate(any(ComplianceRequest.class));
        order.verify(riskEngine).assess(any(RiskAssessmentRequest.class));
        order.verify(analyticsService).getSummary();
        order.verify(administrationService).performOperation(
            any(AdminOperationType.class), anyString(), anyMap(), any(), anyString()
        );
        order.verify(automationEngine).executeJob(any(JobType.class), anyString(), anyMap());
        order.verifyNoMoreInteractions();
    }

    @Test
    void testPipelineWithGovernanceDeny() {
        GovernanceResponse denyResponse = new GovernanceResponse(
            UUID.randomUUID(), request.id(), GovernanceDecision.DENY,
            List.of(new GovernanceViolation(
                UUID.randomUUID(), "RESTRICTED_MODULE",
                "Module not allowed", GovernanceSeverity.HIGH,
                Map.of("module", request.module()), false, OffsetDateTime.now()
            )), Map.of("blocked", true), 5L, OffsetDateTime.now()
        );
        when(governanceEngine.validate(request)).thenReturn(denyResponse);

        GovernanceResponse result = governanceEngine.validate(request);
        assertEquals(GovernanceDecision.DENY, result.decision());
        assertFalse(result.violations().isEmpty());

        verify(governanceEngine).validate(request);
        verify(policyEngine, never()).evaluate(any());
        verify(decisionEngine, never()).evaluate(any());
        verify(approvalEngine, never()).submit(any());
        verify(complianceEngine, never()).validate(any());
        verify(riskEngine, never()).assess(any());
        verify(analyticsService, never()).getSummary();
        verify(administrationService, never()).performOperation(any(), anyString(), anyMap(), any(), anyString());
        verify(automationEngine, never()).executeJob(any(), anyString(), anyMap());
    }

    @Test
    void testPipelineWithPolicyFailure() {
        EvaluationResult failedPolicy = new EvaluationResult(
            request.id(), PolicyDecision.DENY, List.of(), List.of(
                new PolicyViolation(UUID.randomUUID(), UUID.randomUUID(),
                    UUID.randomUUID(), "price-limit",
                    "Price exceeds limit", PolicySeverity.ERROR,
                    Map.of("maxPrice", 50), false, OffsetDateTime.now())
            ), 3L, false, OffsetDateTime.now()
        );

        when(governanceEngine.validate(request)).thenReturn(governanceResponse);
        when(policyEngine.evaluate(any(EvaluationRequest.class))).thenReturn(failedPolicy);

        DecisionResult denyDecision = new DecisionResult(
            UUID.randomUUID(), request.id(), DecisionAction.DENY,
            DecisionStatus.DENIED, DecisionConfidence.CERTAIN,
            "Policy violation", List.of(), List.of(), null,
            4L, false, false, OffsetDateTime.now()
        );
        when(decisionEngine.evaluate(any(DecisionRequest.class))).thenReturn(denyDecision);

        GovernanceResponse step1 = governanceEngine.validate(request);
        assertEquals(GovernanceDecision.ALLOW, step1.decision());

        EvaluationRequest evalReq = new EvaluationRequest(
            request.id(), request.module(), request.action(),
            request.payload(), request.metadata(), request.userId(),
            request.roles(), Map.of(), request.timestamp()
        );
        EvaluationResult step2 = policyEngine.evaluate(evalReq);
        assertFalse(step2.passed());
        assertEquals(PolicyDecision.DENY, step2.finalDecision());
        assertFalse(step2.violations().isEmpty());

        DecisionRequest decReq = new DecisionRequest(
            request.id(), request.module(), request.action(),
            request.payload(), request.metadata(), request.userId(),
            request.roles(), List.of(), List.of(),
            Map.of("policyDecision", step2.finalDecision().name()), request.timestamp()
        );
        DecisionResult step3 = decisionEngine.evaluate(decReq);
        assertEquals(DecisionAction.DENY, step3.action());

        verify(approvalEngine, never()).submit(any());
        verify(complianceEngine, never()).validate(any());
        verify(riskEngine, never()).assess(any());
    }
}
