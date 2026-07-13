package com.sporekart.ai.workflow.domain;

public enum WorkflowTriggerType {
    REST_API,
    KAFKA_EVENT,
    SCHEDULED,
    CRON,
    BUSINESS_EVENT,
    AI_EVENT,
    MANUAL,
    WEBHOOK,
    MOBILE
}
