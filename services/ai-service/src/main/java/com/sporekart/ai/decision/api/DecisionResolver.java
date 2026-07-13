package com.sporekart.ai.decision.api;

import com.sporekart.ai.decision.domain.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DecisionResolver {
    DecisionContext resolveContext(DecisionRequest request);
    List<DecisionRule> resolveRules(DecisionRequest request);
    List<DecisionRegistry> resolveRegistries(String module);
    Optional<DecisionResult> findById(UUID id);
    List<DecisionResult> findByRequestId(UUID requestId);
}
