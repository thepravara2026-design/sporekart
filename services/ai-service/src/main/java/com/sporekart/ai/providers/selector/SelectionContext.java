package com.sporekart.ai.providers.selector;

import com.sporekart.ai.providers.AIProvider;

public interface SelectionContext {
    String requestId();
    String tenantId();
    String model();
    String capability();
    double maxCost();
    long maxLatencyMs();
    String preferredRegion();
    boolean allowFallback();
    AIProvider preferredProvider();
}
