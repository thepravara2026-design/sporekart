package com.sporekart.ai.workflow.domain;

public enum WorkflowExecutionStatus {
    PENDING,
    RUNNING,
    PAUSED,
    COMPLETED,
    FAILED,
    CANCELLED,
    TIMEOUT
}
