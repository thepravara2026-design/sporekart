package com.sporekart.identity.application.service;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.PlainJWT;
import com.nimbusds.jwt.SignedJWT;
import com.sporekart.identity.config.JwtProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private static final String TEST_SECRET = "test-jwt-secret-key-for-hmac-signing-minimum-32-bytes!";
    private static final String ISSUER = "sporekart";
    private static final String SUBJECT = "user-123";

    private JwtProperties properties;
    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        properties = new JwtProperties();
        properties.setSecret(TEST_SECRET);
        properties.setIssuer(ISSUER);
        properties.setExpirationMinutes(15);
        properties.setRefreshExpirationMinutes(10080);
        properties.setClockSkewSeconds(30);
        properties.setBlacklistSize(10000);
        jwtService = new JwtService(properties);
    }

    @Test
    void shouldGenerateValidAccessToken() {
        var roles = List.of("ROLE_USER");
        var extraClaims = Map.of("claim1", "value1");
        var token = jwtService.generateAccessToken(SUBJECT, roles, extraClaims);

        assertNotNull(jwtService.validateAndParse(token));
        assertEquals(SUBJECT, jwtService.extractSubject(token));
        assertEquals(roles, jwtService.extractRoles(token));
        assertFalse(jwtService.isRefreshToken(token));
    }

    @Test
    void shouldGenerateValidRefreshToken() {
        var token = jwtService.generateRefreshToken(SUBJECT);

        assertNotNull(jwtService.validateAndParse(token));
        assertEquals(SUBJECT, jwtService.extractSubject(token));
        assertTrue(jwtService.isRefreshToken(token));
    }

    @Test
    void shouldRejectTamperedToken() {
        var token = jwtService.generateAccessToken(SUBJECT, List.of("ROLE_USER"), null);
        var parts = token.split("\\.");
        var tamperedToken = parts[0] + "." + parts[1] + "x" + "." + parts[2];

        assertNull(jwtService.validateAndParse(tamperedToken));
    }

    @Test
    void shouldRejectMalformedToken() {
        assertNull(jwtService.validateAndParse("not-a-jwt"));
    }

    @Test
    void shouldRejectNullToken() {
        assertNull(jwtService.validateAndParse(null));
        assertNull(jwtService.validateAndParse(""));
        assertNull(jwtService.validateAndParse("   "));
    }

    @Test
    void shouldExtractEmptyRolesForNoRoleToken() throws Exception {
        var claims = new JWTClaimsSet.Builder()
                .subject(SUBJECT)
                .issuer(ISSUER)
                .issueTime(Date.from(Instant.now()))
                .expirationTime(Date.from(Instant.now().plusSeconds(900)))
                .build();
        var header = new JWSHeader(JWSAlgorithm.HS256);
        var signedJwt = new SignedJWT(header, claims);
        signedJwt.sign(new MACSigner(TEST_SECRET.getBytes()));
        var token = signedJwt.serialize();

        assertTrue(jwtService.extractRoles(token).isEmpty());
    }

    @Test
    void shouldRejectUnsignedToken() throws Exception {
        var claims = new JWTClaimsSet.Builder()
                .subject(SUBJECT)
                .build();
        var plainJwt = new PlainJWT(claims);
        var unsignedToken = plainJwt.serialize();

        assertNull(jwtService.validateAndParse(unsignedToken));
    }

    @Test
    void shouldRejectBlacklistedToken() {
        var token = jwtService.generateAccessToken(SUBJECT, List.of("ROLE_USER"), null);

        assertNotNull(jwtService.validateAndParse(token));

        jwtService.blacklist(token);
        assertNull(jwtService.validateAndParse(token));
    }

    @Test
    void shouldHandleClockSkew() throws Exception {
        properties.setClockSkewSeconds(60);
        var skewService = new JwtService(properties);

        var claims = new JWTClaimsSet.Builder()
                .subject(SUBJECT)
                .issuer(ISSUER)
                .issueTime(Date.from(Instant.now().minusSeconds(60)))
                .expirationTime(Date.from(Instant.now().minusSeconds(30)))
                .build();
        var header = new JWSHeader(JWSAlgorithm.HS256);
        var signedJwt = new SignedJWT(header, claims);
        signedJwt.sign(new MACSigner(TEST_SECRET.getBytes()));
        var token = signedJwt.serialize();

        assertNotNull(skewService.validateAndParse(token));
    }

    @Test
    void shouldInitializeWithMinimumKeyLength() {
        var minKeyProps = new JwtProperties();
        minKeyProps.setSecret("abcdefghijklmnopqrstuvwxyz123456");
        minKeyProps.setIssuer(ISSUER);

        assertDoesNotThrow(() -> new JwtService(minKeyProps));
    }

    @Test
    void shouldFailWithShortKey() {
        var shortKeyProps = new JwtProperties();
        shortKeyProps.setSecret("short");
        shortKeyProps.setIssuer(ISSUER);

        assertThrows(IllegalArgumentException.class, () -> new JwtService(shortKeyProps));
    }

    @Test
    void accessTokenShouldNotBeRefreshToken() {
        var token = jwtService.generateAccessToken(SUBJECT, List.of("ROLE_USER"), null);
        assertFalse(jwtService.isRefreshToken(token));

        var refreshToken = jwtService.generateRefreshToken(SUBJECT);
        assertTrue(jwtService.isRefreshToken(refreshToken));
    }
}
