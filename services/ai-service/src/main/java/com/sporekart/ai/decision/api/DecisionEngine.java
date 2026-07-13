package com.sporekart.ai.decision.api;

import com.sporekart.ai.decision.domain.*;
import java.util.List;
import java.util.UUID;

public interface DecisionEngine {
    DecisionResult evaluate(DecisionRequest request);
    DecisionResult evaluateWithContext(DecisionRequest request, DecisionContext context);
    DecisionResult replay(UUID originalRequestId);
    DecisionAction resolveConflict(List<DecisionResult> conflictingResults, ConflictStrategy strategy);
    boolean isAllowed(DecisionRequest request);
}
