package com.sporekart.ai.governance.api;

import com.sporekart.ai.governance.domain.*;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public interface GovernanceAuditService {
    GovernanceAudit recordAudit(GovernanceAudit audit);
    List<GovernanceAudit> findByRequestId(UUID requestId);
    List<GovernanceAudit> findByUserId(String userId);
    List<GovernanceAudit> findByDateRange(OffsetDateTime start, OffsetDateTime end);
    List<GovernanceAudit> findByDecision(GovernanceDecision decision);
}
