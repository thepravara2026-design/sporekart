package com.sporekart.ai.policy.engine;

import com.sporekart.ai.policy.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConditionEvaluator {

    public boolean evaluate(PolicyCondition condition, EvaluationRequest request, PolicyContext context) {
        if (condition == null) return true;
        Object actualValue = resolveValue(condition.field(), request, context);
        boolean result = switch (condition.operator()) {
            case EQUALS -> equals(actualValue, condition.value());
            case NOT_EQUALS -> !equals(actualValue, condition.value());
            case CONTAINS -> contains(actualValue, condition.value());
            case NOT_CONTAINS -> !contains(actualValue, condition.value());
            case EXISTS -> actualValue != null;
            case NOT_EXISTS -> actualValue == null;
            case IN -> in(actualValue, condition.value());
            case NOT_IN -> !in(actualValue, condition.value());
            case MATCHES -> matches(actualValue, condition.value());
            case STARTS_WITH -> startsWith(actualValue, condition.value());
            case ENDS_WITH -> endsWith(actualValue, condition.value());
            case GREATER_THAN -> compare(actualValue, condition.value()) > 0;
            case LESS_THAN -> compare(actualValue, condition.value()) < 0;
            case GREATER_EQUALS -> compare(actualValue, condition.value()) >= 0;
            case LESS_EQUALS -> compare(actualValue, condition.value()) <= 0;
        };
        return condition.negate() != result;
    }

    public boolean evaluateAll(List<PolicyCondition> conditions, EvaluationRequest request, PolicyContext context) {
        if (conditions == null || conditions.isEmpty()) return true;
        return conditions.stream().allMatch(c -> evaluate(c, request, context));
    }

    public boolean evaluateAny(List<PolicyCondition> conditions, EvaluationRequest request, PolicyContext context) {
        if (conditions == null || conditions.isEmpty()) return true;
        return conditions.stream().anyMatch(c -> evaluate(c, request, context));
    }

    private Object resolveValue(String field, EvaluationRequest request, PolicyContext context) {
        if (field == null) return null;
        return switch (field.toLowerCase()) {
            case "module" -> request.module();
            case "action" -> request.action();
            case "userid", "user_id" -> request.userId();
            case "roles" -> request.roles();
            default -> request.payload() != null ? request.payload().get(field) :
                       context != null ? context.resource().get(field) : null;
        };
    }

    private boolean equals(Object a, Object b) {
        if (a == null && b == null) return true;
        if (a == null || b == null) return false;
        return a.toString().equalsIgnoreCase(b.toString());
    }

    private boolean contains(Object container, Object value) {
        if (container == null || value == null) return false;
        return container.toString().toLowerCase().contains(value.toString().toLowerCase());
    }

    private boolean in(Object value, Object collection) {
        if (value == null || collection == null) return false;
        String colStr = collection.toString().toLowerCase();
        return colStr.contains(value.toString().toLowerCase());
    }

    private boolean matches(Object value, Object pattern) {
        if (value == null || pattern == null) return false;
        return value.toString().toLowerCase().matches(pattern.toString().toLowerCase());
    }

    private boolean startsWith(Object value, Object prefix) {
        if (value == null || prefix == null) return false;
        return value.toString().toLowerCase().startsWith(prefix.toString().toLowerCase());
    }

    private boolean endsWith(Object value, Object suffix) {
        if (value == null || suffix == null) return false;
        return value.toString().toLowerCase().endsWith(suffix.toString().toLowerCase());
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private int compare(Object a, Object b) {
        if (a instanceof Number na && b instanceof Number nb) {
            return Double.compare(na.doubleValue(), nb.doubleValue());
        }
        if (a instanceof Comparable ca && b instanceof Comparable cb) {
            return ca.compareTo(cb);
        }
        return a.toString().compareTo(b.toString());
    }
}
