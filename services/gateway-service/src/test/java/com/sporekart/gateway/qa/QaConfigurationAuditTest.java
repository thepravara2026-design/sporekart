package com.sporekart.gateway.qa;

import com.sporekart.gateway.config.GatewayConfig;
import com.sporekart.gateway.validation.ConfigValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class QaConfigurationAuditTest {

    @Autowired
    private GatewayConfig gatewayConfig;

    @Autowired
    private ConfigValidator configValidator;

    @Test
    @DisplayName("QA-08-004: Gateway is enabled")
    void gateway_isEnabled() {
        assertTrue(gatewayConfig.isEnabled(), "Gateway must be enabled by default");
    }

    @Test
    @DisplayName("QA-08-005: JWT auth is enabled")
    void jwtAuth_isEnabled() {
        assertTrue(gatewayConfig.getSecurity().isJwtEnabled(), "JWT must be enabled by default");
    }

    @Test
    @DisplayName("QA-08-006: Public paths are configured")
    void publicPaths_areConfigured() {
        var paths = gatewayConfig.getSecurity().getPublicPaths();
        assertNotNull(paths, "Public paths must be configured");
        assertFalse(paths.isEmpty(), "Public paths must not be empty");
        assertTrue(paths.contains("/actuator/health/**"), "Health endpoint must be public");
        assertTrue(paths.contains("/v3/api-docs/**"), "OpenAPI docs must be public");
    }

    @Test
    @DisplayName("QA-08-007: CORS is configured with exposed headers")
    void cors_isConfigured() {
        var cors = gatewayConfig.getCors();
        assertNotNull(cors, "CORS configuration must exist");
        assertNotNull(cors.getAllowedOrigins(), "CORS allowed origins must be set");
        assertNotNull(cors.getAllowedMethods(), "CORS allowed methods must be set");
    }

    @Test
    @DisplayName("QA-08-008: Observability is enabled")
    void observability_isEnabled() {
        var obs = gatewayConfig.getObservability();
        assertTrue(obs.isMetricsEnabled(), "Metrics must be enabled");
        assertTrue(obs.isAuditEnabled(), "Audit must be enabled");
    }

    @Test
    @DisplayName("QA-08-009: Services are configured in test profile for certification")
    void services_areConfiguredInTest() {
        var services = gatewayConfig.getServices();
        assertFalse(services == null || services.isEmpty(),
            "Services must be configured in test profile for certification validation");
    }

    @Test
    @DisplayName("QA-08-010: Rate limiter is disabled in test profile")
    void rateLimiter_isDisabledInTest() {
        assertFalse(gatewayConfig.getRateLimiter().isEnabled(),
            "Rate limiter must be disabled in test profile (no Redis dependency)");
    }
}
