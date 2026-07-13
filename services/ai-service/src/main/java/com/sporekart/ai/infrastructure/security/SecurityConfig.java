package com.sporekart.ai.infrastructure.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/actuator/health", "/actuator/info",
                                "/v3/api-docs/**", "/swagger-ui/**",
                                "/api/v1/ai/health", "/api/v1/ai/status", "/api/v1/ai/features",
                                "/api/v1/ai/providers", "/api/v1/ai/providers/**",
                                "/api/v1/ai/providers/capabilities", "/api/v1/ai/providers/health",
                                "/api/v1/ai/prompts", "/api/v1/ai/prompts/**",
                                "/api/v1/ai/prompts/categories", "/api/v1/ai/prompts/render",
                                "/api/v1/ai/prompts/history", "/api/v1/ai/prompts/export",
                                "/api/v1/ai/prompts/import",
                                "/api/v1/knowledge/**",
                                "/api/v1/semantic/**",
                                "/api/v1/conversation/**",
                                "/api/v1/workflows/**",
                                "/api/v1/content/**",
                                "/api/v1/assistants/**",
                                "/api/v1/governance/**",
                                "/api/v1/policies/**",
                                "/api/v1/decisions/**",
                                "/api/v1/approvals/**",
                                "/api/v1/compliance/**",
                                "/api/v1/risk/**",
                                "/api/v1/admin/**",
                                "/api/v1/provider-registry/**",
                                "/api/v1/prompt-registry/**",
                                "/api/v1/knowledge-registry/**",
                                "/api/v1/usage-tracking/**",
                                "/api/v1/config-registry/**",
                                "/api/v1/event-catalog/**",
                                "/api/v1/api-registry/**",
                                "/api/v1/capability-discovery/**")
                        .permitAll()
                        .requestMatchers(HttpMethod.GET, "/warehouses/**")
                        .hasAnyRole("WAREHOUSE_MANAGER", "OPERATIONS_MANAGER", "ADMINISTRATOR")
                        .requestMatchers(HttpMethod.POST, "/warehouses/**")
                        .hasAnyRole("WAREHOUSE_MANAGER", "ADMINISTRATOR")
                        .requestMatchers(HttpMethod.GET, "/purchase-orders/**", "/procurement/**")
                        .hasAnyRole("PROCUREMENT_MANAGER", "OPERATIONS_MANAGER", "ADMINISTRATOR")
                        .requestMatchers(HttpMethod.POST, "/purchase-orders/**", "/procurement/**")
                        .hasAnyRole("PROCUREMENT_MANAGER", "ADMINISTRATOR")
                        .requestMatchers(HttpMethod.GET, "/suppliers/**")
                        .hasAnyRole("SUPPLIER_MANAGER", "OPERATIONS_MANAGER", "ADMINISTRATOR")
                        .requestMatchers(HttpMethod.POST, "/suppliers/**")
                        .hasAnyRole("SUPPLIER_MANAGER", "ADMINISTRATOR")
                        .requestMatchers("/erp/**")
                        .hasAnyRole("OPERATIONS_MANAGER", "ADMINISTRATOR")
                        .requestMatchers(HttpMethod.GET, "/operations/**")
                        .hasAnyRole("OPERATIONS_MANAGER", "ADMINISTRATOR")
                        .requestMatchers(HttpMethod.POST, "/operations/**")
                        .hasRole("ADMINISTRATOR")
                        .anyRequest()
                        .authenticated())
                .httpBasic(Customizer.withDefaults());
        return http.build();
    }
}
