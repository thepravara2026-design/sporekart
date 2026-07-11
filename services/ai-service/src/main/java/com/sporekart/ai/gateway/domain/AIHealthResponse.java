package com.sporekart.ai.gateway.domain;

import com.sporekart.ai.core.domain.AiModuleStatus;
import java.util.Map;

public record AIHealthResponse(
        String status,
        boolean ready,
        boolean degraded,
        Map<String, AiModuleStatus> modules,
        String details) {
}
