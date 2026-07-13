package com.sporekart.ai.policy.api;

import com.sporekart.ai.policy.domain.*;
import java.util.List;
import java.util.Map;

public interface PolicyCompiler {
    PolicyExpression compile(String expression, Map<String, Object> bindings);
    boolean validate(String expression);
    PolicyExpression parse(String expression);
    List<PolicyViolation> validatePolicy(Policy policy);
    boolean isCompiled(PolicyExpression expression);
}
