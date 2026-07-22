package com.sporekart.gateway.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JwtValidator {

    private static final Logger log = LoggerFactory.getLogger(JwtValidator.class);

    public JwtClaims validate(String token) {
        if (token == null || token.isBlank()) {
            return null;
        }
        try {
            var parts = token.split("\\.");
            if (parts.length != 3) {
                return null;
            }
            var payload = new String(java.util.Base64.getUrlDecoder().decode(parts[1]));
            var userId = extractClaim(payload, "sub");
            var roles = extractRoles(payload);
            return new JwtClaims(userId, roles);
        } catch (Exception e) {
            log.warn("JWT validation failed: {}", e.getMessage());
            return null;
        }
    }

    private String extractClaim(String payload, String claim) {
        var search = "\"" + claim + "\":\"";
        var start = payload.indexOf(search);
        if (start == -1) return "unknown";
        start += search.length();
        var end = payload.indexOf("\"", start);
        return end == -1 ? "unknown" : payload.substring(start, end);
    }

    private List<String> extractRoles(String payload) {
        var search = "\"roles\":[";
        var start = payload.indexOf(search);
        if (start == -1) return List.of();
        start += search.length();
        var end = payload.indexOf("]", start);
        if (end == -1) return List.of();
        var rolesStr = payload.substring(start, end);
        return java.util.Arrays.stream(rolesStr.split(","))
            .map(r -> r.replaceAll("\"", "").trim())
            .filter(r -> !r.isEmpty())
            .toList();
    }

    public record JwtClaims(String subject, List<String> roles) {}
}
