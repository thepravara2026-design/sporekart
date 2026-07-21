package com.sporekart.ai.gateway.infrastructure;

import com.sporekart.ai.core.api.AIHealthService;
import com.sporekart.ai.core.domain.AiModuleStatus;
import org.springframework.stereotype.Component;

@Component
public class GatewayHealthService implements AIHealthService {
    private boolean degraded = false;
    private String details = "AI Gateway is operational";

    @Override
    public AiModuleStatus health() {
        return degraded ? AiModuleStatus.DEGRADED : AiModuleStatus.ACTIVE;
    }

    @Override
    public boolean isReady() {
        return !degraded;
    }

    @Override
    public boolean isDegraded() {
        return degraded;
    }

    @Override
    public String getHealthDetails() {
        return details;
    }

    public void setDegraded(boolean degraded, String details) {
        this.degraded = degraded;
        this.details = details;
    }
}
