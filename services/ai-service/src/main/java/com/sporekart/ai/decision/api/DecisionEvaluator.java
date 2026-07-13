package com.sporekart.ai.decision.api;

import com.sporekart.ai.decision.domain.*;
import java.util.List;

public interface DecisionEvaluator {
    DecisionResult evaluate(DecisionRequest request, DecisionContext context);
    DecisionAction evaluateAction(DecisionRequest request, List<DecisionRule> rules);
    DecisionConfidence evaluateConfidence(DecisionRequest request, List<DecisionRule> rules, DecisionAction proposedAction);
    List<DecisionReason> generateReasons(DecisionRequest request, DecisionAction action, DecisionConfidence confidence);
}
