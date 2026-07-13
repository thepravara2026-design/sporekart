package com.sporekart.ai.policy.application;

import com.sporekart.ai.policy.api.PolicyCompiler;
import com.sporekart.ai.policy.domain.*;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.*;

@Service
public class PolicyCompilerImpl implements PolicyCompiler {

    @Override
    public PolicyExpression compile(String expression, Map<String, Object> bindings) {
        return new PolicyExpression(expression, bindings, "simple", true, OffsetDateTime.now());
    }

    @Override
    public boolean validate(String expression) {
        return expression != null && !expression.isBlank();
    }

    @Override
    public PolicyExpression parse(String expression) {
        return new PolicyExpression(expression, new HashMap<>(), "simple", false, null);
    }

    @Override
    public List<PolicyViolation> validatePolicy(Policy policy) {
        List<PolicyViolation> violations = new ArrayList<>();
        if (policy.name() == null || policy.name().isBlank()) {
            violations.add(new PolicyViolation(
                UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(),
                "missing_name", "Policy name is required", PolicySeverity.ERROR,
                Map.of("field", "name"), false, OffsetDateTime.now()
            ));
        }
        return violations;
    }

    @Override
    public boolean isCompiled(PolicyExpression expression) {
        return expression != null && expression.compiled();
    }
}
