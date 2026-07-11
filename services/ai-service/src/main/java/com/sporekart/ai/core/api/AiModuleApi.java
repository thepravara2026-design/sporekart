package com.sporekart.ai.core.api;

import com.sporekart.ai.core.domain.AiModule;
import com.sporekart.ai.core.domain.AiModuleStatus;

public interface AiModuleApi {
    AiModule getModule();
    AiModuleStatus getStatus();
    boolean isEnabled();
}
