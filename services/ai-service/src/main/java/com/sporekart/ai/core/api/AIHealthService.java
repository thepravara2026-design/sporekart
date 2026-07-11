package com.sporekart.ai.core.api;

import com.sporekart.ai.core.domain.AiModuleStatus;

public interface AIHealthService {
    AiModuleStatus health();
    boolean isReady();
    boolean isDegraded();
    String getHealthDetails();
}
