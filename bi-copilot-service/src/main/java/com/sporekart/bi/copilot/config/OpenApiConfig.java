package com.sporekart.bi.copilot.config;

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
                        .title("SporeKart Enterprise Business Intelligence Copilot API")
                        .version("1.0.0")
                        .description("Enterprise Business Intelligence Copilot for Cross-Domain Analytics & Insights")
                        .contact(new Contact()
                                .name("SporeKart Platform Team")));
    }
}
