package com.sporekart.ai.policy.api;

import com.sporekart.ai.policy.domain.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PolicyRegistry {
    PolicyRegistry register(PolicyRegistry registry);
    void unregister(UUID id);
    Optional<PolicyRegistry> findById(UUID id);
    List<PolicyRegistry> findByModule(String module);
    List<PolicyRegistry> findByScope(PolicyScope scope);
    List<PolicyRegistry> findAll();
    boolean isRegistered(String module, PolicyType type);
}
