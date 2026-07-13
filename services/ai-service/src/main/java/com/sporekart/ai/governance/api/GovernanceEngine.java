package com.sporekart.ai.governance.api;

import com.sporekart.ai.governance.domain.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GovernanceEngine {
    GovernanceResponse validate(GovernanceRequest request);
    GovernanceResponse validateWithContext(GovernanceRequest request, GovernanceContext context);
    List<GovernanceViolation> checkPolicy(GovernancePolicy policy, GovernanceRequest request);
    GovernanceDecision decide(GovernanceRequest request, List<GovernanceViolation> violations);
    boolean isAllowed(GovernanceRequest request);
}
