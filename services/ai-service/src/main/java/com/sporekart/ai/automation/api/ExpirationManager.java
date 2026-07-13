package com.sporekart.ai.automation.api;

import com.sporekart.ai.automation.domain.*;
import java.util.List;
import java.util.UUID;

public interface ExpirationManager {
    List<ExpirationPolicy> findExpiredEntities();
    void applyExpiration(UUID entityId, String entityType);
    List<ExpirationPolicy> getExpirationPolicies();
}
