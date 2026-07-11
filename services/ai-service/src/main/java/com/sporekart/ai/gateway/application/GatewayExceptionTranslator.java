package com.sporekart.ai.gateway.application;

import com.sporekart.ai.core.application.exception.*;
import com.sporekart.ai.core.domain.AiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class GatewayExceptionTranslator {
    private static final Logger log = LoggerFactory.getLogger(GatewayExceptionTranslator.class);

    public AiResponse translate(Exception e) {
        if (e instanceof AIGatewayException gatewayEx) {
            log.warn("Gateway exception: {} - {}", gatewayEx.getErrorCode(), gatewayEx.getMessage());
            return AiResponse.failure(gatewayEx.getErrorCode() + ": " + gatewayEx.getMessage());
        }
        if (e instanceof AiCoreException coreEx) {
            log.warn("Core exception: {}", coreEx.getMessage());
            return AiResponse.failure(coreEx.getMessage());
        }
        log.error("Unexpected exception during AI execution", e);
        return AiResponse.failure("AI-999: Internal AI Gateway error");
    }
}
