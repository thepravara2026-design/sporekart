package com.sporekart.ai.provider.selectors;

import com.sporekart.ai.provider.models.ProviderRequest;

import java.util.Map;
import java.util.Optional;

public record SelectionContext(
        String module,
        Optional<String> preferredProvider,
        ProviderRequest request,
        Map<String, String> routingHints) {

    public SelectionContext {
        routingHints = routingHints == null ? Map.of() : Map.copyOf(routingHints);
        preferredProvider = preferredProvider == null ? Optional.empty() : preferredProvider;
    }

    public static SelectionContext forModule(String module) {
        return new SelectionContext(module, Optional.empty(), null, Map.of());
    }

    public static SelectionContext forRequest(String module, ProviderRequest request) {
        return new SelectionContext(module, Optional.empty(), request, Map.of());
    }

    public SelectionContext withPreferredProvider(String provider) {
        return new SelectionContext(module, Optional.of(provider), request, routingHints);
    }

    public SelectionContext withHint(String key, String value) {
        var mutable = new java.util.HashMap<>(routingHints);
        mutable.put(key, value);
        return new SelectionContext(module, preferredProvider, request, mutable);
    }
}
