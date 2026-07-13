package com.sporekart.ai.approval.api;

import com.sporekart.ai.approval.domain.*;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public interface ApprovalHistoryService {
    ApprovalHistory recordHistory(ApprovalHistory history);
    List<ApprovalHistory> getHistory(UUID requestId);
    List<ApprovalHistory> getHistoryByReviewer(UUID reviewerId);
    List<ApprovalHistory> getHistoryByDateRange(OffsetDateTime start, OffsetDateTime end);
    ApprovalComment addComment(ApprovalComment comment);
    List<ApprovalComment> getComments(UUID requestId);
}
