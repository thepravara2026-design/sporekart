package com.sporekart.gateway.registry;

import com.sporekart.gateway.config.GatewayConfig;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class ServiceRegistryTest {

    @Test
    void shouldRegisterAndRetrieveService() {
        var config = new GatewayConfig();
        config.setServices(Map.of("identity",
            createConfig("http://localhost:8081", "/actuator/health", "5s", List.of())));
        var registry = new ServiceRegistry(config);
        registry.init();

        var instance = registry.get("identity");
        assertThat(instance).isNotNull();
        assertThat(instance.url()).isEqualTo("http://localhost:8081");
    }

    @Test
    void shouldReturnNullForUnknownService() {
        var config = new GatewayConfig();
        var registry = new ServiceRegistry(config);
        registry.init();

        assertThat(registry.get("nonexistent")).isNull();
    }

    @Test
    void shouldRegisterAndDeregister() {
        var config = new GatewayConfig();
        var registry = new ServiceRegistry(config);
        registry.init();

        registry.register("test-service", new ServiceRegistry.ServiceInstance(
            "test-service", "http://localhost:9999", "/health", "5s", List.of()));
        assertThat(registry.contains("test-service")).isTrue();

        registry.deregister("test-service");
        assertThat(registry.contains("test-service")).isFalse();
    }

    @Test
    void shouldReturnSize() {
        var config = new GatewayConfig();
        var registry = new ServiceRegistry(config);
        registry.init();
        assertThat(registry.size()).isGreaterThanOrEqualTo(0);
    }

    private GatewayConfig.ServiceConfig createConfig(String url, String healthPath, String timeout, List<String> roles) {
        var cfg = new GatewayConfig.ServiceConfig();
        cfg.setUrl(url);
        cfg.setHealthPath(healthPath);
        cfg.setTimeout(timeout);
        cfg.setRoles(roles);
        return cfg;
    }
}
