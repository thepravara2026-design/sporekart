package com.sporekart.risk.infrastructure.persistence;

import com.sporekart.risk.domain.model.RiskAssessment;
import com.sporekart.risk.domain.model.RiskLevel;
import com.sporekart.risk.domain.repository.RiskRepositoryPort;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Primary
@Repository
@Transactional
public class JpaRiskRepositoryAdapter implements RiskRepositoryPort {

    private final RiskJpaRepository jpaRepository;

    public JpaRiskRepositoryAdapter(RiskJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public RiskAssessment save(RiskAssessment assessment) {
        return jpaRepository.save(RiskAssessmentEntity.fromDomain(assessment)).toDomain();
    }

    @Override
    public Optional<RiskAssessment> findById(String id) {
        return jpaRepository.findById(id).map(RiskAssessmentEntity::toDomain);
    }

    @Override
    public List<RiskAssessment> findByEntity(String entityType, String entityId) {
        return jpaRepository.findByEntityTypeAndEntityId(entityType, entityId).stream()
                .map(RiskAssessmentEntity::toDomain).toList();
    }

    @Override
    public List<RiskAssessment> findAllByRiskLevel(RiskLevel riskLevel) {
        return jpaRepository.findByRiskLevel(riskLevel).stream()
                .map(RiskAssessmentEntity::toDomain).toList();
    }

    @Override
    public List<RiskAssessment> findAll() {
        return jpaRepository.findAll().stream().map(RiskAssessmentEntity::toDomain).toList();
    }

    @Override
    public void deleteById(String id) {
        jpaRepository.deleteById(id);
    }
}
