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
                        .title("SporeKart Enterprise AI Platform API")
                        .version("1.0.0")
                        .description("Enterprise AI Platform — Gateway, Provider, Prompt, RAG, Search, Chat, Content, Workflow, Monitoring")
                        .contact(new Contact()
                                .name("SporeKart Platform Team")));
    }
}
