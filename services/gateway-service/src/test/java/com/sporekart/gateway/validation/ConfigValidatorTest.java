package com.sporekart.gateway.validation;

import com.sporekart.gateway.config.GatewayConfig;
import com.sporekart.gateway.registry.ServiceRegistry;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class ConfigValidatorTest {

    @Test
    void shouldPassWithValidConfig() {
        var config = new GatewayConfig();
        config.setServices(Map.of("identity", createServiceConfig("http://localhost:8081")));
        var registry = new ServiceRegistry(config);
        registry.init();
        var validator = new ConfigValidator(config, registry);

        var result = validator.validate();
        assertThat(result.valid()).isTrue();
    }

    @Test
    void shouldWarnOnNoServices() {
        var config = new GatewayConfig();
        var registry = new ServiceRegistry(config);
        registry.init();
        var validator = new ConfigValidator(config, registry);

        var result = validator.validate();
        assertThat(result.valid()).isTrue();
        assertThat(result.warnings()).isNotEmpty();
    }

    @Test
    void shouldDetectMissingUrl() {
        var config = new GatewayConfig();
        var cfg = new GatewayConfig.ServiceConfig();
        cfg.setUrl("");
        config.setServices(Map.of("bad", cfg));
        var registry = new ServiceRegistry(config);
        registry.init();
        var validator = new ConfigValidator(config, registry);

        var result = validator.validate();
        assertThat(result.valid()).isFalse();
    }

    private GatewayConfig.ServiceConfig createServiceConfig(String url) {
        var cfg = new GatewayConfig.ServiceConfig();
        cfg.setUrl(url);
        cfg.setTimeout("5s");
        return cfg;
    }
}
