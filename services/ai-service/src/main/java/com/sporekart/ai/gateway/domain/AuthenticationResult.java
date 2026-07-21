package com.sporekart.ai.gateway.domain;

import java.util.Optional;

public record AuthenticationResult(
    boolean authenticated,
    String userId,
    String tenantId,
    java.util.List<String> roles,
    String tokenType,
    String failureReason
) {
    public static AuthenticationResult success(String userId, String tenantId, java.util.List<String> roles) {
        return new AuthenticationResult(true, userId, tenantId, roles, "bearer", null);
    }

    public static AuthenticationResult failure(String reason) {
        return new AuthenticationResult(false, null, null, java.util.List.of(), null, reason);
    }

    public Optional<String> getFailureReason() { return Optional.ofNullable(failureReason); }
}
