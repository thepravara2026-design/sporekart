package com.sporekart.ai.providers.health.liveness;

public enum LivenessState {
    ALIVE,
    DEAD,
    UNKNOWN,
    HUNG,
    RESTART_REQUIRED,
    HEARTBEAT_LOST
}
