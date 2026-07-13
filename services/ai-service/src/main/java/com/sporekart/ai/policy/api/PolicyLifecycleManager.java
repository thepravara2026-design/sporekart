package com.sporekart.ai.policy.api;

import com.sporekart.ai.policy.domain.*;
import java.util.List;
import java.util.UUID;

public interface PolicyLifecycleManager {
    Policy activatePolicy(UUID policyId, String triggeredBy);
    Policy deactivatePolicy(UUID policyId, String triggeredBy);
    Policy archivePolicy(UUID policyId, String triggeredBy);
    Policy draftPolicy(Policy policy, String triggeredBy);
    PolicyVersion createVersion(UUID policyId, String changeNotes, String triggeredBy);
    List<PolicyVersion> getVersions(UUID policyId);
    boolean canTransition(PolicyStatus from, PolicyStatus to);
}
