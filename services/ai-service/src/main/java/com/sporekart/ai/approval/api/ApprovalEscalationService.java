package com.sporekart.ai.approval.api;

import com.sporekart.ai.approval.domain.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ApprovalEscalationService {
    ApprovalEscalation escalate(UUID requestId, UUID fromReviewerId, EscalationReason reason, String details);
    ApprovalEscalation resolveEscalation(UUID escalationId, UUID newReviewerId);
    List<ApprovalEscalation> getEscalations(UUID requestId);
    List<ApprovalEscalation> getActiveEscalations();
    Optional<ApprovalEscalation> getCurrentEscalation(UUID requestId);
}
