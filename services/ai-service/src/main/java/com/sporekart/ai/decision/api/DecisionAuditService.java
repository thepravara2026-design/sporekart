package com.sporekart.ai.decision.api;

import com.sporekart.ai.decision.domain.*;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public interface DecisionAuditService {
    DecisionAudit recordAudit(DecisionAudit audit);
    List<DecisionAudit> findByRequestId(UUID requestId);
    List<DecisionAudit> findByUserId(String userId);
    List<DecisionAudit> findByAction(DecisionAction action);
    List<DecisionAudit> findByStatus(DecisionStatus status);
    List<DecisionAudit> findByDateRange(OffsetDateTime start, OffsetDateTime end);
}
