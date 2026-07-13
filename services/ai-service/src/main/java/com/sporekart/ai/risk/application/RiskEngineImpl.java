package com.sporekart.ai.risk.application;

import com.sporekart.ai.risk.api.*;
import com.sporekart.ai.risk.domain.*;
import com.sporekart.ai.risk.engine.RiskAssessmentRequest;
import com.sporekart.ai.risk.engine.RiskAssessmentResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class RiskEngineImpl implements RiskEngine {

    private final RiskAssessmentService riskAssessmentService;
    private final RiskScoringService riskScoringService;
    private final RiskClassificationService riskClassificationService;
    private final TrustEngine trustEngine;
    private final ConfidenceCalculator confidenceCalculator;
    private final RiskRecommendationService riskRecommendationService;
    private final RiskAuditService riskAuditService;
    private final RiskMetricsService riskMetricsService;

    @Override
    @Transactional
    public RiskAssessmentResult assess(RiskAssessmentRequest request) {
        long start = System.currentTimeMillis();

        RiskAssessment assessment = riskAssessmentService.createAssessment(
            request.module(), request.action(), request.context()
        );

        RiskScore riskScore = riskScoringService.calculateScore(assessment);
        RiskCategory primaryCategory = riskClassificationService.classify(assessment);
        TrustAssessment trustAssessment = trustEngine.calculateTrust(assessment);
        ConfidenceScore confidenceScore = confidenceCalculator.calculateConfidence(assessment);
        RiskRecommendation recommendation = riskRecommendationService.generateRecommendation(
            assessment, riskScore, trustAssessment, confidenceScore
        );

        riskAssessmentService.completeAssessment(assessment.id());

        riskAuditService.recordAudit(
            "RISK_ASSESSMENT", "ASSESSMENT", assessment.id(), null,
            Map.of(
                "riskLevel", riskScore.riskLevel().name(),
                "riskScore", riskScore.overallScore(),
                "trustScore", trustAssessment.overallTrustScore(),
                "confidenceScore", confidenceScore.overallConfidence(),
                "recommendation", recommendation.type().name(),
                "category", primaryCategory.name()
            ),
            null
        );

        long elapsed = System.currentTimeMillis() - start;
        riskMetricsService.recordAssessment(riskScore.riskLevel());
        riskMetricsService.recordRecommendation(recommendation.type());
        riskMetricsService.recordTrustScore(trustAssessment.overallTrustScore());
        riskMetricsService.recordConfidence(confidenceScore.overallConfidence());
        riskMetricsService.recordLatency(elapsed);

        boolean proceed = riskScore.riskLevel() == RiskLevel.LOW || riskScore.riskLevel() == RiskLevel.MEDIUM;

        Map<String, Object> details = new HashMap<>();
        details.put("primaryCategory", primaryCategory.name());
        details.put("factorCount", riskScore.factorCount());
        details.put("trustFactors", trustAssessment.factorScores());
        details.put("confidenceFactors", confidenceScore.factorScores());
        details.put("latencyMs", elapsed);

        log.info("Risk assessment completed for assessment {} with level {} in {}ms",
            assessment.id(), riskScore.riskLevel(), elapsed);

        return new RiskAssessmentResult(
            assessment.id(), proceed, riskScore.riskLevel(),
            riskScore.overallScore(), trustAssessment.overallTrustScore(),
            confidenceScore.overallConfidence(), recommendation, details
        );
    }

    @Override
    @Transactional
    public RiskAssessmentResult reassess(UUID assessmentId, Map<String, Object> context) {
        long start = System.currentTimeMillis();

        RiskAssessment assessment = riskAssessmentService.getAssessment(assessmentId);

        RiskScore riskScore = riskScoringService.calculateScore(assessment);
        RiskCategory primaryCategory = riskClassificationService.classify(assessment);
        TrustAssessment trustAssessment = trustEngine.recalculateTrust(assessmentId);
        ConfidenceScore confidenceScore = confidenceCalculator.recalculateConfidence(assessmentId);
        RiskRecommendation recommendation = riskRecommendationService.generateRecommendation(
            assessment, riskScore, trustAssessment, confidenceScore
        );

        riskAssessmentService.completeAssessment(assessment.id());

        riskAuditService.recordAudit(
            "RISK_REASSESSMENT", "ASSESSMENT", assessment.id(), null,
            Map.of(
                "riskLevel", riskScore.riskLevel().name(),
                "riskScore", riskScore.overallScore(),
                "trustScore", trustAssessment.overallTrustScore(),
                "confidenceScore", confidenceScore.overallConfidence(),
                "recommendation", recommendation.type().name(),
                "category", primaryCategory.name()
            ),
            null
        );

        long elapsed = System.currentTimeMillis() - start;
        riskMetricsService.recordAssessment(riskScore.riskLevel());
        riskMetricsService.recordRecommendation(recommendation.type());
        riskMetricsService.recordTrustScore(trustAssessment.overallTrustScore());
        riskMetricsService.recordConfidence(confidenceScore.overallConfidence());
        riskMetricsService.recordLatency(elapsed);

        boolean proceed = riskScore.riskLevel() == RiskLevel.LOW || riskScore.riskLevel() == RiskLevel.MEDIUM;

        Map<String, Object> details = new HashMap<>();
        details.put("primaryCategory", primaryCategory.name());
        details.put("factorCount", riskScore.factorCount());
        details.put("trustFactors", trustAssessment.factorScores());
        details.put("confidenceFactors", confidenceScore.factorScores());
        details.put("latencyMs", elapsed);

        log.info("Risk reassessment completed for assessment {} with level {} in {}ms",
            assessment.id(), riskScore.riskLevel(), elapsed);

        return new RiskAssessmentResult(
            assessment.id(), proceed, riskScore.riskLevel(),
            riskScore.overallScore(), trustAssessment.overallTrustScore(),
            confidenceScore.overallConfidence(), recommendation, details
        );
    }
}
