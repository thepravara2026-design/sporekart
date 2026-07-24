package com.sporekart.risk.interfaces.rest;

import com.sporekart.risk.application.dto.CreateRiskAssessmentRequest;
import com.sporekart.risk.application.dto.RiskAssessmentResponse;
import com.sporekart.risk.application.service.RiskService;
import com.sporekart.risk.domain.model.RiskLevel;
import com.sporekart.risk.domain.model.RiskStatus;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class RiskControllerTest {

    private final RiskService riskService = new RiskService(new com.sporekart.risk.infrastructure.persistence.InMemoryRiskRepository());
    private final RiskController controller = new RiskController(riskService);

    @Test
    void createAndGetByIdRoundTrip() {
        var request = new CreateRiskAssessmentRequest("ORDER", "ord-1", 50,
                List.of("Suspicious activity"), "user-1");

        var created = controller.create(request).getBody();
        assertNotNull(created);
        assertNotNull(created.id());

        var fetched = controller.getById(created.id()).getBody();
        assertNotNull(fetched);
        assertEquals(created.id(), fetched.id());
    }

    @Test
    void listReturnsAllAssessments() {
        controller.create(new CreateRiskAssessmentRequest("ORDER", "ord-1", 10, List.of("F1"), "user-1"));
        controller.create(new CreateRiskAssessmentRequest("PAYMENT", "pay-1", 80, List.of("F2"), "user-2"));

        var all = controller.list(null, null, null).getBody();
        assertNotNull(all);
        assertEquals(2, all.size());
    }

    @Test
    void mitigateChangesStatus() {
        var created = controller.create(
                new CreateRiskAssessmentRequest("ORDER", "ord-1", 30, List.of("Factor X"), "user-1")).getBody();
        assertNotNull(created);

        var mitigated = controller.mitigate(created.id()).getBody();
        assertNotNull(mitigated);
        assertEquals(RiskStatus.MITIGATED, mitigated.status());
    }

    @Test
    void escalateChangesStatus() {
        var created = controller.create(
                new CreateRiskAssessmentRequest("ORDER", "ord-1", 70, List.of("High value"), "user-1")).getBody();
        assertNotNull(created);

        var escalated = controller.escalate(created.id(), java.util.Map.of("reason", "Needs review")).getBody();
        assertNotNull(escalated);
        assertEquals(RiskStatus.ESCALATED, escalated.status());
    }
}