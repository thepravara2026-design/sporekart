package com.sporekart.ai.policy.application;

import com.sporekart.ai.policy.api.PolicyValidator;
import com.sporekart.ai.policy.domain.*;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.*;

@Service
public class PolicyValidatorImpl implements PolicyValidator {

    @Override
    public List<PolicyViolation> validatePolicy(Policy policy) {
        List<PolicyViolation> violations = new ArrayList<>();
        if (policy.name() == null || policy.name().isBlank()) {
            violations.add(new PolicyViolation(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(),
                "missing_name", "Name is required", PolicySeverity.ERROR, Map.of(), false, OffsetDateTime.now()));
        }
        if (policy.type() == null) {
            violations.add(new PolicyViolation(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(),
                "missing_type", "Type is required", PolicySeverity.ERROR, Map.of(), false, OffsetDateTime.now()));
        }
        return violations;
    }

    @Override
    public List<PolicyViolation> validateRule(PolicyRule rule) {
        List<PolicyViolation> violations = new ArrayList<>();
        if (rule.name() == null || rule.name().isBlank()) {
            violations.add(new PolicyViolation(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(),
                "missing_rule_name", "Rule name is required", PolicySeverity.ERROR, Map.of(), false, OffsetDateTime.now()));
        }
        return violations;
    }

    @Override
    public List<PolicyViolation> validateRequest(EvaluationRequest request) {
        List<PolicyViolation> violations = new ArrayList<>();
        if (request.module() == null || request.module().isBlank()) {
            violations.add(new PolicyViolation(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(),
                "missing_module", "Module is required", PolicySeverity.ERROR, Map.of(), false, OffsetDateTime.now()));
        }
        if (request.action() == null || request.action().isBlank()) {
            violations.add(new PolicyViolation(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(),
                "missing_action", "Action is required", PolicySeverity.ERROR, Map.of(), false, OffsetDateTime.now()));
        }
        return violations;
    }

    @Override
    public boolean isValidPolicy(Policy policy) {
        return validatePolicy(policy).isEmpty();
    }

    @Override
    public boolean isValidRequest(EvaluationRequest request) {
        return validateRequest(request).isEmpty();
    }
}
