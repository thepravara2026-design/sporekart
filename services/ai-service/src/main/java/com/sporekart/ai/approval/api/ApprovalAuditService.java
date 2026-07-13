package com.sporekart.ai.approval.api;

import com.sporekart.ai.approval.domain.*;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public interface ApprovalAuditService {
    ApprovalAudit recordAudit(ApprovalAudit audit);
    List<ApprovalAudit> findByRequestId(UUID requestId);
    List<ApprovalAudit> findByReviewerId(UUID reviewerId);
    List<ApprovalAudit> findByDateRange(OffsetDateTime start, OffsetDateTime end);
    List<ApprovalAudit> findByDecision(ApprovalDecision decision);
}
