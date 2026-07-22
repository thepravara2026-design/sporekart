package com.sporekart.prompt.config;

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
                        .title("SporeKart Enterprise Prompt Management Platform API")
                        .version("1.0.0")
                        .description("Enterprise Prompt Management — Registry, Versioning, Templates, Approval, Playground, Governance")
                        .contact(new Contact()
                                .name("SporeKart Platform Team")));
    }
}
