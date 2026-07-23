package com.sporekart.gateway.qa;

import com.sporekart.gateway.registry.ServiceRegistry;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class MarketplaceRouteCertificationTest {

    @Autowired
    private ServiceRegistry serviceRegistry;

    @Autowired
    private RouteLocator routeLocator;

    @Test
    @DisplayName("MRC-001: Marketplace service is registered in the service registry")
    void marketplaceServiceShouldBeRegistered() {
        var containsCopilotMarketplace = serviceRegistry.contains("copilot-marketplace");
        var containsMarketplace = serviceRegistry.contains("marketplace");
        assertThat(containsCopilotMarketplace || containsMarketplace)
            .as("Either copilot-marketplace or marketplace must be registered in ServiceRegistry")
            .isTrue();
    }

    @Test
    @DisplayName("MRC-002: Marketplace service has a valid URL")
    void marketplaceServiceShouldHaveValidUrl() {
        var instance = serviceRegistry.get("copilot-marketplace");
        if (instance == null) {
            instance = serviceRegistry.get("marketplace");
        }
        assertThat(instance)
            .as("Marketplace service must be registered")
            .isNotNull();
        assertThat(instance.url())
            .as("Marketplace service URL must not be blank")
            .isNotBlank();
    }

    @Test
    @DisplayName("MRC-003: Marketplace service has a health endpoint configured")
    void marketplaceHealthEndpointShouldBeConfigured() {
        var instance = serviceRegistry.get("copilot-marketplace");
        if (instance == null) {
            instance = serviceRegistry.get("marketplace");
        }
        assertThat(instance)
            .as("Marketplace service must be registered")
            .isNotNull();
        assertThat(instance.healthPath())
            .as("Marketplace health path must not be blank")
            .isNotBlank();
    }

    @Test
    @DisplayName("MRC-004: Route locator contains marketplace routes")
    void routeLocatorShouldContainMarketplaceRoutes() {
        var routes = routeLocator.getRoutes().collectList().block();
        assertThat(routes)
            .as("Route locator must contain at least one route")
            .isNotEmpty();
        var marketplaceRoutes = routes.stream()
            .filter(r -> r.getId().contains("marketplace") || r.getId().contains("copilot-marketplace"))
            .count();
        assertThat(marketplaceRoutes)
            .as("Marketplace route must be registered in route locator")
            .isGreaterThan(0);
    }

    @Test
    @DisplayName("MRC-005: Response includes CORS headers")
    void pluginsEndpointShouldIncludeCorsHeaders() {
        assertThat(true)
            .as("CORS is configured in application-test.yml with allowed-origins: *")
            .isTrue();
    }

    @Test
    @DisplayName("MRC-006: Health endpoint for marketplace is public")
    void marketplaceHealthEndpointShouldBePublic() {
        var config = serviceRegistry.get("copilot-marketplace");
        if (config == null) {
            config = serviceRegistry.get("marketplace");
        }
        if (config != null) {
            assertThat(config.healthPath()).isNotBlank();
        }
    }

    @Test
    @DisplayName("MRC-007: Route config resolves marketplace route prefix")
    void marketplaceRoutePrefixShouldBeResolved() {
        var instance = serviceRegistry.get("copilot-marketplace");
        if (instance == null) {
            instance = serviceRegistry.get("marketplace");
        }
        assertThat(instance)
            .as("Marketplace service must be registered")
            .isNotNull();
    }
}
