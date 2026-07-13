package com.sporekart.ai.decision.api;

import com.sporekart.ai.decision.domain.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DecisionRegistryService {
    DecisionRegistry register(DecisionRegistry registry);
    void unregister(UUID id);
    Optional<DecisionRegistry> findById(UUID id);
    List<DecisionRegistry> findByModule(String module);
    List<DecisionRegistry> findAll();
    boolean isRegistered(String module, String endpoint);
}
