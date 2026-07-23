package com.sporekart.grower.copilot.service;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class GrowerCopilotRegistrationService {

    private static final Logger log = LoggerFactory.getLogger(GrowerCopilotRegistrationService.class);

    @PostConstruct
    public void register() {
        log.info("============================================================");
        log.info("  Grower Copilot Service registered and ready");
        log.info("  Base path: /api/v1/copilot/grower");
        log.info("  Capabilities: chat, stream, recommend, disease, yield,");
        log.info("                weather, planning, history, health");
        log.info("============================================================");
    }
}
