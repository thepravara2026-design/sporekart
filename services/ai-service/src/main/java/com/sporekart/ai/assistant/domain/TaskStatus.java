package com.sporekart.ai.assistant.domain;

public enum TaskStatus {
    PENDING,
    PLANNING,
    QUEUED,
    EXECUTING,
    COMPLETED,
    FAILED,
    CANCELLED,
    TIMEOUT
}
