package com.sporekart.alert.application.sdk;

import com.sporekart.alert.application.engine.RiskEngine;
import com.sporekart.alert.domain.model.*;
import com.sporekart.alert.domain.repository.AlertRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class RiskRuntime {

    private final RiskEngine riskEngine;
    private final AlertRepositoryPort repository;

    public RiskRuntime(RiskEngine riskEngine, AlertRepositoryPort repository) {
        this.riskEngine = riskEngine; this.repository = repository;
    }

    public List<BusinessRisk> generateAllRisks() {
        List<BusinessRisk> risks = riskEngine.generateAllRisks();
        risks.forEach(repository::saveRisk);
        return risks;
    }

    public List<BusinessRisk> getAllRisks() { return repository.findAllRisks(); }

    public Optional<BusinessRisk> findById(String id) { return repository.findRiskById(id); }

    public Map<String, Object> getRiskSummary() { return riskEngine.getRiskSummary(); }
}
