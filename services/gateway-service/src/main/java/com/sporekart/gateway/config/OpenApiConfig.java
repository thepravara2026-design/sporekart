package com.sporekart.gateway.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI gatewayOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("SporeKart Enterprise API Gateway")
                .description("Single entry point for all frontend, mobile, AI, and external clients")
                .version("1.0.0"))
            .servers(List.of(
                new Server().url("http://localhost:8080").description("Local Development"),
                new Server().url("https://api.sporekart.com").description("Production")
            ));
    }
}
