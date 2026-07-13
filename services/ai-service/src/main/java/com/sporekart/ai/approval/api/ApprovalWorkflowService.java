package com.sporekart.ai.approval.api;

import com.sporekart.ai.approval.domain.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ApprovalWorkflowService {
    ApprovalWorkflow createWorkflow(ApprovalWorkflow workflow);
    ApprovalWorkflow updateWorkflow(ApprovalWorkflow workflow);
    void deleteWorkflow(UUID id);
    Optional<ApprovalWorkflow> getWorkflow(UUID id);
    List<ApprovalWorkflow> listWorkflows();
    List<ApprovalWorkflow> findWorkflowsByModule(String module);
    boolean canTransition(ApprovalStatus from, ApprovalStatus to, UUID workflowId);
}
