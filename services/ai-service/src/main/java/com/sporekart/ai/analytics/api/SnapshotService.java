package com.sporekart.ai.analytics.api;

import com.sporekart.ai.analytics.domain.GovernanceSnapshot;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface SnapshotService {
    GovernanceSnapshot createSnapshot(String name, Map<String, Object> data);
    GovernanceSnapshot getSnapshot(UUID id);
    List<GovernanceSnapshot> getSnapshotsByName(String name);
    List<GovernanceSnapshot> getSnapshotsByDateRange(Instant from, Instant to);
}
