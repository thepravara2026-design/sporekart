package com.sporekart.gateway.proxy;

import com.sporekart.gateway.registry.ServiceRegistry;

import java.net.URI;

public class ServiceProxy {

    private final ServiceRegistry serviceRegistry;

    public ServiceProxy(ServiceRegistry serviceRegistry) {
        this.serviceRegistry = serviceRegistry;
    }

    public URI resolveTarget(String serviceName, String path) {
        var instance = serviceRegistry.get(serviceName);
        if (instance == null) {
            throw new IllegalArgumentException("Unknown service: " + serviceName);
        }
        var baseUrl = instance.url().replaceAll("/+$", "");
        var targetPath = path.startsWith("/") ? path : "/" + path;
        return URI.create(baseUrl + targetPath);
    }

    public String extractServiceName(String path) {
        if (path == null || path.isEmpty()) return null;
        var segments = path.split("/");
        if (segments.length < 2) return null;

        var routeGroup = segments[1].toLowerCase();
        return switch (routeGroup) {
            case "api" -> extractServiceFromApiPath(segments);
            case "internal" -> segments.length > 2 ? segments[2] : null;
            default -> routeGroup;
        };
    }

    private String extractServiceFromApiPath(String[] segments) {
        if (segments.length < 3) return null;
        var area = segments[2].toLowerCase();
        return switch (area) {
            case "auth", "identity" -> "identity";
            case "catalog" -> "catalog";
            case "orders" -> "order";
            case "cart" -> "cart";
            case "training" -> "training";
            case "admin" -> "admin";
            case "analytics" -> "analytics";
            case "ai" -> "ai";
            case "public" -> null;
            default -> area;
        };
    }
}
