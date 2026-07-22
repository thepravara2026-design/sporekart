package com.sporekart.gateway.validation;

import com.sporekart.gateway.config.GatewayConfig;
import com.sporekart.gateway.registry.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashSet;

@Component
public class ConfigValidator {

    private static final Logger log = LoggerFactory.getLogger(ConfigValidator.class);

    private final GatewayConfig gatewayConfig;
    private final ServiceRegistry serviceRegistry;

    public ConfigValidator(GatewayConfig gatewayConfig, ServiceRegistry serviceRegistry) {
        this.gatewayConfig = gatewayConfig;
        this.serviceRegistry = serviceRegistry;
    }

    public ValidationResult validate() {
        var builder = ValidationResult.builder();

        validateGeneral(builder);
        validateServices(builder);
        validateRoutes(builder);
        validateMiddleware(builder);
        validateSecurity(builder);

        var result = builder.build();
        if (result.valid()) {
            log.info("Configuration validation passed");
        } else {
            log.error("Configuration validation failed with {} errors", result.errors().size());
            result.errors().forEach(e -> log.error("  Config error: {}", e));
        }
        return result;
    }

    private void validateGeneral(ValidationResult.Builder builder) {
        if (!gatewayConfig.isEnabled()) {
            builder.warn("Gateway is disabled in configuration");
        }
        var routing = gatewayConfig.getRouting();
        if (routing.getMaxHeaderSize() <= 0) {
            builder.error("routing.max-header-size must be positive");
        }
    }

    private void validateServices(ValidationResult.Builder builder) {
        var services = gatewayConfig.getServices();
        if (services == null || services.isEmpty()) {
            builder.warn("No downstream services configured");
            return;
        }
        var seenUrls = new HashSet<String>();
        services.forEach((name, cfg) -> {
            if (name == null || name.isBlank()) {
                builder.error("Service entry has empty name");
            }
            if (cfg.getUrl() == null || cfg.getUrl().isBlank()) {
                builder.error("Service '" + name + "' has no URL");
            } else if (!seenUrls.add(cfg.getUrl())) {
                builder.error("Duplicate URL for service '" + name + "': " + cfg.getUrl());
            }
            if (cfg.getTimeout() == null || cfg.getTimeout().isBlank()) {
                builder.warn("Service '" + name + "' has no timeout, using default");
            }
        });
    }

    private void validateRoutes(ValidationResult.Builder builder) {
        var seenPaths = new HashSet<String>();
    }

    private void validateMiddleware(ValidationResult.Builder builder) {
    }

    private void validateSecurity(ValidationResult.Builder builder) {
        var security = gatewayConfig.getSecurity();
        if (security.getPublicPaths() == null) {
            builder.warn("No public paths configured, all endpoints will require authentication");
        }
    }
}
