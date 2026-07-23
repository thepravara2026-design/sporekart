package com.sporekart.identity.application.service;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.sporekart.identity.common.exception.SecurityException;
import com.sporekart.identity.config.JwtProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class JwtService {

    private static final Logger log = LoggerFactory.getLogger(JwtService.class);

    private final JwtProperties properties;
    private final MACSigner signer;
    private final MACVerifier verifier;
    private final Set<String> blacklist = ConcurrentHashMap.newKeySet();

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

    public String generateAccessToken(String subject, List<String> roles, Map<String, Object> extraClaims) {
        var now = Instant.now();
        var skew = properties.getClockSkewSeconds();
        var claimsBuilder = new JWTClaimsSet.Builder()
                .subject(subject)
                .issuer(properties.getIssuer())
                .issueTime(Date.from(now.minusSeconds(skew)))
                .expirationTime(Date.from(now.plusSeconds(properties.getExpirationMinutes() * 60)))
                .jwtID(UUID.randomUUID().toString())
                .claim("type", "access")
                .claim("roles", roles);
        if (extraClaims != null) {
            extraClaims.forEach(claimsBuilder::claim);
        }
        return sign(claimsBuilder.build());
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
        if (token == null || token.isBlank()) return null;
        if (blacklist.contains(token)) {
            log.warn("JWT is blacklisted");
            return null;
        }
        try {
            var signedJwt = SignedJWT.parse(token);
            if (!signedJwt.verify(verifier)) {
                log.warn("JWT signature verification failed");
                return null;
            }
            var claims = signedJwt.getJWTClaimsSet();
            var exp = claims.getExpirationTime();
            var skewMs = properties.getClockSkewSeconds() * 1000L;
            if (exp != null && exp.getTime() + skewMs < System.currentTimeMillis()) {
                log.warn("JWT is expired (with clock skew tolerance)");
                return null;
            }
            return signedJwt;
        } catch (ParseException | JOSEException e) {
            log.warn("JWT validation failed: {}", e.getMessage());
            return null;
        }
    }

    public void blacklist(String token) {
        blacklist.add(token);
        if (blacklist.size() > properties.getBlacklistSize()) {
            var iter = blacklist.iterator();
            iter.next();
            iter.remove();
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

    public String extractJti(String token) {
        var parsed = validateAndParse(token);
        if (parsed == null) return null;
        try {
            return parsed.getJWTClaimsSet().getJWTID();
        } catch (ParseException e) {
            return null;
        }
    }

    private String sign(JWTClaimsSet claims) {
        try {
            var header = new JWSHeader(JWSAlgorithm.HS256);
            var signedJwt = new SignedJWT(header, claims);
            signedJwt.sign(signer);
            return signedJwt.serialize();
        } catch (JOSEException e) {
            throw new SecurityException("Failed to sign JWT", e);
        }
    }
}
