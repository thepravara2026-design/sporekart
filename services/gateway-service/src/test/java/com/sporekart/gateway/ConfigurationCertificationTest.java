package com.sporekart.gateway;

import com.sporekart.gateway.config.GatewayConfig;
import com.sporekart.gateway.validation.ConfigValidator;
import com.sporekart.gateway.validation.ValidationResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class ConfigurationCertificationTest {

    @Autowired
    private GatewayConfig gatewayConfig;

    @Autowired
    private ConfigValidator configValidator;

    @Test
    void gatewayShouldBeEnabled() {
        assertThat(gatewayConfig.isEnabled()).isTrue();
    }

    @Test
    void configurationShouldPassValidation() {
        var result = configValidator.validate();
        assertThat(result.valid()).isTrue();
    }

    @Test
    void jwtShouldBeEnabled() {
        assertThat(gatewayConfig.getSecurity().isJwtEnabled()).isTrue();
    }

    @Test
    void corsShouldAllowAllOrigins() {
        assertThat(gatewayConfig.getCors().getAllowedOrigins()).isEqualTo("*");
    }

    @Test
    void publicPathsShouldIncludeActuatorHealth() {
        assertThat(gatewayConfig.getSecurity().getPublicPaths())
            .anyMatch(p -> p.contains("actuator/health"));
    }
}
