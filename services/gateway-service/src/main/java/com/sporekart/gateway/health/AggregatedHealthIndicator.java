package com.sporekart.gateway.health;

import com.sporekart.gateway.config.GatewayConfig;
import com.sporekart.gateway.registry.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.ReactiveHealthIndicator;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class AggregatedHealthIndicator implements ReactiveHealthIndicator {

    private static final Logger log = LoggerFactory.getLogger(AggregatedHealthIndicator.class);

    private final GatewayConfig gatewayConfig;
    private final ServiceRegistry serviceRegistry;
    private final WebClient webClient;

    public AggregatedHealthIndicator(GatewayConfig gatewayConfig, ServiceRegistry serviceRegistry, WebClient.Builder webClientBuilder) {
        this.gatewayConfig = gatewayConfig;
        this.serviceRegistry = serviceRegistry;
        this.webClient = webClientBuilder.build();
    }

    @Override
    public Mono<Health> health() {
        var services = serviceRegistry.getAll();

        if (services.isEmpty()) {
            return Mono.just(Health.up()
                .withDetail("services", Map.of())
                .withDetail("totalServices", 0)
                .build());
        }

        var details = new ConcurrentHashMap<String, Object>();
        var totalServices = services.size();

        return Flux.fromIterable(services.entrySet())
            .flatMap(entry -> checkService(entry.getKey(), entry.getValue())
                .doOnNext(healthy -> details.put(entry.getKey(), healthy ? "UP" : "DOWN")))
            .filter(h -> h)
            .count()
            .map(healthyCount -> {
                var builder = healthyCount == totalServices ? Health.up() : Health.down();
                return builder
                    .withDetail("services", details)
                    .withDetail("totalServices", totalServices)
                    .withDetail("healthyServices", healthyCount)
                    .withDetail("unhealthyServices", totalServices - healthyCount)
                    .build();
            });
    }

    private Mono<Boolean> checkService(String name, ServiceRegistry.ServiceInstance instance) {
        var healthUrl = instance.url().replaceAll("/+$", "") + "/" + instance.healthPath().replaceAll("^/+", "");
        return webClient.get()
            .uri(healthUrl)
            .retrieve()
            .toBodilessEntity()
            .map(response -> response.getStatusCode().is2xxSuccessful())
            .onErrorResume(e -> {
                log.debug("Health check failed for service {}: {}", name, e.getMessage());
                return Mono.just(false);
            });
    }
}
