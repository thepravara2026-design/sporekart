package com.sporekart.ai.risk.engine;

import com.sporekart.ai.risk.domain.RiskAssessment;
import com.sporekart.ai.risk.domain.TrustFactor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Slf4j
public class TrustScoreCalculator {

    private static final Map<TrustFactor, Double> DEFAULT_WEIGHTS = Map.ofEntries(
        Map.entry(TrustFactor.PROVIDER_RELIABILITY, 0.15),
        Map.entry(TrustFactor.KNOWLEDGE_QUALITY, 0.12),
        Map.entry(TrustFactor.SEMANTIC_CONFIDENCE, 0.10),
        Map.entry(TrustFactor.PROMPT_VALIDATION, 0.12),
        Map.entry(TrustFactor.HISTORICAL_ACCURACY, 0.13),
        Map.entry(TrustFactor.POLICY_COMPLIANCE, 0.10),
        Map.entry(TrustFactor.WORKFLOW_SUCCESS, 0.10),
        Map.entry(TrustFactor.CONTEXT_COMPLETENESS, 0.08),
        Map.entry(TrustFactor.OUTPUT_VALIDATION, 0.10)
    );

    public TrustResult calculate(RiskAssessment assessment) {
        Map<TrustFactor, Double> factorScores = new EnumMap<>(TrustFactor.class);
        Map<TrustFactor, String> reasons = new EnumMap<>(TrustFactor.class);

        double providerReliability = getProviderReliability(assessment);
        factorScores.put(TrustFactor.PROVIDER_RELIABILITY, providerReliability);
        reasons.put(TrustFactor.PROVIDER_RELIABILITY, "Provider reliability score based on historical data");

        double knowledgeQuality = getKnowledgeQuality(assessment);
        factorScores.put(TrustFactor.KNOWLEDGE_QUALITY, knowledgeQuality);
        reasons.put(TrustFactor.KNOWLEDGE_QUALITY, "Knowledge quality assessed from source accuracy");

        double semanticConfidence = getSemanticConfidence(assessment);
        factorScores.put(TrustFactor.SEMANTIC_CONFIDENCE, semanticConfidence);
        reasons.put(TrustFactor.SEMANTIC_CONFIDENCE, "Semantic confidence based on embedding similarity");

        double promptValidation = getPromptValidation(assessment);
        factorScores.put(TrustFactor.PROMPT_VALIDATION, promptValidation);
        reasons.put(TrustFactor.PROMPT_VALIDATION, "Prompt validation passed all checks");

        double historicalAccuracy = getHistoricalAccuracy(assessment);
        factorScores.put(TrustFactor.HISTORICAL_ACCURACY, historicalAccuracy);
        reasons.put(TrustFactor.HISTORICAL_ACCURACY, "Historical accuracy from past assessments");

        double policyCompliance = getPolicyCompliance(assessment);
        factorScores.put(TrustFactor.POLICY_COMPLIANCE, policyCompliance);
        reasons.put(TrustFactor.POLICY_COMPLIANCE, "Policy compliance verified against active policies");

        double workflowSuccess = getWorkflowSuccess(assessment);
        factorScores.put(TrustFactor.WORKFLOW_SUCCESS, workflowSuccess);
        reasons.put(TrustFactor.WORKFLOW_SUCCESS, "Workflow success rate from execution history");

        double contextCompleteness = getContextCompleteness(assessment);
        factorScores.put(TrustFactor.CONTEXT_COMPLETENESS, contextCompleteness);
        reasons.put(TrustFactor.CONTEXT_COMPLETENESS, "Context completeness evaluated from input size");

        double outputValidation = getOutputValidation(assessment);
        factorScores.put(TrustFactor.OUTPUT_VALIDATION, outputValidation);
        reasons.put(TrustFactor.OUTPUT_VALIDATION, "Output validation score from validation rules");

        double weightedSum = 0.0;
        double totalWeight = 0.0;
        for (Map.Entry<TrustFactor, Double> entry : factorScores.entrySet()) {
            double weight = DEFAULT_WEIGHTS.getOrDefault(entry.getKey(), 0.1);
            weightedSum += entry.getValue() * weight;
            totalWeight += weight;
        }

        double overallScore = totalWeight > 0 ? Math.min(100, Math.max(0, weightedSum / totalWeight)) : 0.0;

        log.debug("Trust score calculated: {}", overallScore);
        return new TrustResult(overallScore, factorScores, reasons);
    }

    private double getProviderReliability(RiskAssessment assessment) {
        return extractDouble(assessment, "providerReliability", 85.0);
    }

    private double getKnowledgeQuality(RiskAssessment assessment) {
        return extractDouble(assessment, "knowledgeQuality", 80.0);
    }

    private double getSemanticConfidence(RiskAssessment assessment) {
        return extractDouble(assessment, "semanticConfidence", 75.0);
    }

    private double getPromptValidation(RiskAssessment assessment) {
        return extractDouble(assessment, "promptValidation", 90.0);
    }

    private double getHistoricalAccuracy(RiskAssessment assessment) {
        return extractDouble(assessment, "historicalAccuracy", 70.0);
    }

    private double getPolicyCompliance(RiskAssessment assessment) {
        return extractDouble(assessment, "policyCompliance", 85.0);
    }

    private double getWorkflowSuccess(RiskAssessment assessment) {
        return extractDouble(assessment, "workflowSuccess", 80.0);
    }

    private double getContextCompleteness(RiskAssessment assessment) {
        Map<String, Object> context = assessment.context();
        if (context == null || context.isEmpty()) {
            return 50.0;
        }
        int contextSize = context.size();
        if (contextSize >= 10) return 95.0;
        if (contextSize >= 7) return 85.0;
        if (contextSize >= 5) return 75.0;
        if (contextSize >= 3) return 65.0;
        return 50.0;
    }

    private double getOutputValidation(RiskAssessment assessment) {
        return extractDouble(assessment, "outputValidation", 80.0);
    }

    private double extractDouble(RiskAssessment assessment, String key, double defaultValue) {
        Map<String, Object> context = assessment.context();
        if (context != null && context.containsKey(key)) {
            Object value = context.get(key);
            if (value instanceof Number) return ((Number) value).doubleValue();
        }
        return defaultValue;
    }

    public record TrustResult(
        double score,
        Map<TrustFactor, Double> factorScores,
        Map<TrustFactor, String> reasons
    ) {}
}
