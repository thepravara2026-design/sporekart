package com.sporekart.ai.gateway.domain;

import java.util.Optional;

public record RoutingDecision(
    String providerId,
    String model,
    String strategy,
    String reason,
    int priority,
    boolean fallback,
    boolean resolved
) {
    public static RoutingDecision unresolved() {
        return new RoutingDecision(null, null, null, "No provider resolved", 0, false, false);
    }

    public Optional<String> getProviderId() { return Optional.ofNullable(providerId); }
}
