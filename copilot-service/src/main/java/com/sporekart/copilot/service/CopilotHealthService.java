package com.sporekart.copilot.service;

import com.sporekart.copilot.dto.HealthResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.management.ManagementFactory;

@Service
public class CopilotHealthService {

    private static final Logger log = LoggerFactory.getLogger(CopilotHealthService.class);

    private final CopilotRegistryService registryService;

    public CopilotHealthService(CopilotRegistryService registryService) {
        this.registryService = registryService;
    }

    public HealthResponse getHealth() {
        return buildHealth("UP");
    }

    public HealthResponse getReadiness() {
        return buildHealth("READY");
    }

    public HealthResponse getLiveness() {
        return buildHealth("ALIVE");
    }

    private HealthResponse buildHealth(String status) {
        var uptime = ManagementFactory.getRuntimeMXBean().getUptime();
        var copilotCount = registryService.listCopilots().size();
        return new HealthResponse(status, "0.1.0-SNAPSHOT", uptime, copilotCount);
    }
}
