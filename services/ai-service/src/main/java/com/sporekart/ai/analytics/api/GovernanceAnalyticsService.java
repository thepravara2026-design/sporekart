package com.sporekart.ai.analytics.api;

import com.sporekart.ai.analytics.domain.GovernanceSnapshot;
import com.sporekart.ai.analytics.domain.GovernanceSummary;
import java.time.Instant;

public interface GovernanceAnalyticsService {
    GovernanceSummary getSummary();
    GovernanceSummary getSummaryByModule(String module);
    GovernanceSummary getSummaryByDateRange(Instant from, Instant to);
    GovernanceSnapshot captureSnapshot(String name);
}
