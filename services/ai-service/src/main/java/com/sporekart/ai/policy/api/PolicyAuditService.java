package com.sporekart.ai.policy.api;

import com.sporekart.ai.policy.domain.*;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public interface PolicyAuditService {
    PolicyAudit recordAudit(PolicyAudit audit);
    List<PolicyAudit> findByPolicyId(UUID policyId);
    List<PolicyAudit> findByRequestId(UUID requestId);
    List<PolicyAudit> findByUserId(String userId);
    List<PolicyAudit> findByDateRange(OffsetDateTime start, OffsetDateTime end);
    List<PolicyAudit> findByDecision(PolicyDecision decision);
}
