package com.sporekart.ai.governance.api;

import com.sporekart.ai.governance.domain.*;
import java.util.List;
import java.util.UUID;

public interface GovernanceLifecycleManager {
    GovernanceLifecycle transition(GovernancePolicy policy, String newStatus, String triggeredBy);
    List<GovernanceLifecycle> getHistory(UUID policyId);
    boolean canTransition(String fromStatus, String toStatus);
}
