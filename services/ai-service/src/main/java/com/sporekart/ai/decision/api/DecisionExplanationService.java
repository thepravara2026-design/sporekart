package com.sporekart.ai.decision.api;

import com.sporekart.ai.decision.domain.*;
import java.util.List;
import java.util.Map;

public interface DecisionExplanationService {
    DecisionExplanation generateExplanation(DecisionResult result, DecisionRequest request);
    String generateSummary(DecisionResult result);
    List<DecisionEvidence> gatherEvidence(DecisionResult result, DecisionRequest request);
    String generateExplanationText(DecisionResult result);
    Map<String, Object> buildAuditMetadata(DecisionResult result, DecisionRequest request);
}
