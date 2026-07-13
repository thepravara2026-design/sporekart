package com.sporekart.ai.governance.application;

import com.sporekart.ai.governance.api.GovernanceValidator;
import com.sporekart.ai.governance.domain.*;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.*;

@Service
public class GovernanceValidatorImpl implements GovernanceValidator {

    @Override
    public List<GovernanceViolation> validateRequest(GovernanceRequest request) {
        List<GovernanceViolation> violations = new ArrayList<>();
        if (request.module() == null || request.module().isBlank()) {
            violations.add(new GovernanceViolation(
                UUID.randomUUID(), "missing_module", "Module is required",
                GovernanceSeverity.ERROR, Map.of("field", "module"), false, OffsetDateTime.now()
            ));
        }
        if (request.action() == null || request.action().isBlank()) {
            violations.add(new GovernanceViolation(
                UUID.randomUUID(), "missing_action", "Action is required",
                GovernanceSeverity.ERROR, Map.of("field", "action"), false, OffsetDateTime.now()
            ));
        }
        if (request.userId() == null || request.userId().isBlank()) {
            violations.add(new GovernanceViolation(
                UUID.randomUUID(), "missing_user", "User ID is required",
                GovernanceSeverity.WARNING, Map.of("field", "userId"), true, OffsetDateTime.now()
            ));
        }
        return violations;
    }

    @Override
    public List<GovernanceViolation> validatePolicy(GovernancePolicy policy) {
        List<GovernanceViolation> violations = new ArrayList<>();
        if (policy.name() == null || policy.name().isBlank()) {
            violations.add(new GovernanceViolation(
                UUID.randomUUID(), "missing_policy_name", "Policy name is required",
                GovernanceSeverity.ERROR, Map.of("field", "name"), false, OffsetDateTime.now()
            ));
        }
        return violations;
    }

    @Override
    public List<GovernanceViolation> validateConfiguration(GovernanceConfiguration config) {
        List<GovernanceViolation> violations = new ArrayList<>();
        if (config.key() == null || config.key().isBlank()) {
            violations.add(new GovernanceViolation(
                UUID.randomUUID(), "missing_config_key", "Configuration key is required",
                GovernanceSeverity.ERROR, Map.of("field", "key"), false, OffsetDateTime.now()
            ));
        }
        return violations;
    }

    @Override
    public boolean isValid(GovernanceRequest request) {
        return validateRequest(request).isEmpty();
    }
}
