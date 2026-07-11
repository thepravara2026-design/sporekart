package com.sporekart.ai.core.api;

import com.sporekart.ai.core.domain.AiModuleStatus;

public interface HealthCheckProvider {
    AiModuleStatus health();
}
