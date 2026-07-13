package com.sporekart.ai.policy.application;
import com.sporekart.ai.policy.domain.*;
import org.junit.jupiter.api.Test;
import java.time.OffsetDateTime;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class PolicyValidatorImplTest {
    private final PolicyValidatorImpl validator = new PolicyValidatorImpl();

    @Test void testValidateRequest_Valid() {
        assertTrue(validator.validateRequest(new EvaluationRequest(UUID.randomUUID(), "content", "generate", Map.of(), Map.of(), "user1", List.of(), Map.of(), OffsetDateTime.now())).isEmpty());
    }
    @Test void testValidateRequest_MissingModule() {
        assertFalse(validator.validateRequest(new EvaluationRequest(UUID.randomUUID(), "", "generate", Map.of(), Map.of(), "user1", List.of(), Map.of(), OffsetDateTime.now())).isEmpty());
    }
    @Test void testValidateRequest_MissingAction() {
        assertFalse(validator.validateRequest(new EvaluationRequest(UUID.randomUUID(), "content", "", Map.of(), Map.of(), "user1", List.of(), Map.of(), OffsetDateTime.now())).isEmpty());
    }
    @Test void testValidatePolicy_Valid() {
        Policy p = new Policy(UUID.randomUUID(), "name", "desc", PolicyType.GLOBAL, PolicyStatus.ACTIVE,
            PolicySeverity.INFO, PolicyScope.GLOBAL, 0, null, List.of(), List.of(), Map.of(), true, false, null, null, null);
        assertTrue(validator.validatePolicy(p).isEmpty());
    }
    @Test void testValidatePolicy_MissingName() {
        Policy p = new Policy(UUID.randomUUID(), "", "desc", PolicyType.GLOBAL, PolicyStatus.ACTIVE,
            PolicySeverity.INFO, PolicyScope.GLOBAL, 0, null, List.of(), List.of(), Map.of(), true, false, null, null, null);
        assertFalse(validator.validatePolicy(p).isEmpty());
    }
    @Test void testIsValidPolicy() {
        Policy valid = new Policy(UUID.randomUUID(), "name", null, PolicyType.GLOBAL, PolicyStatus.ACTIVE,
            PolicySeverity.INFO, PolicyScope.GLOBAL, 0, null, List.of(), List.of(), Map.of(), true, false, null, null, null);
        assertTrue(validator.isValidPolicy(valid));
    }
    @Test void testIsValidRequest() {
        EvaluationRequest r = new EvaluationRequest(UUID.randomUUID(), "mod", "act", Map.of(), Map.of(), "u", List.of(), Map.of(), OffsetDateTime.now());
        assertTrue(validator.isValidRequest(r));
    }
}
