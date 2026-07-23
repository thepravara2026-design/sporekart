package com.sporekart.identity.application.service;

import com.sporekart.identity.config.JwtProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private static final String TEST_SECRET = "test-jwt-secret-key-for-hmac-signing-minimum-32-bytes!";
    private static final String TEST_ISSUER = "https://identity.sporekart.test";

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        var props = new JwtProperties();
        props.setSecret(TEST_SECRET);
        props.setIssuer(TEST_ISSUER);
        props.setExpirationMinutes(15);
        jwtService = new JwtService(props);
    }

    @Test
    void shouldGenerateValidAccessToken() {
        var token = jwtService.generateAccessToken("user-123", List.of("CUSTOMER", "ADMIN"));
        assertNotNull(token);
        assertFalse(token.isBlank());

        var subject = jwtService.extractSubject(token);
        assertEquals("user-123", subject);

        var roles = jwtService.extractRoles(token);
        assertTrue(roles.contains("CUSTOMER"));
        assertTrue(roles.contains("ADMIN"));
    }

    @Test
    void shouldGenerateValidRefreshToken() {
        var token = jwtService.generateRefreshToken("user-123");
        assertNotNull(token);
        assertFalse(token.isBlank());
        assertTrue(jwtService.isRefreshToken(token));
    }

    @Test
    void accessTokenShouldNotBeRefreshToken() {
        var token = jwtService.generateAccessToken("user-123", List.of("CUSTOMER"));
        assertFalse(jwtService.isRefreshToken(token));
    }

    @Test
    void shouldRejectTamperedToken() {
        var token = jwtService.generateAccessToken("user-123", List.of("CUSTOMER"));
        var tampered = token.substring(0, token.lastIndexOf('.') + 1) + "invalidsig";
        assertNull(jwtService.extractSubject(tampered));
    }

    @Test
    void shouldRejectMalformedToken() {
        assertNull(jwtService.extractSubject("not-a-jwt"));
        assertNull(jwtService.extractSubject(""));
        assertNull(jwtService.extractSubject("a.b.c"));
    }

    @Test
    void shouldRejectNullToken() {
        assertNull(jwtService.extractSubject(null));
    }

    @Test
    void shouldExtractEmptyRolesForNoRoleToken() {
        var token = jwtService.generateAccessToken("user-123", List.of());
        var roles = jwtService.extractRoles(token);
        assertTrue(roles.isEmpty());
    }

    @Test
    void shouldRejectUnsignedToken() {
        var unsigned = "eyJhbGciOiJub25lIn0.eyJzdWIiOiJ1c2VyLTEyMyJ9.";
        assertNull(jwtService.extractSubject(unsigned));
    }

    @Test
    void shouldInitializeWithMinimumKeyLength() {
        var props = new JwtProperties();
        props.setSecret("12345678901234567890123456789012"); // 32 bytes
        props.setIssuer(TEST_ISSUER);
        assertDoesNotThrow(() -> new JwtService(props));
    }

    @Test
    void shouldFailWithShortKey() {
        var props = new JwtProperties();
        props.setSecret("short"); // < 32 bytes
        props.setIssuer(TEST_ISSUER);
        assertThrows(IllegalArgumentException.class, () -> new JwtService(props));
    }
}
