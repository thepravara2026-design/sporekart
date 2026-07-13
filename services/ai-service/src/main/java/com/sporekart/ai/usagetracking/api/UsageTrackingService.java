package com.sporekart.ai.usagetracking.api;

import com.sporekart.ai.usagetracking.domain.UsageRecord;

import java.util.List;
import java.util.Optional;

public interface UsageTrackingService {

    UsageRecord recordUsage(UsageRecord usageRecord);

    Optional<UsageRecord> getUsage(String usageId);

    List<UsageRecord> listUsage(int limit);
}
