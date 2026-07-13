package com.sporekart.ai.governance.api;

import com.sporekart.ai.governance.domain.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GovernanceManager {
    GovernancePolicy createPolicy(GovernancePolicy policy);
    GovernancePolicy updatePolicy(GovernancePolicy policy);
    void deletePolicy(UUID id);
    Optional<GovernancePolicy> getPolicy(UUID id);
    List<GovernancePolicy> listPolicies();
    List<GovernancePolicy> listPoliciesByScope(GovernanceScope scope);
    GovernanceConfiguration setConfiguration(GovernanceConfiguration config);
    GovernanceConfiguration getConfiguration(String key);
    List<GovernanceConfiguration> getAllConfigurations();
    void reloadConfiguration();
}
