package com.sporekart.alert.application.engine;

import com.sporekart.alert.domain.model.BusinessRisk;
import com.sporekart.alert.domain.model.RiskCategory;
import com.sporekart.alert.domain.model.RiskSeverity;
import com.sporekart.alert.domain.repository.AlertRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class RiskEngineTest {

    @Mock private AlertRepositoryPort repository;
    private RiskEngine engine;

    @BeforeEach
    void setUp() {
        when(repository.saveRisk(any())).thenAnswer(i -> i.getArgument(0));
        engine = new RiskEngine(repository);
    }

    @Test
    void generateAllRisksShouldReturnRisks() {
        var risks = engine.generateAllRisks();
        assertNotNull(risks);
        assertFalse(risks.isEmpty());
        assertTrue(risks.size() >= 10);
    }

    @Test
    void getRiskSummaryShouldReturnSummaryMap() {
        var risk1 = BusinessRisk.create("R1", "D1", RiskCategory.REVENUE, RiskSeverity.CRITICAL, "REVENUE", "Impact", 0.8, 0.9, "Mitigate");
        var risk2 = BusinessRisk.create("R2", "D2", RiskCategory.INVENTORY, RiskSeverity.HIGH, "INVENTORY", "Impact", 0.6, 0.7, "Mitigate");
        when(repository.findAllRisks()).thenReturn(List.of(risk1, risk2));
        var summary = engine.getRiskSummary();
        assertEquals(2, summary.get("totalRisks"));
        assertEquals(1L, summary.get("critical"));
        assertEquals(1L, summary.get("high"));
    }

    @Test
    void getRiskSummaryShouldHaveAllKeys() {
        when(repository.findAllRisks()).thenReturn(List.of());
        var summary = engine.getRiskSummary();
        assertTrue(summary.containsKey("totalRisks"));
        assertTrue(summary.containsKey("critical"));
        assertTrue(summary.containsKey("high"));
        assertTrue(summary.containsKey("medium"));
        assertTrue(summary.containsKey("low"));
        assertTrue(summary.containsKey("averageRiskScore"));
    }
}
