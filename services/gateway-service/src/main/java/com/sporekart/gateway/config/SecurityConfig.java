package com.sporekart.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
            .csrf(ServerHttpSecurity.CsrfSpec::disable)
            .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
            .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
            .logout(ServerHttpSecurity.LogoutSpec::disable)
            .authorizeExchange(exchanges -> exchanges
                .pathMatchers(
                    "/actuator/health/**",
                    "/actuator/info",
                    "/api/public/**",
                    "/webjars/**",
                    "/v3/api-docs/**",
                    "/swagger-ui/**",
                    "/fallback/**"
                ).permitAll()
                .anyExchange().permitAll()
            )
                .headers(headers -> headers
                .hsts(hsts -> hsts
                    .includeSubdomains(true)
                    .maxAge(java.time.Duration.ofDays(365)))
                .contentTypeOptions(ServerHttpSecurity.HeaderSpec.ContentTypeOptionsSpec::disable)
                .frameOptions(frame -> frame.mode(
                    org.springframework.security.web.server.header.XFrameOptionsServerHttpHeadersWriter.Mode.DENY))
                .xssProtection(ServerHttpSecurity.HeaderSpec.XssProtectionSpec::disable)
            );
        return http.build();
    }
}
