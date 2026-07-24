package com.sporekart.identity.application.service;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.sporekart.identity.config.JwtProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class JwtService {

    private static final Logger log = LoggerFactory.getLogger(JwtService.class);

    private final JwtProperties properties;
    private final MACSigner signer;
    private final MACVerifier verifier;

    public JwtService(JwtProperties properties) {
        this.properties = properties;
        try {
            var keyBytes = properties.getSecret().getBytes();
            if (keyBytes.length < 32) {
                throw new IllegalArgumentException("JWT secret must be at least 32 bytes for HMAC-SHA256");
            }
            this.signer = new MACSigner(keyBytes);
            this.verifier = new MACVerifier(keyBytes);
        } catch (JOSEException e) {
            throw new IllegalStateException("Failed to initialize JWT signer", e);
        }
    }

    public String generateAccessToken(String subject, List<String> roles) {
        var now = Instant.now();
        var claims = new JWTClaimsSet.Builder()
                .subject(subject)
                .issuer(properties.getIssuer())
                .issueTime(Date.from(now))
                .expirationTime(Date.from(now.plusSeconds(properties.getExpirationMinutes() * 60)))
                .jwtID(UUID.randomUUID().toString())
                .claim("roles", roles)
                .build();
        return sign(claims);
    }

    public String generateRefreshToken(String subject) {
        var now = Instant.now();
        var claims = new JWTClaimsSet.Builder()
                .subject(subject)
                .issuer(properties.getIssuer())
                .issueTime(Date.from(now))
                .expirationTime(Date.from(now.plusSeconds(properties.getRefreshExpirationMinutes() * 60)))
                .jwtID(UUID.randomUUID().toString())
                .claim("type", "refresh")
                .build();
        return sign(claims);
    }

    public SignedJWT validateAndParse(String token) {
        try {
            var signedJwt = SignedJWT.parse(token);
            if (!signedJwt.verify(verifier)) {
                log.warn("JWT signature verification failed");
                return null;
            }
            var exp = signedJwt.getJWTClaimsSet().getExpirationTime();
            if (exp != null && exp.before(new Date())) {
                log.warn("JWT is expired");
                return null;
            }
            return signedJwt;
        } catch (ParseException | JOSEException e) {
            log.warn("JWT validation failed: {}", e.getMessage());
            return null;
        }
    }

    public String extractSubject(String token) {
        var parsed = validateAndParse(token);
        if (parsed == null) return null;
        try {
            return parsed.getJWTClaimsSet().getSubject();
        } catch (ParseException e) {
            return null;
        }
    }

    public List<String> extractRoles(String token) {
        var parsed = validateAndParse(token);
        if (parsed == null) return List.of();
        try {
            var roles = parsed.getJWTClaimsSet().getStringListClaim("roles");
            return roles != null ? List.of(roles) : List.of();
        } catch (ParseException e) {
            return List.of();
        }
    }

    public boolean isRefreshToken(String token) {
        var parsed = validateAndParse(token);
        if (parsed == null) return false;
        try {
            return "refresh".equals(parsed.getJWTClaimsSet().getStringClaim("type"));
        } catch (ParseException e) {
            return false;
        }
    }

    private String sign(JWTClaimsSet claims) {
        try {
            var header = new JWSHeader(JWSAlgorithm.HS256);
            var signedJwt = new SignedJWT(header, claims);
            signedJwt.sign(signer);
            return signedJwt.serialize();
        } catch (JOSEException e) {
            throw new IllegalStateException("Failed to sign JWT", e);
        }
    }
}
