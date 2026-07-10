package com.sporekart.identity.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI identityServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("SporeKart Identity Service API")
                        .version("0.1.0")
                        .description("Platform foundation API documentation for the identity service."));
    }
}
