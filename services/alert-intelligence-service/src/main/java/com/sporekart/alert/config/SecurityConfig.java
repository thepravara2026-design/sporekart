package com.sporekart.alert.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    private static final String[] PUBLIC_MATCHERS = {
            "/actuator/health", "/actuator/health/**", "/actuator/info",
            "/actuator/prometheus", "/swagger-ui/**", "/v3/api-docs/**", "/error"
    };
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable()).cors(cors -> {})
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> {
                    for (String m : PUBLIC_MATCHERS) authorize.requestMatchers(AntPathRequestMatcher.antMatcher(m)).permitAll();
                    authorize.anyRequest().authenticated();
                }).httpBasic(httpBasic -> {});
        return http.build();
    }
}
