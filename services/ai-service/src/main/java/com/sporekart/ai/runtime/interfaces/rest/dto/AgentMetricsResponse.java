package com.sporekart.ai.runtime.interfaces.rest.dto;

public record AgentMetricsResponse(
    long totalExecutions,
    long successfulExecutions,
    long failedExecutions,
    double averageExecutionTimeMs,
    double averageTokensPerExecution,
    long totalTokensUsed,
    int activeAgentCount
) {}
