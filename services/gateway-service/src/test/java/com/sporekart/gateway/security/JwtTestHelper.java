package com.sporekart.gateway.security;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import java.util.List;

public final class JwtTestHelper {

    private static final String TEST_SECRET = "test-jwt-secret-key-for-hmac-signing-in-tests-only";

    private JwtTestHelper() {}

    public static String createSignedToken(String subject, List<String> roles) {
        try {
            var claimsSet = new JWTClaimsSet.Builder()
                .subject(subject)
                .claim("roles", roles)
                .build();
            var header = new JWSHeader(JWSAlgorithm.HS256);
            var signedJwt = new SignedJWT(header, claimsSet);
            var signer = new MACSigner(TEST_SECRET);
            signedJwt.sign(signer);
            return signedJwt.serialize();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create test JWT", e);
        }
    }

    public static String createUnsignedToken(String subject, List<String> roles) {
        var header = java.util.Base64.getUrlEncoder().withoutPadding()
            .encodeToString("{\"alg\":\"none\"}".getBytes());
        var rolesJson = roles.stream()
            .map(r -> "\"" + r + "\"")
            .collect(java.util.stream.Collectors.joining(","));
        var payload = java.util.Base64.getUrlEncoder().withoutPadding()
            .encodeToString(("{\"sub\":\"" + subject + "\",\"roles\":[" + rolesJson + "]}").getBytes());
        return header + "." + payload + ".invalidsig";
    }
}
