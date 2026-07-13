package com.sporekart.ai.approval.api;

import com.sporekart.ai.approval.domain.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ApprovalDelegationService {
    ApprovalDelegation delegate(UUID requestId, UUID fromReviewerId, UUID toReviewerId, String reason);
    ApprovalDelegation revokeDelegation(UUID delegationId);
    List<ApprovalDelegation> getDelegations(UUID requestId);
    List<ApprovalDelegation> getActiveDelegations(String userId);
    Optional<ApprovalDelegation> getActiveDelegation(UUID requestId);
}
