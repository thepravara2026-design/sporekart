package com.sporekart.ai.governance.application;

import com.sporekart.ai.governance.domain.*;
import org.junit.jupiter.api.Test;
import java.time.OffsetDateTime;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class GovernanceValidatorImplTest {

    private final GovernanceValidatorImpl validator = new GovernanceValidatorImpl();

    @Test
    void testValidateRequest_Valid() {
        GovernanceRequest request = new GovernanceRequest(UUID.randomUUID(), "content", "generate",
            Map.of(), Map.of(), "user1", List.of("admin"), OffsetDateTime.now());
        assertTrue(validator.validateRequest(request).isEmpty());
    }

    @Test
    void testValidateRequest_MissingModule() {
        GovernanceRequest request = new GovernanceRequest(UUID.randomUUID(), "", "generate",
            Map.of(), Map.of(), "user1", List.of(), OffsetDateTime.now());
        List<GovernanceViolation> violations = validator.validateRequest(request);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.ruleName().equals("missing_module")));
    }

    @Test
    void testValidateRequest_MissingAction() {
        GovernanceRequest request = new GovernanceRequest(UUID.randomUUID(), "content", "",
            Map.of(), Map.of(), "user1", List.of(), OffsetDateTime.now());
        List<GovernanceViolation> violations = validator.validateRequest(request);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.ruleName().equals("missing_action")));
    }

    @Test
    void testValidateRequest_MissingUser() {
        GovernanceRequest request = new GovernanceRequest(UUID.randomUUID(), "content", "generate",
            Map.of(), Map.of(), "", List.of(), OffsetDateTime.now());
        List<GovernanceViolation> violations = validator.validateRequest(request);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.ruleName().equals("missing_user")));
    }

    @Test
    void testValidatePolicy_Valid() {
        GovernancePolicy policy = new GovernancePolicy(UUID.randomUUID(), "test", "desc",
            GovernanceScope.ALL, GovernanceStatus.ACTIVE, 100, Map.of(), Map.of(),
            true, UUID.randomUUID(), OffsetDateTime.now(), OffsetDateTime.now());
        assertTrue(validator.validatePolicy(policy).isEmpty());
    }

    @Test
    void testValidatePolicy_MissingName() {
        GovernancePolicy policy = new GovernancePolicy(UUID.randomUUID(), "", "desc",
            GovernanceScope.ALL, GovernanceStatus.ACTIVE, 100, Map.of(), Map.of(),
            true, UUID.randomUUID(), OffsetDateTime.now(), OffsetDateTime.now());
        assertFalse(validator.validatePolicy(policy).isEmpty());
    }

    @Test
    void testIsValid() {
        GovernanceRequest valid = new GovernanceRequest(UUID.randomUUID(), "content", "generate",
            Map.of(), Map.of(), "user1", List.of(), OffsetDateTime.now());
        assertTrue(validator.isValid(valid));

        GovernanceRequest invalid = new GovernanceRequest(UUID.randomUUID(), "", "generate",
            Map.of(), Map.of(), "user1", List.of(), OffsetDateTime.now());
        assertFalse(validator.isValid(invalid));
    }
}
