package com.sporekart.ai.workflow.domain;

public enum WorkflowStepType {
    AI_GATEWAY,
    PROMPT,
    KNOWLEDGE_RETRIEVAL,
    SEMANTIC_SEARCH,
    BUSINESS_SERVICE,
    REST_API_CALL,
    NOTIFICATION,
    DELAY,
    DECISION,
    CONDITIONAL_BRANCH,
    LOOP,
    RETRY,
    AUDIT,
    LOGGING,
    CUSTOM
}
