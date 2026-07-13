package com.sporekart.ai.decision.api;

import com.sporekart.ai.decision.domain.*;
import java.util.List;

public interface DecisionReasoningService {
    DecisionAction resolveDecision(List<DecisionRule> rules, DecisionRequest request);
    DecisionAction resolveConflict(List<DecisionAction> conflictingActions, ConflictStrategy strategy);
    DecisionConfidence calculateConfidence(List<DecisionRule> matchedRules, DecisionAction action);
    List<DecisionReason> buildReasons(DecisionAction action, List<DecisionRule> matchedRules);
    boolean requiresOverride(DecisionAction action, DecisionRequest request);
}
