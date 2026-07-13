package com.sporekart.ai.integration;

import com.sporekart.ai.compliance.api.ComplianceEngine;
import com.sporekart.ai.compliance.engine.ComplianceRequest;
import com.sporekart.ai.compliance.engine.ComplianceResult;
import com.sporekart.ai.compliance.domain.ComplianceStatus;
import com.sporekart.ai.risk.api.RiskEngine;
import com.sporekart.ai.risk.api.TrustScoreService;
import com.sporekart.ai.risk.engine.RiskAssessmentRequest;
import com.sporekart.ai.risk.engine.RiskAssessmentResult;
import com.sporekart.ai.risk.domain.RiskLevel;
import com.sporekart.ai.risk.domain.TrustAssessment;
import com.sporekart.ai.risk.domain.TrustFactor;
import com.sporekart.ai.risk.domain.ConfidenceScore;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ComplianceRiskIntegrationTest {

    @Mock private ComplianceEngine complianceEngine;
    @Mock private RiskEngine riskEngine;
    @Mock private TrustScoreService trustScoreService;

    @Test
    void testComplianceResultTriggersRiskAssessment() {
        ComplianceRequest compRequest = new ComplianceRequest(
            "catalog", "publish_item",
            Map.of("itemId", "P100", "category", "electronics"),
            "user1", List.of("editor")
        );

        ComplianceResult compResult = new ComplianceResult(
            true, ComplianceStatus.PASSED, List.of(), List.of(),
            UUID.randomUUID(), UUID.randomUUID(),
            Map.of("checked", true, "framework", "ISO27001")
        );
        when(complianceEngine.validate(compRequest)).thenReturn(compResult);

        RiskAssessmentRequest riskRequest = new RiskAssessmentRequest(
            UUID.randomUUID(), "catalog", "publish_item",
            Map.of("compliancePassed", true, "complianceAssessmentId",
                   compResult.assessmentId().toString())
        );

        RiskAssessmentResult riskResult = new RiskAssessmentResult(
            UUID.randomUUID(), true, RiskLevel.LOW, 0.15, 0.97, 0.94, null,
            Map.of("riskLevel", "LOW", "score", 0.15)
        );
        when(riskEngine.assess(riskRequest)).thenReturn(riskResult);

        ComplianceResult validation = complianceEngine.validate(compRequest);
        assertTrue(validation.compliant());

        RiskAssessmentResult assessment = riskEngine.assess(riskRequest);
        assertNotNull(assessment);
        assertEquals(RiskLevel.LOW, assessment.riskLevel());
        assertTrue(assessment.riskScore() < 0.2);
        assertTrue(assessment.trustScore() > 0.95);
        assertTrue(assessment.confidenceScore() > 0.9);

        verify(complianceEngine).validate(compRequest);
        verify(riskEngine).assess(riskRequest);
    }

    @Test
    void testComplianceFailureIncreasesRiskScore() {
        ComplianceRequest compRequest = new ComplianceRequest(
            "finance", "process_payment",
            Map.of("amount", 50000.0, "currency", "USD"),
            "user2", List.of("finance_admin")
        );

        ComplianceResult compResult = new ComplianceResult(
            false, ComplianceStatus.FAILED, List.of(), List.of(),
            UUID.randomUUID(), UUID.randomUUID(),
            Map.of("reason", "AML check failed", "violations", 3)
        );
        when(complianceEngine.validate(compRequest)).thenReturn(compResult);

        RiskAssessmentRequest riskRequest = new RiskAssessmentRequest(
            UUID.randomUUID(), "finance", "process_payment",
            Map.of("compliancePassed", false, "violations", 3)
        );

        RiskAssessmentResult riskResult = new RiskAssessmentResult(
            UUID.randomUUID(), false, RiskLevel.CRITICAL, 0.85, 0.25, 0.30, null,
            Map.of("riskLevel", "CRITICAL", "reason", "Compliance failure")
        );
        when(riskEngine.assess(riskRequest)).thenReturn(riskResult);

        ComplianceResult validation = complianceEngine.validate(compRequest);
        assertFalse(validation.compliant());
        assertEquals(ComplianceStatus.FAILED, validation.status());

        RiskAssessmentResult assessment = riskEngine.assess(riskRequest);
        assertFalse(assessment.proceed());
        assertEquals(RiskLevel.CRITICAL, assessment.riskLevel());
        assertTrue(assessment.riskScore() > 0.8);
        assertTrue(assessment.trustScore() < 0.3);
        assertTrue(assessment.confidenceScore() < 0.4);

        verify(complianceEngine).validate(compRequest);
        verify(riskEngine).assess(riskRequest);
    }

    @Test
    void testTrustAndConfidenceScoresGeneratedFromRisk() {
        ComplianceRequest compRequest = new ComplianceRequest(
            "vendor", "onboard_vendor",
            Map.of("vendorId", "V500", "country", "IN"),
            "user3", List.of("procurement")
        );

        ComplianceResult compResult = new ComplianceResult(
            true, ComplianceStatus.PASSED, List.of(), List.of(),
            UUID.randomUUID(), UUID.randomUUID(), Map.of()
        );
        when(complianceEngine.validate(compRequest)).thenReturn(compResult);

        UUID assessmentId = UUID.randomUUID();
        RiskAssessmentResult riskResult = new RiskAssessmentResult(
            assessmentId, true, RiskLevel.MEDIUM, 0.45, 0.72, 0.68, null,
            Map.of("riskLevel", "MEDIUM", "assessmentId", assessmentId.toString())
        );

        RiskAssessmentRequest riskRequest = new RiskAssessmentRequest(
            assessmentId, "vendor", "onboard_vendor",
            Map.of("compliancePassed", true)
        );
        when(riskEngine.assess(riskRequest)).thenReturn(riskResult);

        TrustAssessment trustAssessment = new TrustAssessment(
            UUID.randomUUID(), assessmentId, 0.72,
            Map.of(TrustFactor.HISTORICAL_ACCURACY, 0.8, TrustFactor.POLICY_COMPLIANCE, 0.9),
            Map.of(TrustFactor.HISTORICAL_ACCURACY, "Good track record"),
            Instant.now()
        );
        when(trustScoreService.calculateTrustScore(assessmentId)).thenReturn(trustAssessment);

        ComplianceResult validation = complianceEngine.validate(compRequest);
        assertTrue(validation.compliant());

        RiskAssessmentResult assessment = riskEngine.assess(riskRequest);
        assertEquals(RiskLevel.MEDIUM, assessment.riskLevel());
        assertEquals(0.72, assessment.trustScore(), 0.01);
        assertEquals(0.68, assessment.confidenceScore(), 0.01);

        TrustAssessment trust = trustScoreService.calculateTrustScore(assessmentId);
        assertNotNull(trust);
        assertEquals(0.72, trust.overallTrustScore(), 0.01);
        assertTrue(trust.factorScores().containsKey(TrustFactor.HISTORICAL_ACCURACY));

        verify(complianceEngine).validate(compRequest);
        verify(riskEngine).assess(riskRequest);
        verify(trustScoreService).calculateTrustScore(assessmentId);
    }
}
