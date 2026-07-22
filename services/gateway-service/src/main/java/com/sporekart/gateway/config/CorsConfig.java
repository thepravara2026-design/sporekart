package com.sporekart.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;

@Configuration
public class CorsConfig {

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public WebFilter corsFilter() {
        return (ServerWebExchange exchange, WebFilterChain chain) -> {
            var request = exchange.getRequest();
            var headers = request.getHeaders();
            var origin = headers.getFirst(HttpHeaders.ORIGIN);

            if (origin != null && !origin.isBlank()) {
                setCorsHeaders(exchange.getResponse(), origin);

                if (request.getMethod() == HttpMethod.OPTIONS
                    && headers.getFirst("Access-Control-Request-Method") != null) {
                    exchange.getResponse().setStatusCode(HttpStatus.OK);
                    return exchange.getResponse().setComplete();
                }
            }

            return chain.filter(exchange);
        };
    }

    private static void setCorsHeaders(ServerHttpResponse response, String origin) {
        var h = response.getHeaders();
        h.set(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, origin);
        h.set(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "GET,POST,PUT,DELETE,PATCH,OPTIONS");
        h.set(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, "*");
        h.set(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS,
            HttpHeaders.AUTHORIZATION + ",X-Request-Id,X-Correlation-Id,X-Trace-Id");
        h.set(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");
        h.set(HttpHeaders.ACCESS_CONTROL_MAX_AGE, "3600");
        h.set(HttpHeaders.VARY, HttpHeaders.ORIGIN);
    }
}
