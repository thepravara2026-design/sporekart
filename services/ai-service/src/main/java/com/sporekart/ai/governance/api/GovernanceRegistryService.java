package com.sporekart.ai.governance.api;

import com.sporekart.ai.governance.domain.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GovernanceRegistryService {
    GovernanceRegistry register(GovernanceRegistry registry);
    void unregister(UUID id);
    Optional<GovernanceRegistry> findById(UUID id);
    List<GovernanceRegistry> findByModule(String module);
    List<GovernanceRegistry> findAll();
    boolean isRegistered(String module, String endpoint);
}
