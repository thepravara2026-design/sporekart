package com.sporekart.ai.automation.api;

import com.sporekart.ai.automation.domain.*;
import java.util.List;
import java.util.UUID;

public interface LifecycleManager {
    LifecycleDefinition registerLifecycle(LifecycleDefinition definition);
    LifecycleState getCurrentState(UUID entityId, String entityType);
    LifecycleState transition(UUID entityId, String entityType, LifecycleStateType targetState, String triggeredBy, String reason);
    List<LifecycleState> getHistory(UUID entityId, String entityType);
    List<LifecycleDefinition> getAllLifecycles();
}
