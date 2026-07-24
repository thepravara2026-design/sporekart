package com.sporekart.risk.infrastructure.persistence;

import com.sporekart.risk.domain.model.RiskAssessment;
import com.sporekart.risk.domain.model.RiskLevel;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InMemoryRiskRepositoryTest {

    private final InMemoryRiskRepository repository = new InMemoryRiskRepository();

    @Test
    void saveAndFindByIdRoundTrip() {
        RiskAssessment assessment = RiskAssessment.create("ORDER", "ord-1", 25, List.of("Flag A"), "user-1");

        RiskAssessment saved = repository.save(assessment);

        assertEquals(saved.getId(), repository.findById(saved.getId()).orElseThrow().getId());
    }

    @Test
    void findByEntityReturnsMatchingAssessments() {
        RiskAssessment a1 = RiskAssessment.create("ORDER", "ord-1", 10, List.of("F1"), "user-1");
        RiskAssessment a2 = RiskAssessment.create("ORDER", "ord-1", 40, List.of("F2"), "user-2");
        RiskAssessment a3 = RiskAssessment.create("PAYMENT", "pay-1", 80, List.of("F3"), "user-1");
        repository.save(a1);
        repository.save(a2);
        repository.save(a3);

        List<RiskAssessment> results = repository.findByEntity("ORDER", "ord-1");

        assertEquals(2, results.size());
    }

    @Test
    void findAllByRiskLevelFiltersCorrectly() {
        RiskAssessment low = RiskAssessment.create("ORDER", "ord-1", 10, List.of("F1"), "user-1");
        RiskAssessment high = RiskAssessment.create("ORDER", "ord-2", 70, List.of("F2"), "user-2");
        repository.save(low);
        repository.save(high);

        List<RiskAssessment> results = repository.findAllByRiskLevel(RiskLevel.HIGH);

        assertEquals(1, results.size());
        assertEquals(RiskLevel.HIGH, results.get(0).getRiskLevel());
    }

    @Test
    void deleteByIdRemovesAssessment() {
        RiskAssessment assessment = RiskAssessment.create("ORDER", "ord-1", 50, List.of("F1"), "user-1");
        repository.save(assessment);

        repository.deleteById(assessment.getId());

        assertTrue(repository.findById(assessment.getId()).isEmpty());
    }
}