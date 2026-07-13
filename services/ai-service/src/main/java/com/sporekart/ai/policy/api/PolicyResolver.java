package com.sporekart.ai.policy.api;

import com.sporekart.ai.policy.domain.*;
import java.util.List;
import java.util.UUID;

public interface PolicyResolver {
    List<Policy> resolvePolicies(EvaluationRequest request);
    List<Policy> resolvePoliciesByModule(String module);
    List<Policy> resolvePoliciesByScope(PolicyScope scope);
    List<Policy> resolveActivePolicies();
    Policy resolvePolicy(UUID id);
}
