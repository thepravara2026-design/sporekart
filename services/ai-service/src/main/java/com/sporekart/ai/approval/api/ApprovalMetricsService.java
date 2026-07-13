package com.sporekart.ai.approval.api;

import com.sporekart.ai.approval.domain.*;
import java.util.Map;

public interface ApprovalMetricsService {
    void recordSubmission();
    void recordApproval(long timeMs);
    void recordRejection(long timeMs);
    void recordEscalation();
    void recordDelegation();
    void recordExpiration();
    long getTotalRequests();
    long getPendingCount();
    long getApprovedCount();
    long getRejectedCount();
    double getAverageReviewTimeMs();
    Map<String, Object> getStatistics();
}
