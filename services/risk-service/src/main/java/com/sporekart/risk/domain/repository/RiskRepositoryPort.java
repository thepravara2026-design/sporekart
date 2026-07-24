package com.sporekart.risk.domain.repository;

import com.sporekart.risk.domain.model.RiskAssessment;
import com.sporekart.risk.domain.model.RiskLevel;

import java.util.List;
import java.util.Optional;

public interface RiskRepositoryPort {
    RiskAssessment save(RiskAssessment assessment);

    Optional<RiskAssessment> findById(String id);

    List<RiskAssessment> findByEntity(String entityType, String entityId);

    List<RiskAssessment> findAllByRiskLevel(RiskLevel riskLevel);

    List<RiskAssessment> findAll();

    void deleteById(String id);
}