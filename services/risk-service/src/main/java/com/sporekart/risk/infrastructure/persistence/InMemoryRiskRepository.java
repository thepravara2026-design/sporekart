package com.sporekart.risk.infrastructure.persistence;

import com.sporekart.risk.domain.model.RiskAssessment;
import com.sporekart.risk.domain.model.RiskLevel;
import com.sporekart.risk.domain.repository.RiskRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryRiskRepository implements RiskRepositoryPort {

    private final Map<String, RiskAssessment> store = new ConcurrentHashMap<>();

    @Override
    public RiskAssessment save(RiskAssessment assessment) {
        store.put(assessment.getId(), assessment);
        return assessment;
    }

    @Override
    public Optional<RiskAssessment> findById(String id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<RiskAssessment> findByEntity(String entityType, String entityId) {
        return store.values().stream()
                .filter(a -> a.getEntityType().equals(entityType) && a.getEntityId().equals(entityId))
                .toList();
    }

    @Override
    public List<RiskAssessment> findAllByRiskLevel(RiskLevel riskLevel) {
        return store.values().stream()
                .filter(a -> a.getRiskLevel() == riskLevel)
                .toList();
    }

    @Override
    public List<RiskAssessment> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public void deleteById(String id) {
        store.remove(id);
    }
}