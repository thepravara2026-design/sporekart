package com.sporekart.ai.risk.application;

import com.sporekart.ai.risk.api.TrustScoreService;
import com.sporekart.ai.risk.api.TrustEngine;
import com.sporekart.ai.risk.domain.*;
import com.sporekart.ai.risk.infrastructure.persistence.TrustScoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class TrustScoreServiceImpl implements TrustScoreService {

    private final TrustEngine trustEngine;
    private final TrustScoreRepository trustScoreRepository;

    @Override
    public TrustAssessment calculateTrustScore(UUID assessmentId) {
        var entities = trustScoreRepository.findByAssessmentId(assessmentId);
        if (entities.isEmpty()) {
            throw new RuntimeException("Trust score not found for assessment: " + assessmentId);
        }
        var entity = entities.get(0);
        return new TrustAssessment(
            entity.getId(), entity.getAssessmentId(), entity.getOverallTrustScore(),
            new HashMap<>(), new HashMap<>(),
            entity.getCalculatedAt() != null ? entity.getCalculatedAt().toInstant(java.time.ZoneOffset.UTC) : null
        );
    }

    @Override
    public Map<TrustFactor, Double> getFactorDetails(UUID assessmentId) {
        var entities = trustScoreRepository.findByAssessmentId(assessmentId);
        if (entities.isEmpty()) {
            return Map.of();
        }
        return trustEngine.calculateTrust(
            new RiskAssessment(assessmentId, null, null, null, null, null, null, null)
        ).factorScores();
    }

    @Override
    public double getFactorScore(UUID assessmentId, String factor) {
        var entities = trustScoreRepository.findByAssessmentId(assessmentId);
        if (entities.isEmpty()) {
            return 0.0;
        }
        try {
            TrustFactor trustFactor = TrustFactor.valueOf(factor);
            var trustAssessment = trustEngine.calculateTrust(
                new RiskAssessment(assessmentId, null, null, null, null, null, null, null)
            );
            return trustAssessment.factorScores().getOrDefault(trustFactor, 0.0);
        } catch (IllegalArgumentException e) {
            log.warn("Invalid trust factor: {}", factor);
            return 0.0;
        }
    }
}
