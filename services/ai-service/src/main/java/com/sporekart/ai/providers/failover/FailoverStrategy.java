package com.sporekart.ai.providers.failover;

public enum FailoverStrategy {
    PRIORITY,
    REGION,
    CAPABILITY,
    COST,
    LATENCY,
    EMERGENCY
}
