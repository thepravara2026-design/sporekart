package com.sporekart.gateway.config;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.OctetSequenceKey;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;

import javax.crypto.spec.SecretKeySpec;

@Configuration
public class JwtDecoderConfig {

    @Bean
    public ReactiveJwtDecoder reactiveJwtDecoder(GatewayConfig gatewayConfig) {
        var secret = gatewayConfig.getSecurity().getJwtSecret();
        if (secret != null && !secret.isBlank()) {
            var key = new OctetSequenceKey.Builder(secret.getBytes())
                .algorithm(JWSAlgorithm.HS256)
                .build();
            return NimbusReactiveJwtDecoder.withSecretKey(
                new SecretKeySpec(key.toByteArray(), "HmacSHA256")
            ).build();
        }
        var jwkSetUri = gatewayConfig.getSecurity().getJwkSetUri();
        if (jwkSetUri != null && !jwkSetUri.isBlank()) {
            return NimbusReactiveJwtDecoder.withJwkSetUri(jwkSetUri).build();
        }
        throw new IllegalStateException("JWT authentication requires either sporekart.gateway.security.jwt-secret or spring.security.oauth2.resourceserver.jwt.jwk-set-uri");
    }
}
