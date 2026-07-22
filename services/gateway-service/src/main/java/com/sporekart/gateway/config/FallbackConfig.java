package com.sporekart.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Configuration
public class FallbackConfig {

    @Bean
    public WebFilter fallbackFilter() {
        return (ServerWebExchange exchange, WebFilterChain chain) -> {
            var path = exchange.getRequest().getURI().getPath();
            if (path.startsWith("/fallback/")) {
                var serviceName = path.substring("/fallback/".length());
                exchange.getResponse().setStatusCode(
                    org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE);
                var body = "{\"status\":503,\"title\":\"Service Unavailable\",\"detail\":\"Service '%s' is currently unavailable. Please try again later.\"}".formatted(serviceName);
                var buffer = exchange.getResponse().bufferFactory().wrap(body.getBytes());
                exchange.getResponse().getHeaders().setContentType(
                    org.springframework.http.MediaType.APPLICATION_JSON);
                return exchange.getResponse().writeWith(Mono.just(buffer));
            }
            return chain.filter(exchange);
        };
    }
}
