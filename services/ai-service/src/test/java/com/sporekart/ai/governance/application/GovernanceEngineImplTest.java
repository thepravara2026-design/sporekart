package com.sporekart.ai.governance.application;

import com.sporekart.ai.governance.api.*;
import com.sporekart.ai.governance.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GovernanceEngineImplTest {

    @Mock private GovernanceContextResolver contextResolver;
    @Mock private GovernanceValidator validator;
    @Mock private GovernanceAuditService auditService;
    @Mock private GovernanceManager manager;

    private GovernanceEngineImpl engine;

    @BeforeEach
    void setUp() {
        engine = new GovernanceEngineImpl(contextResolver, validator, auditService, manager);
    }

    @Test
    void testValidate_AllowsValidRequest() {
        GovernanceRequest request = new GovernanceRequest(UUID.randomUUID(), "content", "generate",
            Map.of(), Map.of(), "user1", List.of("admin"), OffsetDateTime.now());
        GovernanceContext context = new GovernanceContext(UUID.randomUUID(), request.id(), "content", "generate",
            Map.of(), Map.of(), Map.of(), OffsetDateTime.now());

        when(contextResolver.resolveContext(request)).thenReturn(context);
        when(validator.validateRequest(request)).thenReturn(List.of());
        when(auditService.recordAudit(any())).thenReturn(null);

        GovernanceResponse response = engine.validate(request);

        assertEquals(GovernanceDecision.ALLOW, response.decision());
        assertTrue(response.violations().isEmpty());
        verify(auditService).recordAudit(any());
    }

    @Test
    void testValidate_DeniesRequestWithCriticalViolation() {
        GovernanceRequest request = new GovernanceRequest(UUID.randomUUID(), "content", "generate",
            Map.of(), Map.of(), "user1", List.of("admin"), OffsetDateTime.now());
        GovernanceContext context = new GovernanceContext(UUID.randomUUID(), request.id(), "content", "generate",
            Map.of(), Map.of(), Map.of(), OffsetDateTime.now());
        List<GovernanceViolation> violations = List.of(
            new GovernanceViolation(UUID.randomUUID(), "critical_rule", "Critical violation",
                GovernanceSeverity.CRITICAL, Map.of(), false, OffsetDateTime.now())
        );

        when(contextResolver.resolveContext(request)).thenReturn(context);
        when(validator.validateRequest(request)).thenReturn(violations);
        when(auditService.recordAudit(any())).thenReturn(null);

        GovernanceResponse response = engine.validate(request);

        assertEquals(GovernanceDecision.DENY, response.decision());
        assertEquals(1, response.violations().size());
    }

    @Test
    void testDecide_ReturnsAllowForNoViolations() {
        assertEquals(GovernanceDecision.ALLOW, engine.decide(null, List.of()));
        assertEquals(GovernanceDecision.ALLOW, engine.decide(null, null));
    }

    @Test
    void testDecide_ReturnsDenyForCritical() {
        List<GovernanceViolation> violations = List.of(
            new GovernanceViolation(UUID.randomUUID(), "r1", "critical",
                GovernanceSeverity.CRITICAL, Map.of(), false, OffsetDateTime.now())
        );
        assertEquals(GovernanceDecision.DENY, engine.decide(null, violations));
    }

    @Test
    void testDecide_ReturnsReviewForError() {
        List<GovernanceViolation> violations = List.of(
            new GovernanceViolation(UUID.randomUUID(), "r1", "error",
                GovernanceSeverity.ERROR, Map.of(), false, OffsetDateTime.now())
        );
        assertEquals(GovernanceDecision.REVIEW, engine.decide(null, violations));
    }

    @Test
    void testDecide_ReturnsLogForWarning() {
        List<GovernanceViolation> violations = List.of(
            new GovernanceViolation(UUID.randomUUID(), "r1", "warning",
                GovernanceSeverity.WARNING, Map.of(), true, OffsetDateTime.now())
        );
        assertEquals(GovernanceDecision.LOG, engine.decide(null, violations));
    }

    @Test
    void testIsAllowed() {
        GovernanceRequest request = new GovernanceRequest(UUID.randomUUID(), "content", "generate",
            Map.of(), Map.of(), "user1", List.of("admin"), OffsetDateTime.now());
        GovernanceContext context = new GovernanceContext(UUID.randomUUID(), request.id(), "content", "generate",
            Map.of(), Map.of(), Map.of(), OffsetDateTime.now());

        when(contextResolver.resolveContext(request)).thenReturn(context);
        when(validator.validateRequest(request)).thenReturn(List.of());
        when(auditService.recordAudit(any())).thenReturn(null);

        assertTrue(engine.isAllowed(request));
    }
}
