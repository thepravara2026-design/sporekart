package com.sporekart.admin.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI adminCopilotOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("SporeKart Enterprise Admin Copilot Service")
                .description("REST API for the Admin Copilot — enterprise operations, analytics, forecasting, alerts, and reporting assistant.")
                .version("0.2.0")
                .contact(new Contact()
                    .name("SporeKart Platform Team")
                    .email("platform@sporekart.com"))
                .license(new License()
                    .name("Proprietary")
                    .url("https://sporekart.com/license")));
    }
}
