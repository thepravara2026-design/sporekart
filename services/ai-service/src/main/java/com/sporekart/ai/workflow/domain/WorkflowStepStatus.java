package com.sporekart.ai.workflow.domain;

public enum WorkflowStepStatus {
    PENDING,
    RUNNING,
    COMPLETED,
    FAILED,
    SKIPPED,
    RETRYING
}
