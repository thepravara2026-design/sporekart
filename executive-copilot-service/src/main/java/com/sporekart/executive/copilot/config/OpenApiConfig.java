package com.sporekart.executive.copilot.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI executiveCopilotOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("SporeKart Enterprise Executive Copilot API")
                .description("Enterprise Executive Intelligence Platform — AI CEO for SporeKart")
                .version("1.0.0")
                .contact(new Contact()
                    .name("SporeKart Engineering")
                    .email("engineering@sporekart.com")));
    }
}
