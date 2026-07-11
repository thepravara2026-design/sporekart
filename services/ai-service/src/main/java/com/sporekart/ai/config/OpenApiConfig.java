package com.sporekart.ai.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("SporeKart AI Service API")
                        .version("1.0.0")
                        .description("Operations Platform & ERP Integration API")
                        .contact(new Contact()
                                .name("SporeKart Platform Team")));
    }
}
