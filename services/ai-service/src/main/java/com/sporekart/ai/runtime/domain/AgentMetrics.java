package com.sporekart.ai.runtime.domain;

public record AgentMetrics(
    long totalExecutions,
    long successfulExecutions,
    long failedExecutions,
    double averageExecutionTimeMs,
    double averageTokensPerExecution,
    long totalTokensUsed,
    int activeAgentCount
) {}
