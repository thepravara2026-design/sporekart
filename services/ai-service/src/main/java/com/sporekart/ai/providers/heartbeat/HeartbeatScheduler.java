package com.sporekart.ai.providers.heartbeat;

import java.time.Duration;

public interface HeartbeatScheduler {
    void schedule(String providerId, Duration interval);
    void cancel(String providerId);
    boolean isScheduled(String providerId);
    void pause(String providerId);
    void resume(String providerId);
}
