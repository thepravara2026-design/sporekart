package com.sporekart.ai.policy.engine;
import com.sporekart.ai.policy.domain.*;
import org.junit.jupiter.api.Test;
import java.time.OffsetDateTime;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class ConditionEvaluatorTest {
    private final ConditionEvaluator evaluator = new ConditionEvaluator();

    @Test void testEvaluate_Equals() {
        PolicyCondition c = new PolicyCondition(UUID.randomUUID(), UUID.randomUUID(), "module",
            ConditionOperator.EQUALS, "content", false, 1, OffsetDateTime.now());
        EvaluationRequest req = new EvaluationRequest(UUID.randomUUID(), "content", "read", Map.of(), Map.of(), "u", List.of(), Map.of(), OffsetDateTime.now());
        assertTrue(evaluator.evaluate(c, req, null));
    }

    @Test void testEvaluate_NotEquals() {
        PolicyCondition c = new PolicyCondition(UUID.randomUUID(), UUID.randomUUID(), "module",
            ConditionOperator.NOT_EQUALS, "other", false, 1, OffsetDateTime.now());
        EvaluationRequest req = new EvaluationRequest(UUID.randomUUID(), "content", "read", Map.of(), Map.of(), "u", List.of(), Map.of(), OffsetDateTime.now());
        assertTrue(evaluator.evaluate(c, req, null));
    }

    @Test void testEvaluate_Contains() {
        PolicyCondition c = new PolicyCondition(UUID.randomUUID(), UUID.randomUUID(), "action",
            ConditionOperator.CONTAINS, "read", false, 1, OffsetDateTime.now());
        EvaluationRequest req = new EvaluationRequest(UUID.randomUUID(), "content", "read_write", Map.of(), Map.of(), "u", List.of(), Map.of(), OffsetDateTime.now());
        assertTrue(evaluator.evaluate(c, req, null));
    }

    @Test void testEvaluate_Exists() {
        PolicyCondition c = new PolicyCondition(UUID.randomUUID(), UUID.randomUUID(), "module",
            ConditionOperator.EXISTS, null, false, 1, OffsetDateTime.now());
        EvaluationRequest req = new EvaluationRequest(UUID.randomUUID(), "content", "read", Map.of(), Map.of(), "u", List.of(), Map.of(), OffsetDateTime.now());
        assertTrue(evaluator.evaluate(c, req, null));
    }

    @Test void testEvaluate_NotExists() {
        PolicyCondition c = new PolicyCondition(UUID.randomUUID(), UUID.randomUUID(), "nonexistent",
            ConditionOperator.NOT_EXISTS, null, false, 1, OffsetDateTime.now());
        EvaluationRequest req = new EvaluationRequest(UUID.randomUUID(), "content", "read", Map.of(), Map.of(), "u", List.of(), Map.of(), OffsetDateTime.now());
        assertTrue(evaluator.evaluate(c, req, null));
    }

    @Test void testEvaluate_Negate() {
        PolicyCondition c = new PolicyCondition(UUID.randomUUID(), UUID.randomUUID(), "module",
            ConditionOperator.EQUALS, "other", true, 1, OffsetDateTime.now());
        EvaluationRequest req = new EvaluationRequest(UUID.randomUUID(), "content", "read", Map.of(), Map.of(), "u", List.of(), Map.of(), OffsetDateTime.now());
        assertTrue(evaluator.evaluate(c, req, null));
    }

    @Test void testEvaluateAll_Empty() { assertTrue(evaluator.evaluateAll(List.of(), null, null)); }
    @Test void testEvaluateAll_Null() { assertTrue(evaluator.evaluateAll(null, null, null)); }
    @Test void testEvaluateAny_Empty() { assertTrue(evaluator.evaluateAny(List.of(), null, null)); }
}
