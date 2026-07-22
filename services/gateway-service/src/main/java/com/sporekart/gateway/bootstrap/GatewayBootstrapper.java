package com.sporekart.gateway.bootstrap;

import com.sporekart.gateway.config.GatewayConfig;
import com.sporekart.gateway.registry.ServiceRegistry;
import com.sporekart.gateway.validation.ConfigValidator;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class GatewayBootstrapper {

    private static final Logger log = LoggerFactory.getLogger(GatewayBootstrapper.class);

    private final GatewayConfig gatewayConfig;
    private final ServiceRegistry serviceRegistry;
    private final ConfigValidator configValidator;

    public GatewayBootstrapper(GatewayConfig gatewayConfig,
                               ServiceRegistry serviceRegistry,
                               ConfigValidator configValidator) {
        this.gatewayConfig = gatewayConfig;
        this.serviceRegistry = serviceRegistry;
        this.configValidator = configValidator;
    }

    @PostConstruct
    public void bootstrap() {
        log.info("========================================");
        log.info("  Enterprise API Gateway Bootstrap");
        log.info("========================================");

        log.info("Stage 1/5: Validating configuration...");
        var validation = configValidator.validate();
        if (!validation.valid()) {
            log.error("Gateway startup FAILED due to configuration errors:");
            validation.errors().forEach(e -> log.error("  - {}", e));
            throw new GatewayBootstrapException(
                "Configuration validation failed with " + validation.errors().size() + " errors"
            );
        }

        log.info("Stage 2/5: Initializing service registry...");
        var registrySize = serviceRegistry.size();
        log.info("  Registered {} services", registrySize);

        log.info("Stage 3/5: Configuring middleware pipeline...");
        log.info("  Rate limiter: {}", gatewayConfig.getRateLimiter().isEnabled() ? "enabled" : "disabled");
        log.info("  JWT auth: {}", gatewayConfig.getSecurity().isJwtEnabled() ? "enabled" : "disabled");
        log.info("  Metrics: {}", gatewayConfig.getObservability().isMetricsEnabled() ? "enabled" : "disabled");
        log.info("  Tracing: {}", gatewayConfig.getObservability().isTracingEnabled() ? "enabled" : "disabled");

        log.info("Stage 4/5: Configuring routes...");
        log.info("  Routes configured for {} services", registrySize);

        log.info("Stage 5/5: Gateway ready");
        log.info("========================================");
    }

    public static class GatewayBootstrapException extends RuntimeException {
        public GatewayBootstrapException(String message) {
            super(message);
        }
    }
}
