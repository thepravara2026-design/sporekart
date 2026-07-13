package com.sporekart.ai.decision.domain;

public enum ConflictStrategy {
    PRIORITY_BASED, WEIGHTED, DENY_OVERRIDES, ALLOW_OVERRIDES, MOST_RECENT, SAFE_DEFAULT, FAIL_CLOSED, CUSTOM
}
