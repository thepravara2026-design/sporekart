package com.sporekart.ai.risk.application;

import com.sporekart.ai.risk.api.RiskRecommendationService;
import com.sporekart.ai.risk.domain.*;
import com.sporekart.ai.risk.infrastructure.persistence.RiskRecommendationEntity;
import com.sporekart.ai.risk.infrastructure.persistence.RiskRecommendationRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class RiskRecommendationServiceImpl implements RiskRecommendationService {

    private final RiskRecommendationRepository riskRecommendationRepository;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional
    public RiskRecommendation generateRecommendation(RiskAssessment assessment, RiskScore score,
                                                      TrustAssessment trust, ConfidenceScore confidence) {
        RecommendationType type;
        String title;
        String description;
        int priority;
        Map<String, Object> details = new HashMap<>();

        double avgTrustConfidence = (trust.overallTrustScore() + confidence.overallConfidence()) / 2.0;

        switch (score.riskLevel()) {
            case LOW -> {
                if (avgTrustConfidence >= 60) {
                    type = RecommendationType.PROCEED;
                    title = "Proceed with execution";
                    description = "Low risk with adequate trust and confidence levels";
                    priority = 1;
                } else {
                    type = RecommendationType.RETRY;
                    title = "Retry with validation";
                    description = "Low risk but low trust/confidence, recommend validation";
                    priority = 2;
                }
            }
            case MEDIUM -> {
                if (avgTrustConfidence >= 70) {
                    type = RecommendationType.PROCEED;
                    title = "Proceed with caution";
                    description = "Medium risk with good trust/confidence, proceed with monitoring";
                    priority = 2;
                } else if (avgTrustConfidence >= 50) {
                    type = RecommendationType.RETRY;
                    title = "Retry with reduced context";
                    description = "Medium risk and moderate trust/confidence, consider reducing context";
                    priority = 3;
                } else {
                    type = RecommendationType.USE_ALTERNATE_PROVIDER;
                    title = "Use alternative provider";
                    description = "Medium risk with low trust/confidence, consider alternative provider";
                    priority = 3;
                }
            }
            case HIGH -> {
                if (avgTrustConfidence >= 80) {
                    type = RecommendationType.REQUIRE_APPROVAL;
                    title = "Require human approval";
                    description = "High risk, requires human review before proceeding";
                    priority = 4;
                } else if (avgTrustConfidence >= 50) {
                    type = RecommendationType.REDUCE_CONTEXT;
                    title = "Reduce context and retry";
                    description = "High risk with moderate trust, reduce context scope and retry";
                    priority = 4;
                } else {
                    type = RecommendationType.REQUEST_HUMAN_REVIEW;
                    title = "Request human review";
                    description = "High risk with low trust/confidence, escalate for human review";
                    priority = 5;
                }
            }
            case CRITICAL -> {
                type = RecommendationType.BLOCK_EXECUTION;
                title = "Block execution";
                description = "Critical risk level, execution is blocked automatically";
                priority = 5;
            }
            default -> {
                type = RecommendationType.REQUIRE_APPROVAL;
                title = "Require approval";
                description = "Unknown risk level, require manual approval";
                priority = 3;
            }
        }

        details.put("riskScore", score.overallScore());
        details.put("trustScore", trust.overallTrustScore());
        details.put("confidenceScore", confidence.overallConfidence());
        details.put("avgTrustConfidence", avgTrustConfidence);
        details.put("riskLevel", score.riskLevel().name());

        RiskRecommendation recommendation = new RiskRecommendation(
            UUID.randomUUID(), assessment.id(), type, title, description,
            details, priority, Instant.now()
        );

        saveRecommendation(recommendation);
        log.info("Recommendation generated for assessment {}: {}", assessment.id(), type);
        return recommendation;
    }

    @Override
    public List<RiskRecommendation> getRecommendations(UUID assessmentId) {
        return riskRecommendationRepository.findByAssessmentId(assessmentId).stream()
            .map(this::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public RecommendationType getRecommendedAction(UUID assessmentId) {
        return riskRecommendationRepository.findByAssessmentId(assessmentId).stream()
            .findFirst()
            .map(e -> RecommendationType.valueOf(e.getType()))
            .orElse(RecommendationType.PROCEED);
    }

    private void saveRecommendation(RiskRecommendation recommendation) {
        var entity = new RiskRecommendationEntity();
        entity.setId(recommendation.id());
        entity.setAssessmentId(recommendation.assessmentId());
        entity.setType(recommendation.type().name());
        entity.setTitle(recommendation.title());
        entity.setDescription(recommendation.description());
        entity.setDetails(toJson(recommendation.details()));
        entity.setPriority(recommendation.priority());
        entity.setGeneratedAt(LocalDateTime.now());
        riskRecommendationRepository.save(entity);
    }

    private RiskRecommendation toDomain(RiskRecommendationEntity entity) {
        return new RiskRecommendation(
            entity.getId(), entity.getAssessmentId(),
            RecommendationType.valueOf(entity.getType()),
            entity.getTitle(), entity.getDescription(),
            fromJson(entity.getDetails(), new TypeReference<Map<String, Object>>() {}),
            entity.getPriority(),
            entity.getGeneratedAt() != null ? entity.getGeneratedAt().toInstant(ZoneOffset.UTC) : null
        );
    }

    private String toJson(Object value) {
        try {
            return value == null ? null : objectMapper.writeValueAsString(value);
        } catch (Exception e) {
            throw new RuntimeException("JSON conversion error", e);
        }
    }

    private <T> T fromJson(String json, TypeReference<T> type) {
        try {
            return json == null ? null : objectMapper.readValue(json, type);
        } catch (Exception e) {
            return null;
        }
    }
}
