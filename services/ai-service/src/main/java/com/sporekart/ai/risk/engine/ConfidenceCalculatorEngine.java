package com.sporekart.ai.risk.engine;

import com.sporekart.ai.risk.domain.ConfidenceFactor;
import com.sporekart.ai.risk.domain.RiskAssessment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Slf4j
public class ConfidenceCalculatorEngine {

    private static final Map<ConfidenceFactor, Double> DEFAULT_WEIGHTS = Map.ofEntries(
        Map.entry(ConfidenceFactor.KNOWLEDGE_MATCH, 0.20),
        Map.entry(ConfidenceFactor.SEMANTIC_SIMILARITY, 0.18),
        Map.entry(ConfidenceFactor.PROMPT_QUALITY, 0.17),
        Map.entry(ConfidenceFactor.CONVERSATION_CONTEXT, 0.15),
        Map.entry(ConfidenceFactor.WORKFLOW_SUCCESS, 0.15),
        Map.entry(ConfidenceFactor.PROVIDER_METADATA, 0.15)
    );

    public ConfidenceResult calculate(RiskAssessment assessment) {
        Map<ConfidenceFactor, Double> factorScores = new EnumMap<>(ConfidenceFactor.class);
        StringBuilder explanationBuilder = new StringBuilder();

        double knowledgeMatch = getKnowledgeMatch(assessment);
        factorScores.put(ConfidenceFactor.KNOWLEDGE_MATCH, knowledgeMatch);
        explanationBuilder.append("KnowledgeMatch=").append(knowledgeMatch).append("; ");

        double semanticSimilarity = getSemanticSimilarity(assessment);
        factorScores.put(ConfidenceFactor.SEMANTIC_SIMILARITY, semanticSimilarity);
        explanationBuilder.append("SemanticSimilarity=").append(semanticSimilarity).append("; ");

        double promptQuality = getPromptQuality(assessment);
        factorScores.put(ConfidenceFactor.PROMPT_QUALITY, promptQuality);
        explanationBuilder.append("PromptQuality=").append(promptQuality).append("; ");

        double conversationContext = getConversationContext(assessment);
        factorScores.put(ConfidenceFactor.CONVERSATION_CONTEXT, conversationContext);
        explanationBuilder.append("ConversationContext=").append(conversationContext).append("; ");

        double workflowSuccess = getWorkflowSuccess(assessment);
        factorScores.put(ConfidenceFactor.WORKFLOW_SUCCESS, workflowSuccess);
        explanationBuilder.append("WorkflowSuccess=").append(workflowSuccess).append("; ");

        double providerMetadata = getProviderMetadata(assessment);
        factorScores.put(ConfidenceFactor.PROVIDER_METADATA, providerMetadata);
        explanationBuilder.append("ProviderMetadata=").append(providerMetadata);

        double weightedSum = 0.0;
        double totalWeight = 0.0;
        for (Map.Entry<ConfidenceFactor, Double> entry : factorScores.entrySet()) {
            double weight = DEFAULT_WEIGHTS.getOrDefault(entry.getKey(), 0.15);
            weightedSum += entry.getValue() * weight;
            totalWeight += weight;
        }

        double overallScore = totalWeight > 0 ? Math.min(100, Math.max(0, weightedSum / totalWeight)) : 0.0;
        String explanation = explanationBuilder.toString();

        log.debug("Confidence score calculated: {}", overallScore);
        return new ConfidenceResult(overallScore, factorScores, explanation);
    }

    private double getKnowledgeMatch(RiskAssessment assessment) {
        return extractDouble(assessment, "knowledgeMatch", 75.0);
    }

    private double getSemanticSimilarity(RiskAssessment assessment) {
        return extractDouble(assessment, "semanticSimilarity", 75.0);
    }

    private double getPromptQuality(RiskAssessment assessment) {
        Map<String, Object> context = assessment.context();
        if (context != null && context.containsKey("promptQuality")) {
            return extractDouble(assessment, "promptQuality", 80.0);
        }
        return 80.0;
    }

    private double getConversationContext(RiskAssessment assessment) {
        Map<String, Object> context = assessment.context();
        if (context != null && context.containsKey("conversationDepth")) {
            int depth = ((Number) context.getOrDefault("conversationDepth", 0)).intValue();
            if (depth >= 10) return 90.0;
            if (depth >= 5) return 75.0;
            if (depth >= 3) return 60.0;
        }
        return 70.0;
    }

    private double getWorkflowSuccess(RiskAssessment assessment) {
        return extractDouble(assessment, "workflowSuccess", 80.0);
    }

    private double getProviderMetadata(RiskAssessment assessment) {
        return extractDouble(assessment, "providerMetadata", 70.0);
    }

    private double extractDouble(RiskAssessment assessment, String key, double defaultValue) {
        Map<String, Object> context = assessment.context();
        if (context != null && context.containsKey(key)) {
            Object value = context.get(key);
            if (value instanceof Number) return ((Number) value).doubleValue();
        }
        return defaultValue;
    }

    public record ConfidenceResult(
        double score,
        Map<ConfidenceFactor, Double> factorScores,
        String explanation
    ) {}
}
