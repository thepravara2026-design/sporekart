package com.sporekart.ai.risk.domain;

public enum RecommendationType {
    PROCEED,
    REQUIRE_APPROVAL,
    REDUCE_CONTEXT,
    USE_ALTERNATE_PROVIDER,
    RETRY,
    REQUEST_HUMAN_REVIEW,
    BLOCK_EXECUTION
}
