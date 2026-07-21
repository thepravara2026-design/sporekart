package com.sporekart.ai.providers.lifecycle;

import java.util.List;

public interface LifecycleAuditor {
    void record(LifecycleEvent event);
    List<LifecycleEvent> getHistory(String providerId);
    List<LifecycleEvent> getRecentEvents(int limit);
    List<LifecycleEvent> getFailedEvents(String providerId);
    boolean hasFailedRecently(String providerId);
}
