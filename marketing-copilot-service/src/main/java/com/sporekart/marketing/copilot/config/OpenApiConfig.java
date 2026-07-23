package com.sporekart.marketing.copilot.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI marketingCopilotOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("SporeKart Enterprise Marketing Copilot API")
                .description("Enterprise Marketing Intelligence Platform — AI CMO for SporeKart")
                .version("1.0.0")
                .contact(new Contact()
                    .name("SporeKart Engineering")
                    .email("engineering@sporekart.com")));
    }
}
