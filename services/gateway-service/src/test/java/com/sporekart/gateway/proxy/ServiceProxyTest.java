package com.sporekart.gateway.proxy;

import com.sporekart.gateway.config.GatewayConfig;
import com.sporekart.gateway.registry.ServiceRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class ServiceProxyTest {

    private ServiceProxy proxy;

    @BeforeEach
    void setUp() {
        var config = new GatewayConfig();
        config.setServices(Map.of(
            "identity", createConfig("http://localhost:8081", "5s"),
            "catalog", createConfig("http://localhost:8082", "10s"),
            "order", createConfig("http://localhost:8083", "10s")
        ));
        var registry = new ServiceRegistry(config);
        registry.init();
        proxy = new ServiceProxy(registry);
    }

    @Test
    void shouldResolveIdentityService() {
        var uri = proxy.resolveTarget("identity", "/api/auth/login");
        assertThat(uri.toString()).isEqualTo("http://localhost:8081/api/auth/login");
    }

    @Test
    void shouldResolveCatalogService() {
        var uri = proxy.resolveTarget("catalog", "/api/catalog/products");
        assertThat(uri.toString()).isEqualTo("http://localhost:8082/api/catalog/products");
    }

    @Test
    void shouldExtractServiceNameFromApiPath() {
        assertThat(proxy.extractServiceName("/api/auth/login")).isEqualTo("identity");
        assertThat(proxy.extractServiceName("/api/catalog/products")).isEqualTo("catalog");
        assertThat(proxy.extractServiceName("/api/orders/123")).isEqualTo("order");
    }

    @Test
    void shouldExtractServiceNameFromInternalPath() {
        assertThat(proxy.extractServiceName("/internal/identity/sync")).isEqualTo("identity");
    }

    @Test
    void shouldReturnNullForPublicPath() {
        assertThat(proxy.extractServiceName("/api/public/health")).isNull();
    }

    @Test
    void shouldReturnNullForEmptyPath() {
        assertThat(proxy.extractServiceName("")).isNull();
        assertThat(proxy.extractServiceName(null)).isNull();
    }

    private GatewayConfig.ServiceConfig createConfig(String url, String timeout) {
        var cfg = new GatewayConfig.ServiceConfig();
        cfg.setUrl(url);
        cfg.setTimeout(timeout);
        return cfg;
    }
}
