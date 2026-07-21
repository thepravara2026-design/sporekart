package com.sporekart.ai.governance.api;

import com.sporekart.ai.governance.domain.*;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface GovernanceContextResolver {
    GovernanceContext resolveContext(GovernanceRequest request);
    Map<String, Object> resolveResource(String module, String action, Map<String, Object> payload);
    Map<String, Object> resolveSubject(String userId, List<String> roles);
    Map<String, Object> resolveEnvironment();
}
