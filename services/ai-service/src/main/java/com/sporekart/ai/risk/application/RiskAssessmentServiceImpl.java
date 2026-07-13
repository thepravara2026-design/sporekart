package com.sporekart.ai.risk.application;

import com.sporekart.ai.risk.api.RiskAssessmentService;
import com.sporekart.ai.risk.domain.*;
import com.sporekart.ai.risk.infrastructure.persistence.RiskAssessmentEntity;
import com.sporekart.ai.risk.infrastructure.persistence.RiskAssessmentRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class RiskAssessmentServiceImpl implements RiskAssessmentService {

    private final RiskAssessmentRepository riskAssessmentRepository;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional
    public RiskAssessment createAssessment(String module, String action, Map<String, Object> context) {
        var entity = new RiskAssessmentEntity();
        entity.setId(UUID.randomUUID());
        entity.setModule(module);
        entity.setAction(action);
        entity.setStatus(RiskAssessmentStatus.PENDING.name());
        entity.setContext(toJson(context));
        entity.setAssessedAt(LocalDateTime.now());
        var saved = riskAssessmentRepository.save(entity);
        log.info("Risk assessment created: {} for module {} action {}", saved.getId(), module, action);
        return toDomain(saved);
    }

    @Override
    public RiskAssessment getAssessment(UUID id) {
        return riskAssessmentRepository.findById(id)
            .map(this::toDomain)
            .orElseThrow(() -> new RuntimeException("Risk assessment not found: " + id));
    }

    @Override
    public List<RiskAssessment> getAssessmentsByModule(String module) {
        return riskAssessmentRepository.findByModule(module).stream()
            .map(this::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public List<RiskAssessment> getAssessmentsByStatus(RiskAssessmentStatus status) {
        return riskAssessmentRepository.findByStatus(status.name()).stream()
            .map(this::toDomain)
            .collect(Collectors.toList());
    }

    @Transactional
    public void completeAssessment(UUID assessmentId) {
        var entity = riskAssessmentRepository.findById(assessmentId)
            .orElseThrow(() -> new RuntimeException("Risk assessment not found: " + assessmentId));
        entity.setStatus(RiskAssessmentStatus.COMPLETED.name());
        entity.setCompletedAt(LocalDateTime.now());
        riskAssessmentRepository.save(entity);
        log.info("Risk assessment completed: {}", assessmentId);
    }

    private RiskAssessment toDomain(RiskAssessmentEntity entity) {
        return new RiskAssessment(
            entity.getId(), entity.getModule(), entity.getAction(),
            RiskAssessmentStatus.valueOf(entity.getStatus()),
            fromJson(entity.getContext(), new TypeReference<Map<String, Object>>() {}),
            entity.getReviewerId(),
            entity.getAssessedAt() != null ? entity.getAssessedAt().toInstant(ZoneOffset.UTC) : null,
            entity.getCompletedAt() != null ? entity.getCompletedAt().toInstant(ZoneOffset.UTC) : null
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
