package com.sporekart.gateway.config;

import com.sporekart.gateway.registry.ServiceRegistry;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class RouteConfig {

    private static final Logger log = LoggerFactory.getLogger(RouteConfig.class);

    private final ServiceRegistry serviceRegistry;

    public RouteConfig(ServiceRegistry serviceRegistry) {
        this.serviceRegistry = serviceRegistry;
    }

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        var routes = builder.routes();

        registerBuiltinRoutes(routes);

        serviceRegistry.getAll().forEach((name, instance) -> {
            var routePrefix = resolveRoutePrefix(name);
            if (routePrefix != null) {
                routes.route(name + "-route", r -> r
                    .path(routePrefix + "/**")
                    .filters(f -> {
                        f.stripPrefix(1);
                        f.circuitBreaker(cb -> cb
                            .setName(name + "-cb")
                            .setFallbackUri("forward:/fallback/" + name));
                        f.retry(retry -> retry
                            .setRetries(2)
                            .setStatuses(
                                org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE,
                                org.springframework.http.HttpStatus.GATEWAY_TIMEOUT));
                        return f;
                    })
                    .uri(instance.url())
                );
                log.info("Route registered: {} -> {} {}", routePrefix, name, instance.url());
            }
        });

        return routes.build();
    }

    private void registerBuiltinRoutes(RouteLocatorBuilder.Builder routes) {
        routes.route("gateway-health", r -> r
            .path("/actuator/**")
            .uri("forward:/"));
    }

    private String resolveRoutePrefix(String serviceName) {
        return ROUTE_MAP.get(serviceName);
    }

    private static final Map<String, String> ROUTE_MAP = new LinkedHashMap<>();
    static {
        ROUTE_MAP.put("identity", "/api/auth");
        ROUTE_MAP.put("catalog", "/api/catalog");
        ROUTE_MAP.put("order", "/api/orders");
        ROUTE_MAP.put("cart", "/api/cart");
        ROUTE_MAP.put("training", "/api/training");
        ROUTE_MAP.put("admin", "/api/admin");
        ROUTE_MAP.put("analytics", "/api/analytics");
        ROUTE_MAP.put("ai", "/api/ai");
        ROUTE_MAP.put("memory", "/api/memory");
        ROUTE_MAP.put("notification", "/api/notifications");
        ROUTE_MAP.put("payment", "/api/payments");
        ROUTE_MAP.put("inventory", "/api/inventory");
        ROUTE_MAP.put("fulfillment", "/api/fulfillment");
        ROUTE_MAP.put("support", "/api/support");
        ROUTE_MAP.put("risk", "/api/risk");
        ROUTE_MAP.put("content", "/api/content");
        ROUTE_MAP.put("search", "/api/search");
        ROUTE_MAP.put("copilot", "/api/copilot");
        ROUTE_MAP.put("trainer-copilot", "/api/v1/copilot/trainer");
    }
}
