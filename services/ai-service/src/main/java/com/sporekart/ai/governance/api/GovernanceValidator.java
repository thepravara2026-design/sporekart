package com.sporekart.ai.governance.api;

import com.sporekart.ai.governance.domain.*;
import java.util.List;

public interface GovernanceValidator {
    List<GovernanceViolation> validateRequest(GovernanceRequest request);
    List<GovernanceViolation> validatePolicy(GovernancePolicy policy);
    List<GovernanceViolation> validateConfiguration(GovernanceConfiguration config);
    boolean isValid(GovernanceRequest request);
}
