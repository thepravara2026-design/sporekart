package com.sporekart.risk.application.service;

import com.sporekart.risk.application.dto.CreateRiskAssessmentRequest;
import com.sporekart.risk.application.dto.RiskAssessmentResponse;
import com.sporekart.risk.common.exception.RiskAssessmentNotFoundException;
import com.sporekart.risk.domain.model.RiskAssessment;
import com.sporekart.risk.domain.model.RiskLevel;
import com.sporekart.risk.domain.model.RiskStatus;
import com.sporekart.risk.domain.repository.RiskRepositoryPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RiskService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RiskService.class);

    private final RiskRepositoryPort riskRepositoryPort;

    public RiskService(RiskRepositoryPort riskRepositoryPort) {
        this.riskRepositoryPort = riskRepositoryPort;
    }

    public RiskAssessmentResponse assessRisk(CreateRiskAssessmentRequest request) {
        RiskAssessment assessment = RiskAssessment.create(
                request.entityType(),
                request.entityId(),
                request.riskScore(),
                request.factors(),
                request.assessedBy());
        RiskAssessment saved = riskRepositoryPort.save(assessment);
        LOGGER.info("Risk assessment created: id={}, score={}, level={}", saved.getId(), saved.getRiskScore(), saved.getRiskLevel());
        return RiskAssessmentResponse.from(saved);
    }

    public RiskAssessmentResponse getAssessment(String id) {
        RiskAssessment assessment = riskRepositoryPort.findById(id)
                .orElseThrow(() -> new RiskAssessmentNotFoundException("Risk assessment not found: " + id));
        return RiskAssessmentResponse.from(assessment);
    }

    public List<RiskAssessmentResponse> getAssessmentsByEntity(String entityType, String entityId) {
        return riskRepositoryPort.findByEntity(entityType, entityId).stream()
                .map(RiskAssessmentResponse::from)
                .toList();
    }

    public List<RiskAssessmentResponse> getAssessmentsByLevel(RiskLevel riskLevel) {
        return riskRepositoryPort.findAllByRiskLevel(riskLevel).stream()
                .map(RiskAssessmentResponse::from)
                .toList();
    }

    public RiskAssessmentResponse mitigateRisk(String id) {
        RiskAssessment assessment = riskRepositoryPort.findById(id)
                .orElseThrow(() -> new RiskAssessmentNotFoundException("Risk assessment not found: " + id));
        if (assessment.getStatus() == RiskStatus.MITIGATED) {
            throw new IllegalStateException("Risk assessment is already mitigated: " + id);
        }
        RiskAssessment updated = assessment.withStatus(RiskStatus.MITIGATED);
        RiskAssessment saved = riskRepositoryPort.save(updated);
        LOGGER.info("Risk assessment mitigated: id={}", saved.getId());
        return RiskAssessmentResponse.from(saved);
    }

    public RiskAssessmentResponse escalateRisk(String id, String reason) {
        if (reason == null || reason.isBlank()) {
            throw new IllegalArgumentException("Escalation reason is required");
        }
        RiskAssessment assessment = riskRepositoryPort.findById(id)
                .orElseThrow(() -> new RiskAssessmentNotFoundException("Risk assessment not found: " + id));
        RiskAssessment updated = assessment.withStatus(RiskStatus.ESCALATED);
        RiskAssessment saved = riskRepositoryPort.save(updated);
        LOGGER.info("Risk assessment escalated: id={}, reason={}", saved.getId(), reason);
        return RiskAssessmentResponse.from(saved);
    }

    public List<RiskAssessmentResponse> listAll() {
        return riskRepositoryPort.findAll().stream()
                .map(RiskAssessmentResponse::from)
                .toList();
    }
}