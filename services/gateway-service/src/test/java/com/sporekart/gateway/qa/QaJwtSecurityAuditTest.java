package com.sporekart.gateway.qa;

import com.sporekart.gateway.security.JwtValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

class QaJwtSecurityAuditTest {

    private final JwtValidator validator = new JwtValidator();

    private String createToken(String payload) {
        var header = Base64.getUrlEncoder().withoutPadding().encodeToString("{\"alg\":\"none\",\"typ\":\"JWT\"}".getBytes());
        var encodedPayload = Base64.getUrlEncoder().withoutPadding().encodeToString(payload.getBytes());
        return header + "." + encodedPayload + ".";
    }

    @Test
    @DisplayName("QA-P1-001: JWT with alg=none should NOT be accepted as valid")
    void jwtWithAlgNone_shouldBeRejected() {
        var token = createToken("{\"sub\":\"admin\",\"roles\":[\"ADMIN\"]}");
        var claims = validator.validate(token);
        var isRejected = (claims == null);
        assertTrue(isRejected, "P1 DEFECT: Token with alg:none was accepted. JwtValidator does not verify cryptographic signature.");
    }

    @Test
    @DisplayName("QA-P1-002: JWT with empty signature should NOT be accepted")
    void jwtWithEmptySignature_shouldBeRejected() {
        var header = Base64.getUrlEncoder().withoutPadding().encodeToString("{\"alg\":\"HS256\"}".getBytes());
        var payload = Base64.getUrlEncoder().withoutPadding().encodeToString("{\"sub\":\"user\",\"roles\":[\"USER\"]}".getBytes());
        var token = header + "." + payload + ".fakesignature";
        var claims = validator.validate(token);
        assertNull(claims, "P1 DEFECT: Token with invalid signature was accepted");
    }

    @Test
    @DisplayName("QA-P1-003: JWT with forged payload should NOT be accepted")
    void jwtWithForgedPayload_shouldBeRejected() {
        var header = Base64.getUrlEncoder().withoutPadding().encodeToString("{\"alg\":\"HS256\"}".getBytes());
        var payload = Base64.getUrlEncoder().withoutPadding().encodeToString("{\"sub\":\"attacker\",\"roles\":[\"ADMIN\"]}".getBytes());
        var token = header + "." + payload + ".invalidsig";
        var claims = validator.validate(token);
        assertNull(claims, "P1 DEFECT: Forged token with admin claims was accepted");
    }

    @Test
    @DisplayName("QA-P1-004: JWT with expired timestamp should be rejected if exp is validated")
    void jwtWithExpiredClaim_shouldBeRejected() {
        var pastExp = System.currentTimeMillis() / 1000 - 3600;
        var payload = "{\"sub\":\"user\",\"roles\":[\"USER\"],\"exp\":" + pastExp + "}";
        var token = createToken(payload);
        var claims = validator.validate(token);
        assertNull(claims, "P1 DEFECT: Expired token was accepted. No exp claim validation.");
    }

    @Test
    @DisplayName("QA-P1-005: JWT with modified payload should NOT preserve integrity")
    void jwtWithTamperedPayload_shouldBeRejected() {
        var header = Base64.getUrlEncoder().withoutPadding().encodeToString("{\"alg\":\"HS256\"}".getBytes());
        var payload = Base64.getUrlEncoder().withoutPadding().encodeToString("{\"sub\":\"admin\",\"roles\":[\"ADMIN\"]}".getBytes());
        var tamperedPayload = Base64.getUrlEncoder().withoutPadding().encodeToString("{\"sub\":\"hacker\",\"roles\":[\"ADMIN\"]}".getBytes());
        var token = header + "." + payload + ".signature";
        var tamperedToken = header + "." + tamperedPayload + ".signature";
        var claims = validator.validate(tamperedToken);
        assertNull(claims, "P1 DEFECT: Tampered payload was accepted. No signature validation.");
    }

    @Test
    @DisplayName("QA-P1-006: JWT with algorithm confusion (RS256 claimed, no verification)")
    void jwtWithAlgorithmConfusion_shouldBeRejected() {
        var header = Base64.getUrlEncoder().withoutPadding().encodeToString("{\"alg\":\"RS256\"}".getBytes());
        var payload = Base64.getUrlEncoder().withoutPadding().encodeToString("{\"sub\":\"admin\",\"roles\":[\"SUPER_ADMIN\"]}".getBytes());
        var token = header + "." + payload + ".anybytes";
        var claims = validator.validate(token);
        assertNull(claims, "P1 DEFECT: Algorithm confusion attack succeeded. No key-based verification.");
    }
}
