package com.sporekart.gateway.registry;

import com.sporekart.gateway.config.GatewayConfig;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ServiceRegistry {

    private static final Logger log = LoggerFactory.getLogger(ServiceRegistry.class);

    private final Map<String, ServiceInstance> services = new ConcurrentHashMap<>();
    private final GatewayConfig gatewayConfig;

    public ServiceRegistry(GatewayConfig gatewayConfig) {
        this.gatewayConfig = gatewayConfig;
    }

    @PostConstruct
    public void init() {
        var configured = gatewayConfig.getServices();
        if (configured == null || configured.isEmpty()) {
            log.warn("No services configured in sporekart.gateway.services");
            return;
        }
        configured.forEach((name, cfg) -> {
            var instance = new ServiceInstance(name, cfg.getUrl(), cfg.getHealthPath(), cfg.getTimeout(), cfg.getRoles());
            services.put(name, instance);
            log.info("Registered service: {} -> {}", name, cfg.getUrl());
        });
        log.info("Service registry initialized with {} services", services.size());
    }

    public void register(String name, ServiceInstance instance) {
        services.put(name, instance);
        log.info("Registered service: {} -> {}", name, instance.url());
    }

    public void deregister(String name) {
        services.remove(name);
        log.info("Deregistered service: {}", name);
    }

    public ServiceInstance get(String name) {
        return services.get(name);
    }

    public Map<String, ServiceInstance> getAll() {
        return Collections.unmodifiableMap(services);
    }

    public boolean contains(String name) {
        return services.containsKey(name);
    }

    public int size() {
        return services.size();
    }

    public record ServiceInstance(
        String name,
        String url,
        String healthPath,
        String timeout,
        java.util.List<String> requiredRoles
    ) {}
}
