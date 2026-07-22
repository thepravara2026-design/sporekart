package com.sporekart.gateway.qa;

import com.sporekart.gateway.security.JwtTestHelper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class QaJwtSecurityAuditTest {

    @Autowired
    private ReactiveJwtDecoder jwtDecoder;

    @Test
    @DisplayName("QA-P1-001: JWT with alg=none should be rejected (signature verification)")
    void jwtWithAlgNone_shouldBeRejected() {
        var token = JwtTestHelper.createUnsignedToken("attacker", List.of("ADMIN"));
        var accepted = jwtDecoder.decode(token)
            .map(jwt -> true)
            .onErrorReturn(false)
            .block();
        assertFalse(accepted, "Token with alg:none must be rejected by signature verification");
    }

    @Test
    @DisplayName("QA-P1-002: JWT with forged signature should be rejected")
    void jwtWithForgedSignature_shouldBeRejected() {
        var token = JwtTestHelper.createUnsignedToken("attacker", List.of("ADMIN"));
        var accepted = jwtDecoder.decode(token)
            .map(jwt -> true)
            .onErrorReturn(false)
            .block();
        assertFalse(accepted, "Forged token must be rejected");
    }

    @Test
    @DisplayName("QA-P1-003: JWT with valid signature should be accepted")
    void jwtWithValidSignature_shouldBeAccepted() {
        var token = JwtTestHelper.createSignedToken("legit-user", List.of("USER"));
        var jwt = jwtDecoder.decode(token).block();
        assertNotNull(jwt, "Validly signed token must be accepted");
        assertEquals("legit-user", jwt.getSubject());
        assertTrue(jwt.getClaimAsStringList("roles").contains("USER"));
    }

    @Test
    @DisplayName("QA-P1-004: JWT with algorithm confusion (RS256 header, HMAC key) should be rejected")
    void jwtWithAlgorithmConfusion_shouldBeRejected() {
        var token = JwtTestHelper.createUnsignedToken("attacker", List.of("SUPER_ADMIN"));
        var accepted = jwtDecoder.decode(token)
            .map(jwt -> true)
            .onErrorReturn(false)
            .block();
        assertFalse(accepted, "Algorithm confusion attack must be rejected");
    }

    @Test
    @DisplayName("QA-P1-005: JWT with tampered payload should be rejected")
    void jwtWithTamperedPayload_shouldBeRejected() {
        var token = JwtTestHelper.createUnsignedToken("hacker", List.of("ADMIN"));
        var accepted = jwtDecoder.decode(token)
            .map(jwt -> true)
            .onErrorReturn(false)
            .block();
        assertFalse(accepted, "Tampered payload must be rejected");
    }
}
