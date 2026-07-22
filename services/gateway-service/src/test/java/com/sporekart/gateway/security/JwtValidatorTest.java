package com.sporekart.gateway.security;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;

import javax.crypto.spec.SecretKeySpec;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class JwtValidatorTest {

    private static final String TEST_SECRET = "test-jwt-secret-key-for-hmac-signing-in-tests-only";
    private final ReactiveJwtDecoder decoder = NimbusReactiveJwtDecoder.withSecretKey(
        new SecretKeySpec(TEST_SECRET.getBytes(), "HmacSHA256")
    ).build();

    @Test
    void shouldValidateWellFormedToken() {
        var token = JwtTestHelper.createSignedToken("user123", List.of("ADMIN", "USER"));
        var jwt = decoder.decode(token).block();
        assertThat(jwt).isNotNull();
        assertThat(jwt.getSubject()).isEqualTo("user123");
        assertThat(jwt.getClaimAsStringList("roles")).containsExactly("ADMIN", "USER");
    }

    @Test
    void shouldHandleEmptyRoles() {
        var token = JwtTestHelper.createSignedToken("nobody", List.of());
        var jwt = decoder.decode(token).block();
        assertThat(jwt).isNotNull();
        assertThat(jwt.getClaimAsStringList("roles")).isEmpty();
    }

    @Test
    void shouldRejectUnsignedToken() {
        var token = JwtTestHelper.createUnsignedToken("attacker", List.of("ADMIN"));
        var result = decoder.decode(token)
            .map(jwt -> true)
            .onErrorReturn(false)
            .block();
        assertThat(result).isFalse();
    }

    @Test
    void shouldRejectMalformedToken() {
        var result = decoder.decode("not-a-valid-jwt")
            .map(jwt -> true)
            .onErrorReturn(false)
            .block();
        assertThat(result).isFalse();
    }

    @Test
    void shouldRejectEmptyToken() {
        var result = decoder.decode("")
            .map(jwt -> true)
            .onErrorReturn(false)
            .block();
        assertThat(result).isFalse();
    }

    @Test
    void shouldRejectForgedSignature() {
        var token = JwtTestHelper.createUnsignedToken("hacker", List.of("SUPER_ADMIN"));
        var result = decoder.decode(token)
            .map(jwt -> true)
            .onErrorReturn(false)
            .block();
        assertThat(result).isFalse();
    }

    @Test
    void shouldHandleMultipleRoles() {
        var token = JwtTestHelper.createSignedToken("superuser", List.of("ADMIN", "ANALYST", "USER", "SYSTEM"));
        var jwt = decoder.decode(token).block();
        assertThat(jwt).isNotNull();
        assertThat(jwt.getClaimAsStringList("roles")).containsExactly("ADMIN", "ANALYST", "USER", "SYSTEM");
    }
}
