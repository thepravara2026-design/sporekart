package com.sporekart.gateway.health;

import com.sporekart.gateway.config.GatewayConfig;
import com.sporekart.gateway.registry.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.ReactiveHealthIndicator;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class AggregatedHealthIndicator implements ReactiveHealthIndicator {

    private static final Logger log = LoggerFactory.getLogger(AggregatedHealthIndicator.class);

    private final GatewayConfig gatewayConfig;
    private final ServiceRegistry serviceRegistry;
    private final HttpClient httpClient;

    public AggregatedHealthIndicator(GatewayConfig gatewayConfig, ServiceRegistry serviceRegistry) {
        this.gatewayConfig = gatewayConfig;
        this.serviceRegistry = serviceRegistry;
        this.httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(3))
            .build();
    }

    @Override
    public Mono<Health> health() {
        return Mono.fromCallable(() -> {
            var builder = Health.up();
            var services = serviceRegistry.getAll();

            if (services.isEmpty()) {
                return builder
                    .withDetail("services", Map.of())
                    .withDetail("totalServices", 0)
                    .build();
            }

            var details = new ConcurrentHashMap<String, Object>();
            var totalServices = services.size();
            var healthyCount = services.entrySet().stream()
                .map(entry -> {
                    try {
                        var healthy = checkService(entry.getKey(), entry.getValue());
                        details.put(entry.getKey(), healthy ? "UP" : "DOWN");
                        return healthy;
                    } catch (Exception e) {
                        details.put(entry.getKey(), "DOWN");
                        return false;
                    }
                })
                .filter(h -> h)
                .count();

            builder.withDetail("services", details)
                .withDetail("totalServices", totalServices)
                .withDetail("healthyServices", healthyCount)
                .withDetail("unhealthyServices", totalServices - healthyCount);

            if (healthyCount < totalServices) {
                builder.down();
            }

            return builder.build();
        });
    }

    private boolean checkService(String name, ServiceRegistry.ServiceInstance instance) {
        try {
            var healthUrl = instance.url().replaceAll("/+$", "") + "/" + instance.healthPath().replaceAll("^/+", "");
            var request = HttpRequest.newBuilder()
                .uri(URI.create(healthUrl))
                .timeout(Duration.parse("PT" + instance.timeout()))
                .GET()
                .build();
            var response = httpClient.send(request, HttpResponse.BodyHandlers.discarding());
            return response.statusCode() == 200;
        } catch (Exception e) {
            log.debug("Health check failed for service {}: {}", name, e.getMessage());
            return false;
        }
    }
}
