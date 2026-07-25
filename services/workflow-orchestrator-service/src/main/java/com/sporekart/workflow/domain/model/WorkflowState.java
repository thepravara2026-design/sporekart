package com.sporekart.workflow.domain.model;

public enum WorkflowState {
    CREATED,
    QUEUED,
    PENDING,
    RUNNING,
    PAUSED,
    WAITING_APPROVAL,
    COMPLETED,
    FAILED,
    CANCELLED,
    RETRYING,
    ARCHIVED
}
