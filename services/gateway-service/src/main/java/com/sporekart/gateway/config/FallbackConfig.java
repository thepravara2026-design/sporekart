package com.sporekart.gateway.config;

import com.sporekart.gateway.error.ProblemDetails;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class FallbackConfig {

    @Bean
    public WebFilter fallbackFilter(ObjectMapper objectMapper) {
        return (ServerWebExchange exchange, WebFilterChain chain) -> {
            var path = exchange.getRequest().getURI().getPath();
            if (path.startsWith("/fallback/")) {
                var serviceName = path.substring("/fallback/".length());
                exchange.getResponse().setStatusCode(HttpStatus.SERVICE_UNAVAILABLE);
                exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_PROBLEM_JSON);
                var problem = ProblemDetails.from(
                    HttpStatus.SERVICE_UNAVAILABLE, "SERVICE_UNAVAILABLE",
                    "Service '" + serviceName + "' is currently unavailable. Please try again later.",
                    path);
                try {
                    var json = objectMapper.writeValueAsBytes(problem);
                    var buffer = exchange.getResponse().bufferFactory().wrap(json);
                    return exchange.getResponse().writeWith(Mono.just(buffer));
                } catch (Exception e) {
                    return exchange.getResponse().setComplete();
                }
            }
            return chain.filter(exchange);
        };
    }
}
