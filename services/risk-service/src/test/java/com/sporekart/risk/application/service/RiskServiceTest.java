package com.sporekart.risk.application.service;

import com.sporekart.risk.application.dto.CreateRiskAssessmentRequest;
import com.sporekart.risk.application.dto.RiskAssessmentResponse;
import com.sporekart.risk.common.exception.RiskAssessmentNotFoundException;
import com.sporekart.risk.domain.model.RiskAssessment;
import com.sporekart.risk.domain.model.RiskLevel;
import com.sporekart.risk.domain.model.RiskStatus;
import com.sporekart.risk.domain.repository.RiskRepositoryPort;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class RiskServiceTest {

    private final RiskRepositoryPort repository = Mockito.mock(RiskRepositoryPort.class);
    private final RiskService service = new RiskService(repository);

    @Test
    void assessRiskPersistsAndReturnsResponse() {
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        RiskAssessmentResponse response = service.assessRisk(
                new CreateRiskAssessmentRequest("ORDER", "ord-123", 30,
                        List.of("Suspicious location", "High value"), "user-1"));

        assertNotNull(response);
        assertEquals("ORDER", response.entityType());
        assertEquals("ord-123", response.entityId());
        assertEquals(30, response.riskScore());
        assertEquals(RiskLevel.MEDIUM, response.riskLevel());
        assertEquals(RiskStatus.PENDING, response.status());
    }

    @Test
    void getAssessmentThrowsWhenNotFound() {
        when(repository.findById("bad-id")).thenReturn(Optional.empty());

        assertThrows(RiskAssessmentNotFoundException.class, () -> service.getAssessment("bad-id"));
    }

    @Test
    void mitigateRiskSucceedsWhenNotAlreadyMitigated() {
        RiskAssessment assessment = RiskAssessment.create("ORDER", "ord-1", 50,
                List.of("Factor A"), "user-1");
        when(repository.findById(assessment.getId())).thenReturn(Optional.of(assessment));
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        RiskAssessmentResponse response = service.mitigateRisk(assessment.getId());

        assertEquals(RiskStatus.MITIGATED, response.status());
    }

    @Test
    void mitigateRiskFailsWhenAlreadyMitigated() {
        RiskAssessment assessment = RiskAssessment.create("ORDER", "ord-1", 50,
                List.of("Factor A"), "user-1");
        RiskAssessment mitigated = assessment.withStatus(RiskStatus.MITIGATED);
        when(repository.findById(mitigated.getId())).thenReturn(Optional.of(mitigated));

        assertThrows(IllegalStateException.class, () -> service.mitigateRisk(mitigated.getId()));
    }

    @Test
    void escalateRiskRequiresReason() {
        RiskAssessment assessment = RiskAssessment.create("ORDER", "ord-1", 50,
                List.of("Factor A"), "user-1");
        when(repository.findById(assessment.getId())).thenReturn(Optional.of(assessment));

        assertThrows(IllegalArgumentException.class, () -> service.escalateRisk(assessment.getId(), ""));
    }

    @Test
    void escalateRiskSucceedsWithReason() {
        RiskAssessment assessment = RiskAssessment.create("ORDER", "ord-1", 50,
                List.of("Factor A"), "user-1");
        when(repository.findById(assessment.getId())).thenReturn(Optional.of(assessment));
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        RiskAssessmentResponse response = service.escalateRisk(assessment.getId(), "Manual review needed");

        assertEquals(RiskStatus.ESCALATED, response.status());
    }
}